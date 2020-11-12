package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.Orders;
import com.imooc.service.center.MyCommentsService;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "用户中心我的评价", tags = {"用户中心我的评价相关接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;
    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/pending")
    public IMOOCJSONResult pending(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单Id", required = true)
            @RequestParam String orderId) {
        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }
        Orders orders = (Orders) checkResult.getData();
        if (orders.getIsComment() == YesOrNo.YES.type) {
            return IMOOCJSONResult.errorMsg("该笔订单已经评价");
        }
        List<OrderItems> list = myCommentsService.queryPendingComment(orderId);
        return IMOOCJSONResult.ok(list);
    }

    private IMOOCJSONResult checkUserOrder(String userId, String orderId) {
        Orders orders = myOrdersService.queryMyOrders(userId, orderId);
        if (orders == null) {
            return IMOOCJSONResult.errorMsg("订单不存在！");
        }
        return IMOOCJSONResult.ok(orders);
    }
}

