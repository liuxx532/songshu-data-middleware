package com.comall.songshu.service;

import com.comall.songshu.repository.GrossMarginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by liugaoyu on 2017/4/20.
 */
@Service
public class GrossMarginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrossMarginService.class);

    @Autowired
    private GrossMarginRepository grossMarginRepository;

    public Object[] getGrossMargin(){
        return  grossMarginRepository.findAll().toArray();
    }


    public Double getProductCostWithAllPlatform(Timestamp beginTime, Timestamp endTime) {
        return grossMarginRepository.getProductCostWithAllPlatform(beginTime, endTime);
    }

    public Double getProductCostWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime) {
        return grossMarginRepository.getProductCostWithSinglePlatform(platform, beginTime, endTime);
    }

    public Double getGrossMarginWithAllPlatform(Double totalRevenue, Timestamp beginTime, Timestamp endTime) {
        Double cost = getProductCostWithAllPlatform(beginTime, endTime);
        if (totalRevenue <= 0.0) {
            LOGGER.warn("Total revenue is {}", totalRevenue);
            return 0.0;
        }

        return (totalRevenue - cost) / totalRevenue;
    }

    public Double getGrossMarginWithSinglePlatform(Double totalRevenue, Integer platform, Timestamp beginTime, Timestamp endTime) {
        Double cost = getProductCostWithSinglePlatform(platform, beginTime, endTime);
        if (totalRevenue <= 0.0) {
            LOGGER.warn("Total revenue is {}", totalRevenue);
            return 0.0;
        }

        return (totalRevenue - cost) / totalRevenue;
    }

}
