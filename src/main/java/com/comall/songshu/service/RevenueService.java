package com.comall.songshu.service;

import com.comall.songshu.repository.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by lgx on 17/4/18.
 */
@Component
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    public Object[]  getRevenue() {

        return revenueRepository.findAll().toArray();
    }



    public Double getRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime) {
        return revenueRepository.getRevenueWithAllPlatform(beginTime, endTime);
    }


    public Double getRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime) {
        return revenueRepository.getRevenueWithSinglePlatform(platform, beginTime, endTime);
    }

    // TODO add trend
}
