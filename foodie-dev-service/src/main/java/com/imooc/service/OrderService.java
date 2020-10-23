package com.imooc.service;


import com.imooc.pojo.bo.SubmitOrderBO;

public interface OrderService {
    /**
     * 用于创建订单相关
     * @param submitOrderBO 用于创建订单的BO对象
     */
    String createOrder(SubmitOrderBO submitOrderBO);
}
