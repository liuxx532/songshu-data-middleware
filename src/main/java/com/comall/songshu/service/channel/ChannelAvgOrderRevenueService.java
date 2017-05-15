package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.ChannelAvgOrderRevenueRepository;
import com.comall.songshu.repository.index.AvgOrderRevenueRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * 渠道客单价
 * Created by wdc on 2017/4/19.
 */
@Service
public class ChannelAvgOrderRevenueService {

    @Autowired
    private ChannelAvgOrderRevenueRepository channelAvgOrderRevenueRepository;


    public String getChannelAvgOrderRevenue(String target,String platformName,String channelName, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {


        int platform = TransferUtil.getPlatform(platformName);
        boolean isChannelNameEmpty = channelName == null || channelName == "";
        Double avgOrderRevenueResult = null;
        Double chainAvgOrderRevenueResult = null;

        if (platform<0){//全部
            if(isChannelNameEmpty){
                avgOrderRevenueResult = channelAvgOrderRevenueRepository.getChannelAvgRevenueWithAllPlatformAllChannel(beginTime,endTime);
                chainAvgOrderRevenueResult = channelAvgOrderRevenueRepository.getChannelAvgRevenueWithAllPlatformAllChannel(chainBeginTime,chainEndTime);
            }else{
                avgOrderRevenueResult = channelAvgOrderRevenueRepository.getChannelAvgRevenueWithAllPlatformSingleChannel(beginTime,endTime,channelName);
                chainAvgOrderRevenueResult = channelAvgOrderRevenueRepository.getChannelAvgRevenueWithAllPlatformSingleChannel(chainBeginTime,chainEndTime,channelName);
            }

        }else {
            if(isChannelNameEmpty){
                avgOrderRevenueResult = channelAvgOrderRevenueRepository.getChannelAvgRevenueWithSinglePlatformAllChannel(platform,beginTime,endTime);
                chainAvgOrderRevenueResult = channelAvgOrderRevenueRepository.getChannelAvgRevenueWithSinglePlatformAllChannel(platform,chainBeginTime,chainEndTime);
            }else{
                avgOrderRevenueResult = channelAvgOrderRevenueRepository.getChannelAvgRevenueWithSinglePlatformSingleChannel(platform,beginTime,endTime,channelName);
                chainAvgOrderRevenueResult = channelAvgOrderRevenueRepository.getChannelAvgRevenueWithSinglePlatformSingleChannel(platform,chainBeginTime,chainEndTime,channelName);
            }
        }

        Double avgOrderRevenue = Optional.ofNullable(avgOrderRevenueResult).orElse(0.00);
        Double chainAvgOrderRevenue = Optional.ofNullable(chainAvgOrderRevenueResult).orElse(0.00);
        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(avgOrderRevenue,chainAvgOrderRevenue);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");
    }



}
