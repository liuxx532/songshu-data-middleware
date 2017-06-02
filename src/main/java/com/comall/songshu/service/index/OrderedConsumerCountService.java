package com.comall.songshu.service.index;

import com.comall.songshu.repository.index.FirstOrderedConsumerCountRepository;
import com.comall.songshu.repository.index.NotFirstOrderedConsumerCountRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 下单用户数
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
        List<Object[]> list = new LinkedList<>();
        Double order;
        Double notOrder;

        if (platform < 0) {
            // For all platform
            order = firstOrderedConsumerCountRepository.getFirstOrderedConsumerCountWithAllPlatform(startTime, endTime);
            notOrder = notFirstOrderedConsumerCountRepository.getNotFirstOrderedConsumerCountWithAllPlatform(startTime, endTime);
        } else {
            // For single platform
            order = firstOrderedConsumerCountRepository.getFirstOrderedConsumerCountWithSinglePlatform(platform, startTime, endTime);
            notOrder = notFirstOrderedConsumerCountRepository.getNotFirstOrderedConsumerCountWithSinglePlatform(platform, startTime, endTime);
        }

        order = Optional.ofNullable(order).orElse(0.0);
        notOrder = Optional.ofNullable(notOrder).orElse(0.0);
        list.add(new Object[]{"首单",order});
        list.add(new Object[]{"非首单",notOrder});

        return JsonStringBuilder.buildPieJsonString(list, "是否首次消费占比");
    }
}
