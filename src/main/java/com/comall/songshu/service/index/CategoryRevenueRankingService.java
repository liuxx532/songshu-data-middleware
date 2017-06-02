package com.comall.songshu.service.index;

import com.comall.songshu.repository.index.CategoryRevenueRankingRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * 分类排行
 * Created by wdc on 2017/4/25.
 */
@Service
public class CategoryRevenueRankingService {

    @Autowired
    private CategoryRevenueRankingRepository categoryRevenueRankingRepository;


    public String getCategoryRevenueRanking(String platformName, Timestamp beginTime, Timestamp endTime) {

        // 平台
        int platform = TransferUtil.getPlatform(platformName);
        // 当前
        List<Object[] > current;

        if (platform < 0){// 所有平台
            current = categoryRevenueRankingRepository.getCategoryRevenueRankingWithAllPlatform(beginTime, endTime);
        } else {
            //单个平台
            current = categoryRevenueRankingRepository.getCategoryRevenueRankingWithSinglePlatform(platform, beginTime, endTime);
        }

        return JsonStringBuilder.buildRankJsonString(current,"品类销售额");
    }




}
