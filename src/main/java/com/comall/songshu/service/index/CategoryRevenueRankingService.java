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

        // 没有查询到任何值，返回默认Json串
        if (current == null || current.size() == 0) {
            return defaultRanks();
        }

        return JsonStringBuilder.buildRankJsonString(current);
    }

    // 当在某个时间区间查询数据库时，如果没有查到记录
    // 返回默认的排行榜
    private String defaultRanks() {
        return "[{\"target\": \"坚果/炒货\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}," +
            "{\"target\": \"果干/蜜饯\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}," +
            "{\"target\": \"肉类/熟食\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}," +
            "{\"target\": \"礼盒/礼品\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}," +
            "{\"target\": \"糕点/点心\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}," +
            "{\"target\": \"饼干/膨化\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}," +
            "{\"target\": \"海味/河鲜\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}," +
            "{\"target\": \"花果茶\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}," +
            "{\"target\": \"传统茶\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}," +
            "{\"target\": \"方便面\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}," +
            "{\"target\": \"糖果/巧克力\", \"datapoints\": [[0.0,1493278351678]],\"columnName\": \"\"}]";
    }


}