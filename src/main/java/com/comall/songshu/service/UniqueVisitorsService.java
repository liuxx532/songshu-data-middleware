package com.comall.songshu.service;

import com.comall.songshu.repository.UniqueVisitorsRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * 访客数
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

    public String getUniqueVisitorsTrend(String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime, int aggCount){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interValue= ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime,aggCount);

        //当前
        List<Object[] > currentUniqueVisitorsResult ;
        //环比
        List<Object[] > chainUniqueVisitorsResult ;

        if (platform < 0){ //所有平台
            currentUniqueVisitorsResult = uniqueVisitorsRepository.getUniqueVisitorsTrendAllPlatform(beginTime, endTime, interValue);
            chainUniqueVisitorsResult = uniqueVisitorsRepository.getUniqueVisitorsTrendAllPlatform(chainBeginTime, chainEndTime, interValue);
        }else {//单个平台
            currentUniqueVisitorsResult = uniqueVisitorsRepository.getUniqueVisitorsTrendSinglePlatform(platformName, beginTime, endTime, interValue);
            chainUniqueVisitorsResult = uniqueVisitorsRepository.getUniqueVisitorsTrendSinglePlatform(platformName, chainBeginTime, chainEndTime, interValue);
        }

        List<Object[]> currentUniqueVisitors = Optional.ofNullable(currentUniqueVisitorsResult).orElse(null);
        List<Object[]> chainUniqueVisitors = Optional.ofNullable(chainUniqueVisitorsResult).orElse(null);

        return JsonStringBuilder.buildTrendJsonStringForLongType(currentUniqueVisitors,chainUniqueVisitors);
    }


}
