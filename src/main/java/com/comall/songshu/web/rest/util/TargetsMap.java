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
        targets.put("注册用户数","NewRegisterCount");
        targets.put("首单用户数","FirstOrderedConsumerCount");
        targets.put("非首单用户数","NotFirstOrderConsumerCount");
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
        targetNames.put("NewRegisterCount", "注册用户数");
        targetNames.put("FirstOrderedConsumerCount", "首单用户数");
        targetNames.put("NotFirstOrderConsumerCount", "非首单用户数");
        return targetNames;
    }
}
