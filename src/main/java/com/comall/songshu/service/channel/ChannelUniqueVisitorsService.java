package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.ChannelUniqueVisitorsRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 渠道页面访客数
 * Created by lgx on 17/4/25.
 */
@Service
public class ChannelUniqueVisitorsService {

    @Autowired
    private ChannelUniqueVisitorsRepository channelUniqueVisitorsRepository;

    public String getChannelUniqueVisitors(String target,String platformName,String channelName, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {
        Double uniqueVisitors;
        Double chainUniqueVisitors;

        int platform = TransferUtil.getPlatform(platformName);
        boolean isChannelNameEmpty = channelName == null || channelName.equals("");

        if (platform<0){//全部
            if(isChannelNameEmpty){
                uniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsAllPlatformAllChannel(beginTime, endTime);
                chainUniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsAllPlatformAllChannel(chainBeginTime, chainEndTime);
            }else{
                uniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsAllPlatformSingleChannel(beginTime,endTime,channelName);
                chainUniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsAllPlatformSingleChannel(chainBeginTime, chainEndTime,channelName);
            }
        }  else {
            if(isChannelNameEmpty){
                uniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsSinglePlatformAllChannel(platformName, beginTime, endTime);
                chainUniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsSinglePlatformAllChannel(platformName, chainBeginTime, chainEndTime);
            }else {
                uniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsSinglePlatformSingleChannel(platformName,beginTime,endTime,channelName);
                chainUniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsSinglePlatformSingleChannel(platformName,chainBeginTime,chainEndTime,channelName);
            }
        }

        TopStat result= AssembleUtil.assemblerTopStat(uniqueVisitors,chainUniqueVisitors);

        return  JsonStringBuilder.buildTargetJsonString(target,result,"");
    }



}
