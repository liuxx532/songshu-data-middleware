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

    public String getChannelRevenue(String target, String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime) {

        int platform = TransferUtil.getPlatform(platformName);
        Double revenueResult;
        Double chainRevenueResult;
        if (platform<0){//
            revenueResult = channelRevenueRepository.getChannelRevenueWithAllPlatform(beginTime,endTime);
            chainRevenueResult = channelRevenueRepository.getChannelRevenueWithAllPlatform(chainBeginTime,chainEndTime);
        }else {
            revenueResult = channelRevenueRepository.getChannelRevenueWithSinglePlatform(platform,beginTime,endTime);
            chainRevenueResult = channelRevenueRepository.getChannelRevenueWithSinglePlatform(platform,chainBeginTime,chainEndTime);
        }

        Double revenue = Optional.ofNullable(revenueResult).orElse(0.00);
        Double chainRevenue = Optional.ofNullable(chainRevenueResult).orElse(0.00);

        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(revenue,chainRevenue);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");
    }


}
