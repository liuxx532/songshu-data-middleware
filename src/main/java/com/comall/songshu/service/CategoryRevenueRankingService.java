package com.comall.songshu.service;

import com.comall.songshu.repository.CategoryRevenueRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by wdc on 2017/4/25.
 */
@Service
public class CategoryRevenueRankingService {

    @Autowired
    private CategoryRevenueRankingRepository categoryRevenueRankingRepository;


    // List<Object[]> 是排行榜数据
    // Object[]： 第一个元素是 String name, 第二个元素是 BigDecimal amout
    public List<Object[]> getCategoryRevenueRankingWithSinglePlatform(Integer platform, Timestamp startTime, Timestamp endTime) {
        return categoryRevenueRankingRepository.getCategoryRevenueRankingWithSinglePlatform(platform, startTime, endTime);
    }

    // List<Object[]> 是排行榜数据
    // Object[]： 第一个元素是 String name, 第二个元素是 BigDecimal amout
    public List<Object[]> getCategoryRevenueRankingWithAllPlatform(Timestamp startTime, Timestamp endTime) {
        return categoryRevenueRankingRepository.getCategoryRevenueRankingWithAllPlatform(startTime, endTime);
    }

    // TODO add  interface for controller

    // TODO add trend
}
