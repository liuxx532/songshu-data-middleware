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


        //所有平台
        List<Object[] > currentAllPlatformResult;
        List<Object[] > chainAllPlatformResult;

        //单个平台
        List<Object[] > currentSinglePlatformResult;
        List<Object[] > chainSinglePlatformResult;


        //所有平台
        if (platform < 0){ //所有平台
            //当前
            currentAllPlatformResult = avgOrderRevenueRepository.getAvgRevenueTrendWithAllPlatform(beginTime,endTime,interValue);
            //环比
            chainAllPlatformResult = avgOrderRevenueRepository.getAvgRevenueTrendWithAllPlatform(chainBeginTime,chainEndTime,interValue);

            List<Object[]> currentAllPlatform = Optional.ofNullable(currentAllPlatformResult).orElse(null);
            List<Object[]> chainAllPlatform = Optional.ofNullable(chainAllPlatformResult).orElse(null);

            return JsonStringBuilder.buildTrendJsonString(currentAllPlatform,chainAllPlatform);
        }else {//单个平台
            currentSinglePlatformResult = avgOrderRevenueRepository.getAvgRevenueTrendWithSinglePlatform(platform,beginTime,endTime,interValue);
            chainSinglePlatformResult = avgOrderRevenueRepository.getAvgRevenueTrendWithSinglePlatform(platform,chainBeginTime,chainEndTime,interValue);

            List<Object[]> currentSinglePlatform = Optional.ofNullable(currentSinglePlatformResult).orElse(null);
            List<Object[]> chainSinglePlatform = Optional.ofNullable(chainSinglePlatformResult).orElse(null);

            return JsonStringBuilder.buildTrendJsonString(currentSinglePlatform,chainSinglePlatform);
        }

    }


}
