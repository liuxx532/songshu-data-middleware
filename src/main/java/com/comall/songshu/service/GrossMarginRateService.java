package com.comall.songshu.service;

import com.comall.songshu.repository.GrossMarginRateRepository;
import com.comall.songshu.repository.RevenueRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Created by liugaoyu on 2017/4/20.
 */
@Service
public class GrossMarginRateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrossMarginRateService.class);

    @Autowired
    private GrossMarginRateRepository grossMarginRateRepository;

    @Autowired
    private RevenueRepository revenueRepository;

    public Object[] findGrossMarginRate(){
        return  grossMarginRateRepository.findAll().toArray();
    }


    public String  getGrossMarginRate(String target,String platformName, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {

        int platform = TransferUtil.getPlatform(platformName);

        //毛利率 = （销售额 - 商品成本）／ 销售额 * 100%
        //注：这里毛利率先不用乘以100%
        Double grossMarginRateResult;
        Double chainGrossMarginRateResult;

        //商品成本
//        Double productCostResult;
//        Double chainProductCostResult;
        //销售额
//        Double revenueResult;
//        Double chainRevenueResult;

        if (platform<0){//
              grossMarginRateResult = grossMarginRateRepository.getCrossMarginWithAllPlatform(beginTime,endTime);
              chainGrossMarginRateResult  = grossMarginRateRepository.getCrossMarginWithAllPlatform(chainBeginTime,chainEndTime);
//            productCostResult = grossMarginRateRepository.getProductCostWithAllPlatform(beginTime,endTime);
//            chainProductCostResult = grossMarginRateRepository.getProductCostWithAllPlatform(beginTime,endTime);
//            revenueResult = revenueRepository.getRevenueWithAllPlatform(beginTime,endTime);
//            chainRevenueResult = revenueRepository.getRevenueWithAllPlatform(chainBeginTime,chainEndTime);
        }else {
              grossMarginRateResult = grossMarginRateRepository.getCrossMarginWithSinglePlatform(beginTime,endTime,platform);
              chainGrossMarginRateResult  = grossMarginRateRepository.getCrossMarginWithSinglePlatform(chainBeginTime,chainEndTime,platform);
//            productCostResult = grossMarginRateRepository.getProductCostWithSinglePlatform(platform,beginTime,endTime);
//            chainProductCostResult = grossMarginRateRepository.getProductCostWithSinglePlatform(platform,beginTime,endTime);
//            revenueResult = revenueRepository.getRevenueWithSinglePlatform(platform,beginTime,endTime);
//            chainRevenueResult = revenueRepository.getRevenueWithSinglePlatform(platform,chainBeginTime,chainEndTime);
        }

        Double grossMarginRate = Optional.ofNullable(grossMarginRateResult).orElse(0.00);
        Double chainGrossMarginRate = Optional.ofNullable(chainGrossMarginRateResult).orElse(0.00);


        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(grossMarginRate,chainGrossMarginRate);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");


    }

    public String getGrossMarginRateTrend(String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime, int aggCount){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interValue = ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime,aggCount);


        //所有平台
        List<Object[] > currentAllPlatformResult = null;
        List<Object[] > chainAllPlatformResult = null;

        //单个平台
        List<Object[] > currentSinglePlatformResult = null;
        List<Object[] > chainSinglePlatformResult = null;


        //所有平台
        if (platform < 0){ //所有平台
            //当前
            currentAllPlatformResult = grossMarginRateRepository.getCrossMarginTrendWithAllPlatform(beginTime, endTime, interValue);
            //环比
            chainAllPlatformResult = grossMarginRateRepository.getCrossMarginTrendWithAllPlatform(chainBeginTime, chainEndTime, interValue);

            List<Object[]> currentAllPlatform =null ;
            List<Object[]> chainAllPlatform =null ;

            if (null != currentAllPlatformResult){
                currentAllPlatform= currentAllPlatformResult;
            }
            if(null != chainAllPlatformResult){
                chainAllPlatform= chainAllPlatformResult;
            }

            return JsonStringBuilder.buildTrendJsonString(currentAllPlatform, chainAllPlatform);
        }else {//单个平台
            currentSinglePlatformResult = grossMarginRateRepository.getCrossMarginTrendWithSinglePlatform(beginTime, endTime, interValue, platform);
            chainSinglePlatformResult = grossMarginRateRepository.getCrossMarginTrendWithSinglePlatform(chainBeginTime, chainEndTime, interValue, platform);

            List<Object[]> currentSinglePlatform =null;
            List<Object[]> chainSinglePlatform =null;

            if(null != currentSinglePlatformResult){
                currentSinglePlatform = currentSinglePlatformResult;

            }
            if (null != chainSinglePlatformResult){
                chainSinglePlatform = chainSinglePlatformResult;
            }
            return JsonStringBuilder.buildTrendJsonString(currentSinglePlatform, chainSinglePlatform);
        }

    }



}
