package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.OrderStatusCountVO;
import com.imooc.utils.PagedGridResult;

public interface MyOrdersService {
    /**
     * 查询我的订单列表
     */
    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    Orders queryMyOrders(String userId, String orderId);

    boolean updateReceiveOrderStatus(String orderId);

    boolean deleteOrder(String userId, String orderId);

    OrderStatusCountVO getOrderStatusCounts(String userId);

    PagedGridResult getOrderTrend(String userId, Integer page, Integer pageSize);
}
