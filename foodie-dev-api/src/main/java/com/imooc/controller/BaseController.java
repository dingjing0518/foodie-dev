package com.imooc.controller;

public class BaseController {
    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOODIE_SHOPCART = "shopcart";
    //微信支付成功->支付中心->天天吃货平台
    String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    //支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
}
