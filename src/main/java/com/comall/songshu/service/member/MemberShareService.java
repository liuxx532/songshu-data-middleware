package com.comall.songshu.service.member;

import com.comall.songshu.repository.member.MemberShareRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            shareProductCount = memberShareRepository.getMemberShareWithAllPlatformByName("对应事件名称", beginTime, endTime);
            shareOrderCount = memberShareRepository.getMemberShareWithAllPlatformByName("对应事件名称", beginTime, endTime);
            shareSpecialPageCount = memberShareRepository.getMemberShareWithAllPlatformByName("对应事件名称", beginTime, endTime);
            shareRegisterCount = memberShareRepository.getMemberShareWithAllPlatformByName("对应事件名称", beginTime, endTime);

        }else{
            //TODO 填入对应的事件字段名
            shareProductCount = memberShareRepository.getMemberShareWithSinglePlatformByName("对应事件名称", beginTime, endTime,platformName);
            shareOrderCount = memberShareRepository.getMemberShareWithSinglePlatformByName("对应事件名称", beginTime, endTime,platformName);
            shareSpecialPageCount = memberShareRepository.getMemberShareWithSinglePlatformByName("对应事件名称", beginTime, endTime,platformName);
            shareRegisterCount = memberShareRepository.getMemberShareWithSinglePlatformByName("对应事件名称", beginTime, endTime,platformName);
        }

        //TODO 组装数据
        return null;
    }
    public String getMemberShareTrendByName(String target, String platformName, Timestamp beginTime, Timestamp endTime,int aggCount){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interval = ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime,aggCount);

        List<Object[]> shareProductCount;
        List<Object[]> shareOrderCount;
        List<Object[]> shareSpecialPageCount;
        List<Object[]> shareRegisterCount;
        if (platform<0) {
            //TODO 填入对应的事件字段名
            shareProductCount = memberShareRepository.getMemberShareTrendWithAllPlatformByName("对应事件名称", beginTime, endTime,interval);
            shareOrderCount = memberShareRepository.getMemberShareTrendWithAllPlatformByName("对应事件名称", beginTime, endTime,interval);
            shareSpecialPageCount = memberShareRepository.getMemberShareTrendWithAllPlatformByName("对应事件名称", beginTime, endTime,interval);
            shareRegisterCount = memberShareRepository.getMemberShareTrendWithAllPlatformByName("对应事件名称", beginTime, endTime,interval);

        }else{
            //TODO 填入对应的事件字段名
            shareProductCount = memberShareRepository.getMemberShareTrendWithSinglePlatformByName("对应事件名称", beginTime, endTime,platformName,interval);
            shareOrderCount = memberShareRepository.getMemberShareTrendWithSinglePlatformByName("对应事件名称", beginTime, endTime,platformName,interval);
            shareSpecialPageCount = memberShareRepository.getMemberShareTrendWithSinglePlatformByName("对应事件名称", beginTime, endTime,platformName,interval);
            shareRegisterCount = memberShareRepository.getMemberShareTrendWithSinglePlatformByName("对应事件名称", beginTime, endTime,platformName,interval);
        }

        //TODO 组装数据
        List<Object[]> shareProductCountResult = Optional.ofNullable(shareProductCount).orElse(null);
        List<Object[]> shareOrderCountResult = Optional.ofNullable(shareOrderCount).orElse(null);
        List<Object[]> shareSpecialPageCountResult = Optional.ofNullable(shareSpecialPageCount).orElse(null);
        List<Object[]> shareRegisterCountResult = Optional.ofNullable(shareRegisterCount).orElse(null);

        List<String> targetNames = new ArrayList<>();
        targetNames.add("分享商品次数");
        targetNames.add("分享订单次数");
        targetNames.add("分享专题页次数");
        targetNames.add("分享注册次数");

        return JsonStringBuilder.buildTrendJsonString(targetNames,shareProductCountResult,shareOrderCountResult,shareSpecialPageCountResult,shareRegisterCountResult);
    }
}
