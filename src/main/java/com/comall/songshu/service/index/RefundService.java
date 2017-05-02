package com.comall.songshu.service.index;

import com.comall.songshu.repository.index.RefundRepository;
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
 * 退款金额
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

    public String getRefundTrend(String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime, int aggCount){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interValue= ServiceUtil.getInstance().getAggTimeValue(beginTime, endTime,aggCount);


        //当前
        List<Object[] > currentRefundResult;
        //环比
        List<Object[] > chainRefundResult;

        if (platform < 0){ //所有平台
            currentRefundResult = refundRepository.getRefundTrendWithAllPlatform(beginTime, endTime, interValue);
            chainRefundResult = refundRepository.getRefundTrendWithAllPlatform(chainBeginTime, chainEndTime, interValue);
        }else {//单个平台
            currentRefundResult = refundRepository.getRefundTrendWithSinglePlatform(beginTime, endTime, interValue, platform);
            chainRefundResult = refundRepository.getRefundTrendWithSinglePlatform(chainBeginTime, chainEndTime, interValue, platform);
        }

        List<Object[]> currentRefund = Optional.ofNullable(currentRefundResult).orElse(null);
        List<Object[]> chainRefund = Optional.ofNullable(chainRefundResult).orElse(null);

        return JsonStringBuilder.buildTrendJsonString(currentRefund,chainRefund);
    }
}
