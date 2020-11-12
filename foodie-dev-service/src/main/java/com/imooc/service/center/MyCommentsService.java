package com.imooc.service.center;

import com.imooc.pojo.OrderItems;

import java.util.List;

public interface MyCommentsService {
    List<OrderItems> queryPendingComment(String orderId);
}
