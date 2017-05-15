package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.ChannelRevenueRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * 渠道销售额
 * Created by huanghaizhou on 2017/5/8.
 */
@Service
public class ChannelRevenueService {

    @Autowired
    private ChannelRevenueRepository channelRevenueRepository;

    public String getChannelRevenue(String target, String platformName,String channelName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime) {

        int platform = TransferUtil.getPlatform(platformName);
        Double revenueResult = null;
        Double chainRevenueResult = null;

        boolean isChannelNameEmpty = channelName == null || channelName == "";

        if (platform<0){
            if(isChannelNameEmpty){
                revenueResult = channelRevenueRepository.getChannelRevenueWithAllPlatformAllChannel(beginTime,endTime);
                chainRevenueResult = channelRevenueRepository.getChannelRevenueWithAllPlatformAllChannel(chainBeginTime,chainEndTime);
            }else{
                revenueResult = channelRevenueRepository.getChannelRevenueWithAllPlatformSingleChannel(beginTime,endTime,channelName);
                chainRevenueResult = channelRevenueRepository.getChannelRevenueWithAllPlatformSingleChannel(chainBeginTime,chainEndTime,channelName);
            }
        }else {
            if(isChannelNameEmpty){
                revenueResult = channelRevenueRepository.getChannelRevenueWithSinglePlatformAllChannel(platform,beginTime,endTime);
                chainRevenueResult = channelRevenueRepository.getChannelRevenueWithSinglePlatformAllChannel(platform,chainBeginTime,chainEndTime);
            }else{
                revenueResult = channelRevenueRepository.getChannelRevenueWithSinglePlatformSingleChannel(platform,beginTime,endTime,channelName);
                chainRevenueResult = channelRevenueRepository.getChannelRevenueWithSinglePlatformSingleChannel(platform,chainBeginTime,chainEndTime,channelName);
            }
        }

        Double revenue = Optional.ofNullable(revenueResult).orElse(0.00);
        Double chainRevenue = Optional.ofNullable(chainRevenueResult).orElse(0.00);

        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(revenue,chainRevenue);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");
    }


}
