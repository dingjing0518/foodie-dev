package com.imooc.controller;

import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "购物车接口controller", tags = "购物车相关接口的api")
@RequestMapping("shopcart")
@RestController
public class ShopCartController extends BaseController {
    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestParam String userId,
                               @RequestBody ShopCartBO shopCartBO,
                               HttpServletRequest request,
                               HttpServletResponse httpServletResponse) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        //前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存中
        //需要判断当前购物车中包含已存在的商品，如果存在则累加购买数量
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopCartBO> shopCartBOList = null;
        if (StringUtils.isNotBlank(shopCartJson)) {
            //redis中已经有购物车了
            shopCartBOList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);
            //判断购物车中是否有存在已有商品，如果有的话counts累加
            boolean isHaving = false;
            for (ShopCartBO sc : shopCartBOList) {
                if (sc.getSpecId().equals(shopCartBO.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + shopCartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopCartBOList.add(shopCartBO);
            }
        } else {
            shopCartBOList.add(shopCartBO);

        }
        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartBOList));
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "从购物车删除商品", notes = "从购物车删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(@RequestParam String userId,
                               @RequestParam String itemSpecId,
                               HttpServletRequest request,
                               HttpServletResponse httpServletResponse) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        //前端用户在登录的情况下，同步删除redis购物车中的数据
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopCartBO> shopCartBOList = null;
        if (StringUtils.isNotBlank(shopCartJson)) {
            //redis中已经有购物车了
            shopCartBOList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);
            for (ShopCartBO sc : shopCartBOList) {
                if (sc.getSpecId().equals(itemSpecId)) {
                    shopCartBOList.remove(sc);
                    break;
                }
            }
            //覆盖现有redis中的购物车
            redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartBOList));
        }
        return IMOOCJSONResult.ok();
    }
}
