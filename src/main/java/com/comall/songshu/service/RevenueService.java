package com.comall.songshu.service;

import com.comall.songshu.repository.RevenueRepository;
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

    public String getRevenueTrend(String target,String platformName,Timestamp beginTime,Timestamp endTime ,Timestamp chainBeginTime,Timestamp chainEndTime){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interValue=ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime);

        log.info("paltform",platform);
        log.info("interVlue",interValue);

        //所有平台
        List<Object[] > currentAllPlatformResult = null;
        List<Object[] > chainAllPlatformResult = null;

        //单个平台
        List<Object[] > currentSinglePlatformResult = null;
        List<Object[] > chainSinglePlatformResult = null;


        //所有平台

        if (platform < 0){ //所有平台
            //当前
            currentAllPlatformResult = revenueRepository.getRevenueTrendWithAllPlatform(beginTime,endTime,interValue);
            //环比
            chainAllPlatformResult = revenueRepository.getRevenueTrendWithAllPlatform(chainBeginTime,chainEndTime,interValue);

            List<Object[]> currentAllPlatform =null ;
            List<Object[]> chainAllPlatform =null ;

            if (null != currentAllPlatformResult){
                currentAllPlatform= currentAllPlatformResult;

            }
            if(null != chainAllPlatformResult){
                chainAllPlatform= chainAllPlatformResult;
            }

            return JsonStringBuilder.buildTrendJsonString(currentAllPlatform,chainAllPlatform);
        }else {//单个平台
            currentSinglePlatformResult = revenueRepository.getRevenueTrendWithSinglePlatform(platform,beginTime,endTime,interValue);
            chainSinglePlatformResult = revenueRepository.getRevenueTrendWithSinglePlatform(platform,chainBeginTime,chainEndTime,interValue);

            List<Object[]> currentSinglePlatform =null;
            List<Object[]> chainSinglePlatform =null;

            if(null != currentSinglePlatformResult){
                currentSinglePlatform = currentSinglePlatformResult;

            }
            if (null != chainSinglePlatformResult){
                chainSinglePlatform = chainSinglePlatformResult;
            }
            return JsonStringBuilder.buildTrendJsonString(currentSinglePlatform,chainSinglePlatform);
        }

    }

}
