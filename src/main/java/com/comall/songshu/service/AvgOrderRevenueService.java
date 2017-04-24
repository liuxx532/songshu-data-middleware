package com.comall.songshu.service;

import com.comall.songshu.repository.AvgOrderRevenueRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by wdc on 2017/4/19.
 */
@Service
public class AvgOrderRevenueService {

    @Autowired
    private AvgOrderRevenueRepository avgOrderRevenueRepository;

//    public Object[] getAvgOrderRevenue(){
//        return  avgOrderRevenueRepository.findAll().toArray();
//    }

    public String getAvgOrderRevenue(String platformName,String startTime,String toTime){

        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        int platform= TransferUtil.getPlatform(platformName);
        Timestamp beginTime= ServiceUtil.getInstance().parseTimestamp(startTime);
        Timestamp endTime=ServiceUtil.getInstance().parseTimestamp(toTime);

        Double avgOrderRevenueResult=null;

        //环比时间
        String[] chainCreateTime=ServiceUtil.getInstance().getChainIndexDateTime(startTime,toTime);
        Timestamp chainBeginTime=Timestamp.valueOf(DateTime.parse(chainCreateTime[0]).toString(dateTimeFormat));
        Timestamp chainEndTime=Timestamp.valueOf(DateTime.parse(chainCreateTime[1]).toString(dateTimeFormat));
        Double chainAvgOrderRevenueResult=null;
        if (platform<0){//全部
            avgOrderRevenueResult=avgOrderRevenueRepository.getAvgRevenueWithAllPlatform(beginTime,endTime);
            chainAvgOrderRevenueResult=avgOrderRevenueRepository.getAvgRevenueWithAllPlatform(chainBeginTime,chainEndTime);

        }else {
            avgOrderRevenueResult=avgOrderRevenueRepository.getAvgRevenueWithSinglePlatform(platform,beginTime,endTime);
            chainAvgOrderRevenueResult=avgOrderRevenueRepository.getAvgRevenueWithSinglePlatform(platform,beginTime,endTime);
        }

        Double avgOrderRevenue=0.0;
        Double chainAvgOrderRevenue=0.0;

        if (null!= avgOrderRevenueResult){
            avgOrderRevenue=avgOrderRevenueResult;

        }
        if (null !=chainAvgOrderRevenueResult){
            chainAvgOrderRevenue=chainAvgOrderRevenueResult;
        }

        //TopStat

        TopStat result=null;
        if (avgOrderRevenue <=0|| chainAvgOrderRevenue <=0){
            result =new TopStat(avgOrderRevenue,0.0);

        }else {
            //比例
            double percent =Math.abs(avgOrderRevenue-chainAvgOrderRevenue)/chainAvgOrderRevenue;
            int flag= ServiceUtil.getInstance().getChainIndexFlag(avgOrderRevenue,chainAvgOrderRevenue);
        }


        return JsonStringBuilder.buildTargetJsonString("avgOrderRevenue",result,"");
    }






//    public Double getAvgRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime) {
//        return avgOrderRevenueRepository.getAvgRevenueWithAllPlatform(beginTime, endTime);
//    }
//
//
//    public Double getAvgRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime) {
//        return avgOrderRevenueRepository.getAvgRevenueWithSinglePlatform(platform, beginTime, endTime);
//    }



}
