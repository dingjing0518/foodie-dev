package com.imooc.service;


import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;

import java.util.List;

public interface OrderService {
    /**
     * 用于创建订单相关
     *
     * @param submitOrderBO 用于创建订单的BO对象
     * @param shopCartBOList 购物车列表
     */
    OrderVO createOrder(SubmitOrderBO submitOrderBO, List<ShopCartBO> shopCartBOList);

    /**
     * 修改订单状态
     *
     * @param orderId     订单id
     * @param orderStatus 订单状态
     */
    void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     *
     * @param orderId 订单Id
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    void closeOrder();
}
