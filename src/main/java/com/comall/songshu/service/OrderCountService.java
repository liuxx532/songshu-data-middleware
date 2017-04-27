package com.comall.songshu.service;

import com.comall.songshu.repository.OrderCountRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Created by liugaoyu on 2017/4/20.
 */
// 订单量
@Service
public class OrderCountService {
    @Autowired
    private OrderCountRepository orderCountRepository;

    public Object[] getOrder(){
        return orderCountRepository.findAll().toArray();
    }

    public String  getOrderCount(String target,String platformName, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {

        int platform = TransferUtil.getPlatform(platformName);
        Double orderCountResult;
        Double chainOrderCountResult;
        if (platform<0){//
            orderCountResult = orderCountRepository.getOrderCountWithAllPlatform(beginTime,endTime);
            chainOrderCountResult = orderCountRepository.getOrderCountWithAllPlatform(chainBeginTime,chainEndTime);
        }else {
            orderCountResult = orderCountRepository.getOrderCountWithSinglePlatform(platform,beginTime,endTime);
            chainOrderCountResult = orderCountRepository.getOrderCountWithSinglePlatform(platform,chainBeginTime,chainEndTime);
        }

        Double orderCount = Optional.ofNullable(orderCountResult).orElse(0.00);
        Double chainOrderCount = Optional.ofNullable(chainOrderCountResult).orElse(0.00);

        //TopStat
        TopStat result = AssembleUtil.assemblerTopStat(orderCount,chainOrderCount);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");


    }

    // TODO add trend

    public String getOrderCountTrend(String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interValue= ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime);


        //所有平台
        List<Object[] > currentAllPlatformResult = null;
        List<Object[] > chainAllPlatformResult = null;

        //单个平台
        List<Object[] > currentSinglePlatformResult = null;
        List<Object[] > chainSinglePlatformResult = null;


        //所有平台
        if (platform < 0){ //所有平台
            //当前
            currentAllPlatformResult = orderCountRepository.getOrderCountTrendWithAllPlatform(beginTime, endTime, interValue);
            //环比
            chainAllPlatformResult = orderCountRepository.getOrderCountTrendWithAllPlatform(chainBeginTime, chainEndTime, interValue);

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
            currentSinglePlatformResult = orderCountRepository.getOrderCounTrendtWithSinglePlatform(platform, beginTime, endTime, interValue);
            chainSinglePlatformResult = orderCountRepository.getOrderCounTrendtWithSinglePlatform(platform, chainBeginTime, chainEndTime, interValue);

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
