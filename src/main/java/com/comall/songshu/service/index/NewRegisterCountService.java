package com.comall.songshu.service.index;

import com.comall.songshu.repository.index.NewRegisterCountRepository;
import com.comall.songshu.web.rest.AuthorResource;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 新注册用户数
 * Created by wdc on 2017/4/24.
 */
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
        List<Object[]> list = new ArrayList();


        if (platform < 0){ //所有
            Integer androidResult = Optional.ofNullable(getNewRegisterCountWithSinglePlatform(1,startTime,endTime)).orElse(0);
            Integer iosResult = Optional.ofNullable(getNewRegisterCountWithSinglePlatform(2,startTime,endTime)).orElse(0);
            Integer weChatResult = Optional.ofNullable(getNewRegisterCountWithSinglePlatform(3,startTime,endTime)).orElse(0);
            Integer wapResult = Optional.ofNullable(getNewRegisterCountWithSinglePlatform(5,startTime,endTime)).orElse(0);
            Integer othersResult = Optional.ofNullable(getNewRegisterCountWithOthersPlatform(startTime,endTime)).orElse(0);
            list.add(new Object[]{TransferUtil.getPlatFormName(TransferUtil.CHANNEL_ANDROID),androidResult});
            list.add(new Object[]{TransferUtil.getPlatFormName(TransferUtil.CHANNEL_IOS),iosResult});
            list.add(new Object[]{TransferUtil.getPlatFormName(TransferUtil.CHANNEL_WECHAT),weChatResult});
            list.add(new Object[]{TransferUtil.getPlatFormName(TransferUtil.CHANNEL_WAP),wapResult});
            list.add(new Object[]{TransferUtil.getPlatFormName(TransferUtil.CHANNEL_OTHERS),othersResult});
        }else {
            Integer platformResult = Optional.ofNullable(getNewRegisterCountWithSinglePlatform(platform,startTime,endTime)).orElse(0);
            log.info("platformResult",platformResult);
            list.add(new Object[]{TransferUtil.getPlatFormName(platform),platformResult});
        }
        log.info("list",list);
        return JsonStringBuilder.buildPieJsonString(list);
    }

}
