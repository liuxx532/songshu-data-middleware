package com.comall.songshu.service;

import com.comall.songshu.repository.GrossMarginRateRepository;
import com.comall.songshu.repository.RevenueRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

        //商品成本
        Double productCostResult;
        Double chainProductCostResult;
        //销售额
        Double revenueResult;
        Double chainRevenueResult;

        if (platform<0){//
            productCostResult = grossMarginRateRepository.getProductCostWithAllPlatform(beginTime,endTime);
            chainProductCostResult = grossMarginRateRepository.getProductCostWithAllPlatform(beginTime,endTime);
            revenueResult = revenueRepository.getRevenueWithAllPlatform(beginTime,endTime);
            chainRevenueResult = revenueRepository.getRevenueWithAllPlatform(chainBeginTime,chainEndTime);
        }else {
            productCostResult = grossMarginRateRepository.getProductCostWithSinglePlatform(platform,beginTime,endTime);
            chainProductCostResult = grossMarginRateRepository.getProductCostWithSinglePlatform(platform,beginTime,endTime);
            revenueResult = revenueRepository.getRevenueWithSinglePlatform(platform,beginTime,endTime);
            chainRevenueResult = revenueRepository.getRevenueWithSinglePlatform(platform,chainBeginTime,chainEndTime);
        }

        Double productCost = Optional.ofNullable(productCostResult).orElse(0.00);
        Double chainProductCost = Optional.ofNullable(chainProductCostResult).orElse(0.00);
        Double revenue = Optional.ofNullable(revenueResult).orElse(0.00);
        Double chainRevenue = Optional.ofNullable(chainRevenueResult).orElse(0.00);


        //毛利率 = （销售额 - 商品成本）／ 销售额 * 100%
        //注：这里毛利率先不用乘以100%
        Double grossMarginRate = Optional.of(revenue)
            .filter((value) -> value > 0)
            .map( r -> (r-productCost)/r)
            .orElse(0.00);

        Double chainGrossMarginRate = Optional.of(chainRevenue)
            .filter((value) -> value > 0)
            .map( r -> (r-chainProductCost)/r)
            .orElse(0.00);

        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(grossMarginRate,chainGrossMarginRate);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");


    }



}
