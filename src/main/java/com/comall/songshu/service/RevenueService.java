package com.comall.songshu.service;

import com.comall.songshu.repository.RevenueRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by lgx on 17/4/18.
 */
@Component
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    public String  getRevenue(String target,String platformName, String startTime,String toTime) {

        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        int platform = TransferUtil.getPlatform(platformName);
        Timestamp beginTime = ServiceUtil.getInstance().parseTimestamp(startTime);
        Timestamp endTime = ServiceUtil.getInstance().parseTimestamp(toTime);

        Double revenueResult = null;

        //环比时间
        String[] chainCreateTime = ServiceUtil.getInstance().getChainIndexDateTime(startTime,toTime);
        Timestamp chainBeginTime = Timestamp.valueOf(DateTime.parse(chainCreateTime[0]).toString(dateTimeFormat));
        Timestamp chainEndTime = Timestamp.valueOf(DateTime.parse(chainCreateTime[1]).toString(dateTimeFormat));
        Double chainRevenueResult = null;
        if (platform<0){//
            revenueResult = revenueRepository.getRevenueWithAllPlatform(beginTime,endTime);
            chainRevenueResult = revenueRepository.getRevenueWithAllPlatform(chainBeginTime,chainEndTime);
        }else {
            revenueResult = revenueRepository.getRevenueWithSinglePlatform(platform,beginTime,endTime);
            chainRevenueResult = revenueRepository.getRevenueWithSinglePlatform(platform,chainBeginTime,chainEndTime);
        }

        Double revenue = 0.0;
        Double chainRevenue = 0.0;

        if (null!= revenueResult){
            revenue = revenueResult;

        }
        if (null !=chainRevenueResult){
            chainRevenue = chainRevenueResult;
        }

        //TopStat

        TopStat result=null;
        if (revenue <=0|| chainRevenue <=0){
            result =new TopStat(revenue,0.0);

        }else {
            //比例
            double percent =Math.abs(revenue-chainRevenueResult)/chainRevenueResult;
            int flag= ServiceUtil.getInstance().getChainIndexFlag(revenue,chainRevenueResult);
            result = new TopStat(revenue, percent, flag);
        }


        return JsonStringBuilder.buildTargetJsonString(target,result,"");


    }



    public Double getRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime) {
        return revenueRepository.getRevenueWithAllPlatform(beginTime, endTime);
    }


    public Double getRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime) {
        return revenueRepository.getRevenueWithSinglePlatform(platform, beginTime, endTime);
    }

    // TODO add trend
}
