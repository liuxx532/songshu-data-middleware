package com.comall.songshu.service;

import com.comall.songshu.repository.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by liugaoyu on 2017/4/20.
 */

@Service
public class RefundService {
    @Autowired
    private RefundRepository refundRepository;


    public Object[] getRefund(){
        return  refundRepository.findAll().toArray();
    }


    public Double getRefundWithAllPlatform(Timestamp beginTime, Timestamp endTime) {
        return refundRepository.getRefundWithAllPlatform(beginTime, endTime);
    }

    public Double getRefundWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime) {
        return refundRepository.getRefundWithSinglePlatform(platform, beginTime, endTime);
    }
}
