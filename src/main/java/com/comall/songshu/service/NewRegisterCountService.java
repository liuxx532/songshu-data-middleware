package com.comall.songshu.service;

import com.comall.songshu.repository.NewRegisterCountRepository;
import com.comall.songshu.web.rest.AuthorResource;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by wdc on 2017/4/24.
 */
// 新注册用户数
@Service
public class NewRegisterCountService {

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    @Autowired
    NewRegisterCountRepository newRegisterCountRepository;

    public Integer getNewRegisterCountWithSinglePlatform(Integer platform, Timestamp startTime, Timestamp endTime) {
        return newRegisterCountRepository.getNewRegisterCountWithSinglePlatform(platform, startTime, endTime);
    }

    public Integer getNewRegisterCountWithOthersPlatform(Timestamp startTime, Timestamp endTime) {
        return newRegisterCountRepository.getNewRegisterCountWithOthersPlatform(startTime, endTime);
    }


    public String getNewRegisterCount( String platformName, Timestamp startTime, Timestamp endTime) {

        Integer platform = TransferUtil.getPlatform(platformName);
        List<Integer> list = new ArrayList();
        Integer platformResult = null;

        if (platform < 0){ //所有
            Integer android = getNewRegisterCountWithSinglePlatform(1,startTime,endTime);
            Optional.ofNullable(android).orElse(0);
            Integer ios = getNewRegisterCountWithSinglePlatform(2,startTime,endTime);
            Optional.ofNullable(ios).orElse(0);
            Integer wechat = getNewRegisterCountWithSinglePlatform(3,startTime,endTime);
            Optional.ofNullable(wechat).orElse(0);
            Integer wap = getNewRegisterCountWithSinglePlatform(5,startTime,endTime);
            Optional.ofNullable(wap).orElse(0);
            Integer others = getNewRegisterCountWithOthersPlatform(startTime,endTime);
            Optional.ofNullable(others).orElse(0);
            list.add(android);
            list.add(ios);
            list.add(wechat);
            list.add(wap);
            list.add(others);
        }else {
            platformResult=getNewRegisterCountWithSinglePlatform(platform,startTime,endTime);
            Optional.ofNullable(platform).orElse(0);

            log.info("platformResult",platformResult);
            list.add(platformResult);
        }
        log.info("list",list);
        return JsonStringBuilder.buildPieJsonString(platformName,list);
    }
}
