package com.comall.songshu.service;

import com.comall.songshu.repository.NotFirstOrderedConsumerCountRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by wdc on 2017/4/24.
 */
@Service
public class NotFirstOrderedConsumerCountService {

    @Autowired
    private NotFirstOrderedConsumerCountRepository notFirstOrderedConsumerCountRepository;

    public  Double getCountWithAllPlatform(Timestamp startTime, Timestamp endTime) {
        return notFirstOrderedConsumerCountRepository.getNotFirstOrderedConsumerCountWithAllPlatform(startTime, endTime);
    }

    public Double getCountWithSinglePlatform(Integer platform, Timestamp startTime, Timestamp endTime) {
        return notFirstOrderedConsumerCountRepository.getNotFirstOrderedConsumerCountWithSinglePlatform(platform, startTime, endTime);
    }
}
