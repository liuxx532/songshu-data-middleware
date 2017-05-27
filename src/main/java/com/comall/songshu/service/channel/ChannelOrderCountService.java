package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.ChannelOrderCountRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * 渠道订单量
 * Created by liugaoyu on 2017/4/20.
 */
@Service
public class ChannelOrderCountService {

    @Autowired
    private ChannelOrderCountRepository channelOrderCountRepository;

    public Object[] getOrder(){
        return channelOrderCountRepository.findAll().toArray();
    }

    public String  getChannelOrderCount(String target,String platformName,String channelName,Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {

        int platform = TransferUtil.getPlatform(platformName);
        boolean isChannelNameEmpty = channelName == null || channelName.equals("");

        Double orderCountResult;
        Double chainOrderCountResult;

        if (platform<0){
            if(isChannelNameEmpty){
                orderCountResult = channelOrderCountRepository.getChannelOrderCountWithAllPlatformAllChannel(beginTime,endTime);
                chainOrderCountResult = channelOrderCountRepository.getChannelOrderCountWithAllPlatformAllChannel(chainBeginTime,chainEndTime);
            }else{
                orderCountResult = channelOrderCountRepository.getChannelOrderCountWithAllPlatformSingleChannel(beginTime,endTime,channelName);
                chainOrderCountResult = channelOrderCountRepository.getChannelOrderCountWithAllPlatformSingleChannel(chainBeginTime,chainEndTime,channelName);
            }
        }else {
            if(isChannelNameEmpty){
                orderCountResult = channelOrderCountRepository.getChannelOrderCountWithSinglePlatformAllChannel(platform,beginTime,endTime);
                chainOrderCountResult = channelOrderCountRepository.getChannelOrderCountWithSinglePlatformAllChannel(platform,chainBeginTime,chainEndTime);
            }else{
                orderCountResult = channelOrderCountRepository.getChannelOrderCountWithSinglePlatformSingleChannel(platform,beginTime,endTime,channelName);
                chainOrderCountResult = channelOrderCountRepository.getChannelOrderCountWithSinglePlatformSingleChannel(platform,chainBeginTime,chainEndTime,channelName);
            }
        }

        Double orderCount = Optional.ofNullable(orderCountResult).orElse(0.00);
        Double chainOrderCount = Optional.ofNullable(chainOrderCountResult).orElse(0.00);

        //TopStat
        TopStat result = AssembleUtil.assemblerTopStat(orderCount,chainOrderCount);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");


    }



}
