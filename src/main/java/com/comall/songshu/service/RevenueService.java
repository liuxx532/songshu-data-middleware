package com.comall.songshu.service;

import com.comall.songshu.repository.RevenueRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Created by lgx on 17/4/18.
 */
@Component
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

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

        Double revenue = Optional.ofNullable(revenueResult)
            .map( d -> new BigDecimal(d).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue())
            .orElse(0.00);
        Double chainRevenue = Optional.ofNullable(chainRevenueResult)
            .map( d -> new BigDecimal(d).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue())
            .orElse(0.00);

        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(revenue,chainRevenue);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");


    }

    // TODO add trend

}
