package com.imooc.config;

import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {
    @Autowired
    private OrderService orderService;

    /**
     * 1.会有时间差，程序不严谨
     * 2.不支持集群
     *      单机没毛病，但是集群后，就会有多个定时任务，
     *      解决方案：1)只使用一台计算机节点，单独用来运行所有的定时任务
     *               2)延时队列
     * 3.会对数据库全表搜索，极其影响数据库性能
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void autoCloseOrder() {
        orderService.closeOrder();
        System.out.println("定时执行任务，当前时间为：" + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }

}
