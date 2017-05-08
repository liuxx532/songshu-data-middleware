package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.ChannelUniqueVisitorsRepository;
import com.comall.songshu.repository.index.UniqueVisitorsRepository;
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
 * 渠道页面访客数
 * Created by lgx on 17/4/25.
 */
@Service
public class ChannelUniqueVisitorsService {

    @Autowired
    private ChannelUniqueVisitorsRepository channelUniqueVisitorsRepository;

    public String getChannelUniqueVisitors(String target,String platform, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {
        Double uniqueVisitors;
        Double chainUniqueVisitors;

        if ( platform.equals("all") ) {
            uniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsAllPlatform(beginTime, endTime);
            chainUniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsAllPlatform(chainBeginTime, chainEndTime);
        }  else {
            uniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsSinglePlatform(platform, beginTime, endTime);
            chainUniqueVisitors = channelUniqueVisitorsRepository.getChannelUniqueVisitorsSinglePlatform(platform, chainBeginTime, chainEndTime);
        }

        TopStat result= AssembleUtil.assemblerTopStat(uniqueVisitors,chainUniqueVisitors);

        return  JsonStringBuilder.buildTargetJsonString(target,result,"");
    }



}
