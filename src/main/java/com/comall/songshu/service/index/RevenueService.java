package com.comall.songshu.service.index;

import com.comall.songshu.repository.index.RevenueRepository;
import com.comall.songshu.web.rest.AuthorResource;
import com.comall.songshu.web.rest.util.*;
import com.comall.songshu.web.rest.vm.TopStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * 销售额
 * Created by lgx on 17/4/18.
 */
@Component
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;


    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    public String  getRevenue(String target,String platformName, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {

        int platform = TransferUtil.getPlatform(platformName);
        Double revenueResult;
        Double chainRevenueResult;
        if (platform<0){//
            revenueResult = revenueRepository.getRevenueWithAllPlatform(beginTime,endTime);
            chainRevenueResult = revenueRepository.getRevenueWithAllPlatform(chainBeginTime,chainEndTime);
        }else {
            revenueResult = revenueRepository.getRevenueWithSinglePlatform(platform,beginTime,endTime);
            chainRevenueResult = revenueRepository.getRevenueWithSinglePlatform(platform,chainBeginTime,chainEndTime);
        }

        Double revenue = Optional.ofNullable(revenueResult).orElse(0.00);
        Double chainRevenue = Optional.ofNullable(chainRevenueResult).orElse(0.00);

        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(revenue,chainRevenue);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");
    }

    public String getRevenueTrend(String target,String platformName,Timestamp beginTime,Timestamp endTime ,Timestamp chainBeginTime,Timestamp chainEndTime, int aggCount){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interValue=ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime,aggCount);

        log.info("paltform",platform);
        log.info("interVlue",interValue);

        //当前
        List<Object[] > currentRevenueResult ;
        //环比
        List<Object[] > chainRevenueResult ;

        if (platform < 0){ //所有平台
            currentRevenueResult = revenueRepository.getRevenueTrendWithAllPlatform(beginTime,endTime,interValue);
            chainRevenueResult = revenueRepository.getRevenueTrendWithAllPlatform(chainBeginTime,chainEndTime,interValue);
        }else {//单个平台
            currentRevenueResult = revenueRepository.getRevenueTrendWithSinglePlatform(platform,beginTime,endTime,interValue);
            chainRevenueResult = revenueRepository.getRevenueTrendWithSinglePlatform(platform,chainBeginTime,chainEndTime,interValue);
        }

        List<Object[]> currentRevenue = Optional.ofNullable(currentRevenueResult).orElse(null);
        List<Object[]> chainRevenue = Optional.ofNullable(chainRevenueResult).orElse(null);

        return JsonStringBuilder.buildTrendJsonString(currentRevenue,chainRevenue);
    }

}
