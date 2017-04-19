package com.comall.songshu.web.rest.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lgx on 17/4/7.
 */
public class TargetsMap {
    static Map<String,String> targets = new HashMap<>();

    public static Map<String,String> getTargets() {
        targets.put("OrderCount","订单量");
        targets.put("FinishedGroup","关闭团数");
        targets.put("TotalRevenue", "销售额");
        targets.put("VisitorCount", "访客数");
        targets.put("ConsumerCount", "消费用户数");
        targets.put("ConsumerConversionRate", "消费用户转化率");
        targets.put("AvgOrderRevenue", "客单价");
        targets.put("GrossMargin", "毛利");
        targets.put("OrderCountTrend", "订单量趋势图");
        targets.put("TotalRevenueTrend", "销售额趋势图");
        targets.put("VisitorCountTrend", "访客数趋势图");
        targets.put("ConsumerCountTrend", "消费用户数趋势图");
        targets.put("ConsumerConversionRateTrend", "消费用户转化率趋势图");
        targets.put("AvgOrderRevenueTrend", "客单价趋势图");
        targets.put("GrossMarginTrend", "毛利趋势图");
        targets.put("GoodsSaleRank", "商品销量排行");
        targets.put("GoodsRevenueRank", "商品销售额排行");

        return targets;
    }

}
