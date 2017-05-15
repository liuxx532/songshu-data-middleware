package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.ChannelConsumerCountRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * 渠道消费用户数
 * Created by wdc on 2017/4/19.
 */
@Service
public class ChannelConsumerCountService {

    @Autowired
    private ChannelConsumerCountRepository channelConsumerCountRepository;


    public String getChannelConsumerRevenue(String target,String platformName,String channelName, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {


        int platform = TransferUtil.getPlatform(platformName);
        boolean isChannelNameEmpty = channelName == null || channelName == "";
        Double channelConsumerResult;
        Double chainChannelConsumerResult;

        if (platform<0){//全部
            if(isChannelNameEmpty){
                channelConsumerResult = channelConsumerCountRepository.getChannelConsumerCountWithAllPlatformAllChannel(beginTime,endTime);
                chainChannelConsumerResult = channelConsumerCountRepository.getChannelConsumerCountWithAllPlatformAllChannel(chainBeginTime,chainEndTime);
            }else{
                channelConsumerResult = channelConsumerCountRepository.getChannelConsumerCountWithAllPlatformSingleChannel(beginTime,endTime,channelName);
                chainChannelConsumerResult = channelConsumerCountRepository.getChannelConsumerCountWithAllPlatformSingleChannel(chainBeginTime,chainEndTime,channelName);
            }

        }else {
            if(isChannelNameEmpty){
                channelConsumerResult = channelConsumerCountRepository.getChannelConsumerCountSinglePlatformAllChannel(beginTime,endTime,platform);
                chainChannelConsumerResult = channelConsumerCountRepository.getChannelConsumerCountSinglePlatformAllChannel(beginTime,endTime,platform);
            }else{
                channelConsumerResult = channelConsumerCountRepository.getChannelConsumerCountSinglePlatformSingleChannel(beginTime,endTime,platform,channelName);
                chainChannelConsumerResult = channelConsumerCountRepository.getChannelConsumerCountSinglePlatformSingleChannel(chainBeginTime,chainEndTime,platform,channelName);
            }
        }

        Double channelConsumer = Optional.ofNullable(channelConsumerResult).orElse(0.00);
        Double chainChannelConsumer = Optional.ofNullable(chainChannelConsumerResult).orElse(0.00);
        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(channelConsumer,chainChannelConsumer);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");
    }



}
