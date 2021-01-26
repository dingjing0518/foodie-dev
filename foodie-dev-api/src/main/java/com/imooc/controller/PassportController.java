package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {
        //1.判断用户名不能为空
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不为空");
        }
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();
        //1.用户名密码是否为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPwd)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //2.用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        //3.密码长度不能少于6位
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能少于6位");
        }
        //4.判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return IMOOCJSONResult.errorMsg("两次密码输入不一致");
        }
        //5.实现注册
        Users users = userService.createUser(userBO);
        users = setNullProperty(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);
        //TODO 生成用户token,存入redis
        //同步购物车数据
        synchShopcartData(request, response, users.getId());
        return IMOOCJSONResult.ok(users);
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        //1.用户名密码是否为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //2.实现登录
        Users users = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (users == null) {
            return IMOOCJSONResult.errorMsg("用户名或者密码不正确");
        }
        users = setNullProperty(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);

        //TODO 生成用户token,存入redis
        //同步购物车数据
        synchShopcartData(request, response, users.getId());
        return IMOOCJSONResult.ok(users);
    }

    /**
     * 注册登录成功后，同步cookie和redis中的购物车数据
     */
    private void synchShopcartData(HttpServletRequest request,
                                   HttpServletResponse response, String userId) {
        /**
         * 1.redis中无数据，如果cookie中的购物车为空，那么这个时候不做任何处理
         *                 如果cookie中的购物车不为空，直接放入redis中
         * 2.redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
         *                 如果cookie中的购物车不为空，如果cookie中的某个商品在redis中存在，则以cookie为主，删除redis的，把cookie的商品直接覆盖redis
         * 3.同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步的
         */
        //从redis中获取购物车
        String shopCartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        //从cookie中获取购物车
        String shopCartJsonCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(shopCartJsonRedis)) {
            //redis为空，cookie中的购物车不为空，直接放入redis中
            if (StringUtils.isNotBlank(shopCartJsonCookie)) {
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopCartJsonRedis);
            }
        } else {
            //redis不为空，cookie不为空，合并
            if (StringUtils.isNotBlank(shopCartJsonCookie)) {
                /**
                 * 1.已经存在的，把cookie中的对应的数量，，覆盖redis
                 * 2.该项商品标记为待删除，统一放入一个待删除的list
                 * 3.从cookie中清理所有待删除list
                 * 4.合并redis和cookie中的数据
                 * 5.更新到redis和cookie的数据
                 */
                List<ShopCartBO> removedCart = new ArrayList<>();
                List<ShopCartBO> redisCart = JsonUtils.jsonToList(shopCartJsonRedis, ShopCartBO.class);
                List<ShopCartBO> cookieCart = JsonUtils.jsonToList(shopCartJsonCookie, ShopCartBO.class);
                for (ShopCartBO redisCartItem : redisCart) {
                    for (ShopCartBO cookieCartItem : cookieCart) {
                        if (redisCartItem.getSpecId().equals(cookieCartItem.getSpecId())) {
                            redisCartItem.setBuyCounts(cookieCartItem.getBuyCounts());
                            removedCart.add(cookieCartItem);
                            break;
                        }
                    }
                }
                //从cookie中清理所有待删除list
                cookieCart.removeAll(removedCart);
                //合并redis和cookie中的数据
                redisCart.addAll(cookieCart);
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(redisCart), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(redisCart));
            } else {
                //redis不为空，cookie为空，直接把redis覆盖cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopCartJsonRedis, true);
            }
        }
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        //清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");
        //用户退出登录，需要清空cookie中购物车数据
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);
        //todo 分布式会话中需要清除用户数据
        return IMOOCJSONResult.ok();
    }

    private Users setNullProperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
        return users;
    }
}
