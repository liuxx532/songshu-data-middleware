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
 * 毛利率
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

        if (platform<0){
              grossMarginRateResult = grossMarginRateRepository.getCrossMarginWithAllPlatform(beginTime,endTime);
              chainGrossMarginRateResult  = grossMarginRateRepository.getCrossMarginWithAllPlatform(chainBeginTime,chainEndTime);
        }else {
              grossMarginRateResult = grossMarginRateRepository.getCrossMarginWithSinglePlatform(beginTime,endTime,platform);
              chainGrossMarginRateResult  = grossMarginRateRepository.getCrossMarginWithSinglePlatform(chainBeginTime,chainEndTime,platform);
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

        //当前
        List<Object[] > currentGrossMarginRateResult;
        //环比
        List<Object[] > chainGrossMarginRateResult;


        if (platform < 0){ //所有平台
            currentGrossMarginRateResult = grossMarginRateRepository.getCrossMarginTrendWithAllPlatform(beginTime, endTime, interValue);
            chainGrossMarginRateResult = grossMarginRateRepository.getCrossMarginTrendWithAllPlatform(chainBeginTime, chainEndTime, interValue);
        }else {//单个平台
            currentGrossMarginRateResult = grossMarginRateRepository.getCrossMarginTrendWithSinglePlatform(beginTime, endTime, interValue, platform);
            chainGrossMarginRateResult = grossMarginRateRepository.getCrossMarginTrendWithSinglePlatform(chainBeginTime, chainEndTime, interValue, platform);
        }

        List<Object[]> currentGrossMarginRate = Optional.ofNullable(currentGrossMarginRateResult).orElse(null);
        List<Object[]> chainRGrossMarginRate= Optional.ofNullable(chainGrossMarginRateResult).orElse(null);

        return JsonStringBuilder.buildTrendJsonString(currentGrossMarginRate,chainRGrossMarginRate);
    }



}
