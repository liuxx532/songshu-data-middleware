package com.comall.songshu.service.member;

import com.comall.songshu.repository.member.MemberShareRepository;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by huanghaizhou on 2017/5/4.
 */
@Service
public class MemberShareService {

    @Autowired
    private MemberShareRepository memberShareRepository;

    public String getMemberShareDetailByName(String target, String platformName, Timestamp beginTime, Timestamp endTime){

        int platform = TransferUtil.getPlatform(platformName);

        Integer shareProductCount = 0;
        Integer shareOrderCount = 0;
        Integer shareSpecialPageCount = 0;
        Integer shareRegisterCount = 0;
        if (platform<0) {
            //TODO 填入对应的事件字段名
            shareProductCount = memberShareRepository.getMemberShareWithAllPlatformByName("事件名字段，需要改", beginTime, endTime);
            shareOrderCount = memberShareRepository.getMemberShareWithAllPlatformByName("事件名字段，需要改", beginTime, endTime);
            shareSpecialPageCount = memberShareRepository.getMemberShareWithAllPlatformByName("事件名字段，需要改", beginTime, endTime);
            shareRegisterCount = memberShareRepository.getMemberShareWithAllPlatformByName("事件名字段，需要改", beginTime, endTime);

        }else{
            //TODO 填入对应的事件字段名
            shareProductCount = memberShareRepository.getMemberShareWithSinglePlatformByName("事件名字段，需要改", beginTime, endTime,platform);
            shareOrderCount = memberShareRepository.getMemberShareWithSinglePlatformByName("事件名字段，需要改", beginTime, endTime,platform);
            shareSpecialPageCount = memberShareRepository.getMemberShareWithSinglePlatformByName("事件名字段，需要改", beginTime, endTime,platform);
            shareRegisterCount = memberShareRepository.getMemberShareWithSinglePlatformByName("事件名字段，需要改", beginTime, endTime,platform);
        }

        //TODO 组装数据
        return null;
    }
    public String getMemberShareTrendByName(String target, String platformName, Timestamp beginTime, Timestamp endTime,int aggCount){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interval = ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime,aggCount);

        Integer shareProductCount = 0;
        Integer shareOrderCount = 0;
        Integer shareSpecialPageCount = 0;
        Integer shareRegisterCount = 0;
        if (platform<0) {
            //TODO 填入对应的事件字段名
            shareProductCount = memberShareRepository.getMemberShareTrendWithAllPlatformByName("事件名字段，需要改", beginTime, endTime,interval);
            shareOrderCount = memberShareRepository.getMemberShareTrendWithAllPlatformByName("事件名字段，需要改", beginTime, endTime,interval);
            shareSpecialPageCount = memberShareRepository.getMemberShareTrendWithAllPlatformByName("事件名字段，需要改", beginTime, endTime,interval);
            shareRegisterCount = memberShareRepository.getMemberShareTrendWithAllPlatformByName("事件名字段，需要改", beginTime, endTime,interval);

        }else{
            //TODO 填入对应的事件字段名
            shareProductCount = memberShareRepository.getMemberShareTrendWithSinglePlatformByName("事件名字段，需要改", beginTime, endTime,platform,interval);
            shareOrderCount = memberShareRepository.getMemberShareTrendWithSinglePlatformByName("事件名字段，需要改", beginTime, endTime,platform,interval);
            shareSpecialPageCount = memberShareRepository.getMemberShareTrendWithSinglePlatformByName("事件名字段，需要改", beginTime, endTime,platform,interval);
            shareRegisterCount = memberShareRepository.getMemberShareTrendWithSinglePlatformByName("事件名字段，需要改", beginTime, endTime,platform,interval);
        }

        //TODO 组装数据
        return null;
    }
}
