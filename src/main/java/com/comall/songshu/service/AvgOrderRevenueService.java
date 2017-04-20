package com.comall.songshu.service;

import com.comall.songshu.repository.AvgOrderRevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wdc on 2017/4/19.
 */
@Service
public class AvgOrderRevenueService {

    @Autowired
    private AvgOrderRevenueRepository avgOrderRevenueRepository;

    public Object[] getAvgOrderRevenue(){
        return  avgOrderRevenueRepository.findAll().toArray();
    }


}
