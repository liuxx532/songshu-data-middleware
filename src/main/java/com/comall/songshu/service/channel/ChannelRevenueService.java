package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.ChannelRevenueRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * 渠道销售额
 * Created by huanghaizhou on 2017/5/8.
 */
public class ChannelRevenueService {

    @Autowired
    private ChannelRevenueRepository channelRevenueRepository;

    public String  getRevenue(String target, String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime,String channelName) {

        int platform = TransferUtil.getPlatform(platformName);
        Double revenueResult;
        Double chainRevenueResult;
        if (platform<0){//
            revenueResult = channelRevenueRepository.getChannelRevenueWithAllPlatform(beginTime,endTime,channelName);
            chainRevenueResult = channelRevenueRepository.getChannelRevenueWithAllPlatform(chainBeginTime,chainEndTime,channelName);
        }else {
            revenueResult = channelRevenueRepository.getChannelRevenueWithSinglePlatform(platform,beginTime,endTime,channelName);
            chainRevenueResult = channelRevenueRepository.getChannelRevenueWithSinglePlatform(platform,chainBeginTime,chainEndTime,channelName);
        }

        Double revenue = Optional.ofNullable(revenueResult).orElse(0.00);
        Double chainRevenue = Optional.ofNullable(chainRevenueResult).orElse(0.00);

        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(revenue,chainRevenue);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");
    }


}
