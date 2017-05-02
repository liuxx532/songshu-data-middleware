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
 * 订单量
 * Created by liugaoyu on 2017/4/20.
 */
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

        if (platform<0){
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


    public String getOrderCountTrend(String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime, int aggCount){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interValue= ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime,aggCount);

        //当前
        List<Object[] > currentOrderCountResult;
        //环比
        List<Object[] > chainOrderCountResult;


        //所有平台
        if (platform < 0){ //所有平台
            currentOrderCountResult = orderCountRepository.getOrderCountTrendWithAllPlatform(beginTime, endTime, interValue);
            chainOrderCountResult = orderCountRepository.getOrderCountTrendWithAllPlatform(chainBeginTime, chainEndTime, interValue);
        }else {//单个平台
            currentOrderCountResult = orderCountRepository.getOrderCounTrendtWithSinglePlatform(platform, beginTime, endTime, interValue);
            chainOrderCountResult = orderCountRepository.getOrderCounTrendtWithSinglePlatform(platform, chainBeginTime, chainEndTime, interValue);
        }

        List<Object[]> currentOrderCount = Optional.ofNullable(currentOrderCountResult).orElse(null);
        List<Object[]> chainOrderCount = Optional.ofNullable(chainOrderCountResult).orElse(null);

        return JsonStringBuilder.buildTrendJsonString(currentOrderCount, chainOrderCount);
    }

}
