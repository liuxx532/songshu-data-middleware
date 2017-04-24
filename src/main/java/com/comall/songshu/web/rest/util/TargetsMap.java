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
        targets.put("OrderCount","订单量");
        targets.put("AvgOrderRevenue", "客单价");
        targets.put("UniqueVisitors", "访客数");
        targets.put("Refund", "退款金额");
        targets.put("GrossMarginRate", "毛利率");
        targets.put("NewRegisterCount", "注册用户数");
        targets.put("FirstOrderedConsumerCount", "首单用户数");
        targets.put("NotFirstOrderConsumerCount", "非首单用户数");
        return targets;
    }

}
