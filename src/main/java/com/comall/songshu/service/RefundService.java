package com.comall.songshu.service;

import com.comall.songshu.repository.RefundRepository;
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

@Service
public class RefundService {
    @Autowired
    private RefundRepository refundRepository;

    public Object[] findRefund(){
        return  refundRepository.findAll().toArray();
    }


    public String  getRefund(String target,String platformName, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {

        int platform = TransferUtil.getPlatform(platformName);
        Double refundResult;
        Double chainRefundResult;

        if (platform<0){//
            refundResult = refundRepository.getRefundWithAllPlatform(beginTime,endTime);
            chainRefundResult = refundRepository.getRefundWithAllPlatform(chainBeginTime,chainEndTime);
        }else {
            refundResult = refundRepository.getRefundWithSinglePlatform(platform,beginTime,endTime);
            chainRefundResult = refundRepository.getRefundWithSinglePlatform(platform,chainBeginTime,chainEndTime);
        }

        Double refund = Optional.ofNullable(refundResult).orElse(0.00);
        Double chainRefund = Optional.ofNullable(chainRefundResult).orElse(0.00);

        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(refund,chainRefund);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");


    }

    // TODO add trend
    public String getRefundTrend(String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interValue= ServiceUtil.getInstance().getAggTimeValue(beginTime, endTime);


        //所有平台
        List<Object[] > currentAllPlatformResult = null;
        List<Object[] > chainAllPlatformResult = null;

        //单个平台
        List<Object[] > currentSinglePlatformResult = null;
        List<Object[] > chainSinglePlatformResult = null;


        //所有平台
        if (platform < 0){ //所有平台
            //当前
            currentAllPlatformResult = refundRepository.getRefundTrendWithAllPlatform(beginTime, endTime, interValue);
            //环比
            chainAllPlatformResult = refundRepository.getRefundTrendWithAllPlatform(chainBeginTime, chainEndTime, interValue);

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
            currentSinglePlatformResult = refundRepository.getRefundTrendWithSinglePlatform(beginTime, endTime, interValue, platform);
            chainSinglePlatformResult = refundRepository.getRefundTrendWithSinglePlatform(chainBeginTime, chainEndTime, interValue, platform);

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
