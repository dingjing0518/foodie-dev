package com.imooc.controller;

import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "购物车接口controller", tags = "购物车相关接口的api")
@RequestMapping("shopcart")
@RestController
public class ShopCartController {
    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestParam String userId,
                               @RequestBody ShopCartBO shopCartBO,
                               HttpServletRequest request,
                               HttpServletResponse httpServletResponse) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        //TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存中

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
        //TODO 前端用户在登录的情况下，同步删除后端购物车中的数据

        return IMOOCJSONResult.ok();
    }
}
