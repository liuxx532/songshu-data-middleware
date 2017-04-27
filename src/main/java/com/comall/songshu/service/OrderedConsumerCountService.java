package com.comall.songshu.service;

import com.comall.songshu.repository.FirstOrderedConsumerCountRepository;
import com.comall.songshu.repository.NotFirstOrderedConsumerCountRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by wdc on 2017/4/27.
 */

@Service
public class OrderedConsumerCountService {
    @Autowired
    FirstOrderedConsumerCountRepository firstOrderedConsumerCountRepository;

    @Autowired
    NotFirstOrderedConsumerCountRepository notFirstOrderedConsumerCountRepository;


    public String getOrderedConsumerCount(String platformName, Timestamp startTime, Timestamp endTime) {

        int platform = TransferUtil.getPlatform(platformName);

        Double order = null;
        Double notOrder = null;

        if (platform < 0) {
            // For all platform
            order = firstOrderedConsumerCountRepository.getFirstOrderedConsumerCountWithAllPlatform(startTime, endTime);
            notOrder = notFirstOrderedConsumerCountRepository.getNotFirstOrderedConsumerCountWithAllPlatform(startTime, endTime);
        } else {
            // For single platform
            order = firstOrderedConsumerCountRepository.getFirstOrderedConsumerCountWithSinglePlatform(platform, startTime, endTime);
            notOrder = notFirstOrderedConsumerCountRepository.getNotFirstOrderedConsumerCountWithSinglePlatform(platform, startTime, endTime);
        }

        Double total = Optional.ofNullable(order).orElse(0.0) + Optional.ofNullable(notOrder).orElse(0.0);

        // 计算百分比
        if (total <= 0.0) {
            order = 0.5;
            notOrder = 0.5;
        } else {
            order = order / total;
            notOrder = notOrder / total;
        }


        return JsonStringBuilder.buildOrderedConsumerCountJsonString(order, notOrder);
    }
}
