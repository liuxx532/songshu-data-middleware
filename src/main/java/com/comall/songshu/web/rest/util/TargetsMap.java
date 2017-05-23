package com.comall.songshu.web.rest.util;


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
        targets.put("销售额趋势图","RevenueTrend");
        targets.put("订单量趋势图","OrderCountTrend");
        targets.put("客单价趋势图","AvgOrderRevenueTrend");
        targets.put("访客数趋势图","UniqueVisitorsTrend");
        targets.put("退款金额趋势图","RefundTrend");
        targets.put("毛利率趋势图","GrossMarginRateTrend");
        targets.put("注册用户占比", "NewRegisterRate");
        targets.put("是否首次消费占比", "FirstOrderedRate");
        targets.put("销售额品类排行榜", "CategoryRevenueRanking");
        return targets;
    }


    static Map<String,String> productTargets = new HashMap<>();

    public static Map<String,String> productTargets() {
        productTargets.put("商品销售额","ProductRevenue");
        productTargets.put("商品页面雷达图","ProductRadar");
        productTargets.put("商品品类销售占比排行","ProductCategoryRank");
        productTargets.put("关联商品","ProductLinkedSales");
        return productTargets;
    }

    static Map<String,String> memberTargets = new HashMap<>();

    public static Map<String,String> memberTargets() {
        memberTargets.put("分享详情","MemberShareDetail");
        memberTargets.put("分享趋势","MemberShareTrend");
        memberTargets.put("渠道注册用户","ChannelRegisterMember");
        memberTargets.put("用户数据","MemberDetail");
        memberTargets.put("用户漏斗","MemberFunnel");
        return memberTargets;
    }

    static Map<String,String> channelTargets = new HashMap<>();

    public static Map<String,String> channelTargets() {
        channelTargets.put("访问时长分布","VisitTimeDistribution");
        channelTargets.put("访问深度分布","VisitDeepDistribution");
        channelTargets.put("TOP10机型分布","ManufacturerRank");
        channelTargets.put("TOP10地区分布","RegionRank");
        channelTargets.put("年龄分布","AgeDistribution");
        channelTargets.put("性别分布","SexDistribution");
        channelTargets.put("渠道页面销售额","ChannelRevenue");
        channelTargets.put("渠道页面订单量","ChannelOrderCount");
        channelTargets.put("渠道页面客单价","ChannelAvgOrderRevenue");
        channelTargets.put("渠道页面访客数","ChannelUniqueVisitors");
        channelTargets.put("渠道页面毛利率","ChannelGrossMarginRate");
        channelTargets.put("渠道页面消费用户数","ChannelConsumerCount");
        channelTargets.put("渠道分页页面","ChannelPageInfo");
        return channelTargets;
    }

    static Map<String,String> mockTargets = new HashMap<>();

    public static Map<String,String> mockTargets() {
        mockTargets.put("用户A自定义数据","userA");
        mockTargets.put("用户B自定义数据","userB");
        mockTargets.put("用户C自定义数据","userC");
        mockTargets.put("用户D自定义数据","userD");
        return mockTargets;
    }

}
