package com.comall.songshu.service;

import com.comall.songshu.repository.FirstOrderedConsumerCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by wdc on 2017/4/24.
 */
// 首单用户数
@Service
public class FirstOrderedConsumerCountService {
    @Autowired
    FirstOrderedConsumerCountRepository firstOrderedConsumerCountRepository;

    public Double getFirstOrderedConsumerCountWithAllPlatform(Timestamp startTime, Timestamp endTime) {
        return firstOrderedConsumerCountRepository.getFirstOrderedConsumerCountWithAllPlatform(startTime, endTime);
    }

    public Double getFirstOrderedConsumerCountWithSinglePlatform(Integer platform, Timestamp startTime, Timestamp endTime) {
        return firstOrderedConsumerCountRepository.getFirstOrderedConsumerCountWithSinglePlatform(platform, startTime, endTime);
    }
}
