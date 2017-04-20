package com.comall.songshu.service;

import com.comall.songshu.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liugaoyu on 2017/4/20.
 */
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Object[] getOrder(){
        return orderRepository.findAll().toArray();
    }

}
