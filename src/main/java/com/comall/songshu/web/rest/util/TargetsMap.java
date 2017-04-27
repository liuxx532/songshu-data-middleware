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
        targets.put("销售额","Revenue");
        targets.put("订单量","OrderCount");
        targets.put("客单价","AvgOrderRevenue");
        targets.put("访客数","UniqueVisitors");
        targets.put("退款金额","Refund");
        targets.put("毛利率","GrossMarginRate");
//        targets.put("注册用户数","NewRegisterCount");
//        targets.put("首单用户数","FirstOrderedConsumerCount");
//        targets.put("非首单用户数","NotFirstOrderConsumerCount");
        targets.put("销售额趋势图","RevenueTrend");
        targets.put("订单量趋势图","OrderCountTrend");
        targets.put("客单价趋势图","AvgOrderRevenueTrend");
        targets.put("访客数趋势图","UniqueVisitorsTrend");
        targets.put("退款金额趋势图","RefundTrend");
        targets.put("毛利率趋势图","GrossMarginRateTrend");
        targets.put("注册用户占比", "NewRegisterRate");
        targets.put("是否首次消费占比", "FirstOrderedRate");
        targets.put("销售额品类排行帮", "CategoryRevenueRanking");
        return targets;
    }

    static Map<String,String> targetNames = new HashMap<>();

    public static Map<String,String> getTargetNames() {
        targetNames.put("Revenue", "销售额");
        targetNames.put("OrderCount","订单量");
        targetNames.put("AvgOrderRevenue", "客单价");
        targetNames.put("UniqueVisitors", "访客数");
        targetNames.put("Refund", "退款金额");
        targetNames.put("GrossMarginRate", "毛利率");
        targetNames.put("RevenueTrend", "销售额趋势图");
        targetNames.put("OrderCountTrend","订单量趋势图");
        targetNames.put("AvgOrderRevenueTrend", "客单价趋势图");
        targetNames.put("UniqueVisitorsTrend", "访客数趋势图");
        targetNames.put("RefundTrend", "退款金额趋势图");
        targetNames.put("GrossMarginRateTrend", "毛利率趋势图");
//        targetNames.put("NewRegisterCount", "注册用户数");
//        targetNames.put("FirstOrderedConsumerCount", "首单用户数");
//        targetNames.put("NotFirstOrderConsumerCount", "非首单用户数");
        targetNames.put("NewRegisterRate", "注册用户占比");
        targetNames.put("FirstOrderedRate", "是否首次消费占比");
        targetNames.put("CategoryRevenueRanking", "销售额品类排行帮");
        return targetNames;
    }
}
