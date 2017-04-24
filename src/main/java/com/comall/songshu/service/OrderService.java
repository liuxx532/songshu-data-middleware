package com.comall.songshu.service;

import com.comall.songshu.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by liugaoyu on 2017/4/20.
 */
// 订单量
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Object[] getOrder(){
        return orderRepository.findAll().toArray();
    }

    // 所有平台
    public Double getOrderCountWithAllPlatform(Timestamp beginTime, Timestamp endTime) {
        return orderRepository.getOrderCountWithAllPlatform(beginTime, endTime);
    }

    // 单个平台
    public Double getOrderCountWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime) {
        return orderRepository.getOrderCountWithSinglePlatform(platform, beginTime, endTime);
    }

}
