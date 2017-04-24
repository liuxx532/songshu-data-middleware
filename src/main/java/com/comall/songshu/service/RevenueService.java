package com.comall.songshu.service;

import com.comall.songshu.repository.RevenueRepository;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
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

    public String  getRevenue(String platformName, String startTime,String toTime) {
//
//        int platform= TransferUtil.getPlatform(platformName);
//
//       Timestamp beginTime= ServiceUtil.getInstance().parseTimestamp(startTime);
//       Timestamp endTime=ServiceUtil.getInstance().parseTimestamp(toTime);
//
//        Double revenue=10.0;
//        revenue=revenueRepository.getRevenueRe(platform,beginTime,endTime);
//
//         String[] chainDatetime= ServiceUtil.getInstance().getChainIndexDateTime(startTime,toTime);
//
//        Timestamp chainBeginTime = ServiceUtil.getInstance().parseTimestamp(chainDatetime[0]);
//        Timestamp chainEndTime = ServiceUtil.getInstance().parseTimestamp(chainDatetime[1]);
//
//        Double chainRevenue=11.0;
//
//        chainRevenue=revenueRepository.getRevenueRe(platform,chainBeginTime,chainEndTime);
//
//        if (null == revenue){
//            revenue=0.0;
//        }
//        if (null == chainRevenue){
//            chainRevenue=0.0;
//        }
//
//        if (platform<0){
//                //所有
//
//        }






        return null;
    }



    public Double getRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime) {
        return revenueRepository.getRevenueWithAllPlatform(beginTime, endTime);
    }


    public Double getRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime) {
        return revenueRepository.getRevenueWithSinglePlatform(platform, beginTime, endTime);
    }

    // TODO add trend
}
