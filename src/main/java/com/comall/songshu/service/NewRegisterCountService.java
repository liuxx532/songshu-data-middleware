package com.comall.songshu.service;

import com.comall.songshu.repository.NewRegisterCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wdc on 2017/4/24.
 */
// 新注册用户数
@Service
public class NewRegisterCountService {

    @Autowired
    NewRegisterCountRepository newRegisterCountRepository;

    public Double getNewRegisterCountWithSinglePlatform(Integer platform, Timestamp startTime, Timestamp endTime) {
        return newRegisterCountRepository.getNewRegisterCountWithSinglePlatform(platform, startTime, endTime);
    }

    public Double getNewRegisterCountWithOthersPlatform(Timestamp startTime, Timestamp endTime) {
        return newRegisterCountRepository.getNewRegisterCountWithOthersPlatform(startTime, endTime);
    }


    // 返回的值是 anroid, ios, others 占百分比
    public List<Double> getNewRegisterCount(Timestamp startTime, Timestamp endTime) {
        // 渠道取值
        // 1. android, 2. ios, 3. wechat, 4. app, 5. wap, 0. others (不能确定)


        Double android = getNewRegisterCountWithSinglePlatform(1, startTime, endTime);
        Double ios = getNewRegisterCountWithSinglePlatform(2, startTime, endTime);
        Double others = getNewRegisterCountWithOthersPlatform(startTime, endTime);

        Double sum = android + ios + others;
        if (sum <= 0) {
            return Arrays.asList(0.333, 0.333, 0.334);
        }

        // 返回计算值
        return Arrays.asList(android / sum, ios / sum, others / sum);
    }
}
