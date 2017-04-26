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

    // TODO add trend


}
