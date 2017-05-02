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

    public String getUniqueVisitorsTrend(String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime, int aggCount){

        int platform = TransferUtil.getPlatform(platformName);

        Integer interValue= ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime,aggCount);


        //所有平台
        List<Object[] > currentAllPlatformResult = null;
        List<Object[] > chainAllPlatformResult = null;

        //单个平台
        List<Object[] > currentSinglePlatformResult = null;
        List<Object[] > chainSinglePlatformResult = null;


        //所有平台
        if (platform < 0){ //所有平台
            //当前
            currentAllPlatformResult = uniqueVisitorsRepository.getUniqueVisitorsTrendAllPlatform(beginTime, endTime, interValue);
            //环比
            chainAllPlatformResult = uniqueVisitorsRepository.getUniqueVisitorsTrendAllPlatform(chainBeginTime, chainEndTime, interValue);

            List<Object[]> currentAllPlatform =null ;
            List<Object[]> chainAllPlatform =null ;

            if (null != currentAllPlatformResult){
                currentAllPlatform= currentAllPlatformResult;
            }
            if(null != chainAllPlatformResult){
                chainAllPlatform= chainAllPlatformResult;
            }

            return JsonStringBuilder.buildTrendJsonStringForLongType(currentAllPlatform,chainAllPlatform);
        }else {//单个平台
            currentSinglePlatformResult = uniqueVisitorsRepository.getUniqueVisitorsTrendSinglePlatform(platformName, beginTime, endTime, interValue);
            chainSinglePlatformResult = uniqueVisitorsRepository.getUniqueVisitorsTrendSinglePlatform(platformName, chainBeginTime, chainEndTime, interValue);

            List<Object[]> currentSinglePlatform =null;
            List<Object[]> chainSinglePlatform =null;

            if(null != currentSinglePlatformResult){
                currentSinglePlatform = currentSinglePlatformResult;

            }
            if (null != chainSinglePlatformResult){
                chainSinglePlatform = chainSinglePlatformResult;
            }
            return JsonStringBuilder.buildTrendJsonStringForLongType(currentSinglePlatform,chainSinglePlatform);
        }

    }

}
