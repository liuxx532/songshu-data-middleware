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
        targets.put("Revenue", "销售额");
        targets.put("Order","订单量");
        targets.put("AvgOrderRevenue", "客单价");
        targets.put("Visitors", "活跃用户数");
        targets.put("Refund", "退款金额");
        targets.put("GrossMargin", "毛利率");
        targets.put("RevenueTrend", "销售额趋势图");
        targets.put("AvgOrderTrend", "客单价趋势图");
        targets.put("OrderTrend", "订单量趋势图");
        targets.put("VisitorsTrend", "活跃用户趋势图");
        targets.put("RefundTrend", "退款金额趋势图");
        targets.put("GrossMarginTrend", "毛利趋势图");
//        targets.put("FinishedGroup","关闭团数");
//        targets.put("ConsumerConversionRateTrend", "消费用户转化率趋势图");
//        targets.put("AvgOrderRevenueTrend", "客单价趋势图");
//        targets.put("GoodsSaleRank", "商品销量排行");
//        targets.put("GoodsRevenueRank", "商品销售额排行");

        return targets;
    }

}