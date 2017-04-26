package com.comall.songshu.service;

import com.comall.songshu.repository.UniqueVisitorsRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by lgx on 17/4/25.
 */
@Component
public class UniqueVisitorsService {
    @Autowired
    private UniqueVisitorsRepository uniqueVisitorsRepository;

    public String getUniqueVisitors(String target,String platform, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {
        Double uniqueVisitors;
        Double chainUniqueVisitors;

        if ( platform.equals("all") ) {
            uniqueVisitors = uniqueVisitorsRepository.getUniqueVisitorsAllPlatform(beginTime, endTime);
            chainUniqueVisitors = uniqueVisitorsRepository.getUniqueVisitorsAllPlatform(chainBeginTime, chainEndTime);
        }  else {
            uniqueVisitors = uniqueVisitorsRepository.getUniqueVisitorsSinglePlatform(platform, beginTime, endTime);
            chainUniqueVisitors = uniqueVisitorsRepository.getUniqueVisitorsSinglePlatform(platform, chainBeginTime, chainEndTime);
        }

        TopStat result= AssembleUtil.assemblerTopStat(uniqueVisitors,chainUniqueVisitors);

        return  JsonStringBuilder.buildTargetJsonString(target,result,"");
    }

}
