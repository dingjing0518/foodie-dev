package com.imooc.controller;

import com.imooc.enums.PayMethod;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.OrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    private OrderService orderService;

    @ApiOperation(value = "创建订单", notes = "创建订单", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO, HttpServletRequest request, HttpServletResponse response) {
        if (submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type
                && submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        //1.创建订单
        String orderId = orderService.createOrder(submitOrderBO);
        //2.创建订单以后，移除购物车中已结算的商品
        /**
         *1001
         *2002->用户购买
         *3003->用户购买
         *4004
         */
        //todo 整合redis后，完善购物车的已结算商品清楚，并且同步到前端的cookie
//        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);
        //3.向支付中心发送当前订单，用于保存支付中心的订单数据
        return IMOOCJSONResult.ok(orderId);
    }
}
