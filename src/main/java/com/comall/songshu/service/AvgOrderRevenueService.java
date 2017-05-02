package com.comall.songshu.service;

import com.comall.songshu.repository.AvgOrderRevenueRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 客单价
 * Created by wdc on 2017/4/19.
 */
@Service
public class AvgOrderRevenueService {

    @Autowired
    private AvgOrderRevenueRepository avgOrderRevenueRepository;


    public String getAvgOrderRevenue(String target,String platformName, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {


        int platform = TransferUtil.getPlatform(platformName);
        Double avgOrderRevenueResult;
        Double chainAvgOrderRevenueResult;

        if (platform<0){//全部
            avgOrderRevenueResult = avgOrderRevenueRepository.getAvgRevenueWithAllPlatform(beginTime,endTime);
            chainAvgOrderRevenueResult = avgOrderRevenueRepository.getAvgRevenueWithAllPlatform(chainBeginTime,chainEndTime);

        }else {
            avgOrderRevenueResult = avgOrderRevenueRepository.getAvgRevenueWithSinglePlatform(platform,beginTime,endTime);
            chainAvgOrderRevenueResult = avgOrderRevenueRepository.getAvgRevenueWithSinglePlatform(platform,beginTime,endTime);
        }

        Double avgOrderRevenue = Optional.ofNullable(avgOrderRevenueResult).orElse(0.00);
        Double chainAvgOrderRevenue = Optional.ofNullable(chainAvgOrderRevenueResult).orElse(0.00);
        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(avgOrderRevenue,chainAvgOrderRevenue);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");
    }

    // 趋势
    public String getAvgOrderRevenueTrend(String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime, int aggCount){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interValue=ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime,aggCount);

        //当前
        List<Object[] > currentAvgOrderRevenueResult;
        //环比
        List<Object[] > chainAvgOrderRevenueResult;


        //所有平台
        if (platform < 0){ //所有平台
            currentAvgOrderRevenueResult = avgOrderRevenueRepository.getAvgRevenueTrendWithAllPlatform(beginTime,endTime,interValue);
            chainAvgOrderRevenueResult = avgOrderRevenueRepository.getAvgRevenueTrendWithAllPlatform(chainBeginTime,chainEndTime,interValue);
        }else {//单个平台
            currentAvgOrderRevenueResult = avgOrderRevenueRepository.getAvgRevenueTrendWithSinglePlatform(platform,beginTime,endTime,interValue);
            chainAvgOrderRevenueResult = avgOrderRevenueRepository.getAvgRevenueTrendWithSinglePlatform(platform,chainBeginTime,chainEndTime,interValue);
        }

        List<Object[]> currentAvgOrderRevenue = Optional.ofNullable(currentAvgOrderRevenueResult).orElse(null);
        List<Object[]> chainAvgOrderRevenue = Optional.ofNullable(chainAvgOrderRevenueResult).orElse(null);

        return JsonStringBuilder.buildTrendJsonString(currentAvgOrderRevenue,chainAvgOrderRevenue);
    }


}
