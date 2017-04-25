package com.comall.songshu.service;

import com.comall.songshu.repository.RefundRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
}
