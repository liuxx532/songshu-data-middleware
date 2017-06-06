package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.cache.util.EhCacheKey;
import com.comall.songshu.service.RequestCacheService;
import com.comall.songshu.service.channel.*;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.TargetsMap;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Created by huanghaizhou on 2017/5/8.
 */
@RestController
@RequestMapping("/channel")
public class ChannelResource {

    private final Logger log = LoggerFactory.getLogger(ChannelResource.class);

    @Autowired
    private VisitTimeDistributionService visitTimeDistributionService;
    @Autowired
    private VisitDeepDistributionService visitDeepDistributionService;
    @Autowired
    private ManufacturerRankService manufacturerRankService;
    @Autowired
    private RegionRankService regionRankService;
    @Autowired
    private AgeAndSexDistributionService ageAndSexDistributionService;
    @Autowired
    private ChannelRevenueService channelRevenueService;
    @Autowired
    private ChannelOrderCountService channelOrderCountService;
    @Autowired
    private ChannelAvgOrderRevenueService channelAvgOrderRevenueService;
    @Autowired
    private ChannelUniqueVisitorsService channelUniqueVisitorsService;
    @Autowired
    private ChannelGrossMarginRateService channelGrossMarginRateService;
    @Autowired
    private ChannelConsumerCountService channelConsumerCountService;
    @Autowired
    private ChannelPageInfoService channelPageInfoService;
    @Autowired
    private RequestCacheService requestCacheService;

    @GetMapping("")
    @Timed
    public Map<String,String> getTargets() {
        return TargetsMap.channelTargets();
    }

    @PostMapping("/search")
    @Timed
    public Collection<String> getKeys() {
        return TargetsMap.channelTargets().keySet();
    }

    @PostMapping("/removeCache")
    @Timed
    public boolean removeCache() {
        return requestCacheService.removeAllRequestCache(EhCacheKey.CHANNEL_CACHE);
    }

    @PostMapping("/query")
    @Timed
    public String query(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestBody String requestBody) throws Exception{
        log.debug("[RequestBody] {}", requestBody);


        String result = requestCacheService.getRequestCache(request,requestBody);
        if(result != null){
            return result;
        }

        if(Optional.ofNullable(requestBody).isPresent()){

            JSONObject obj = new JSONObject(requestBody);

            JSONObject range = (JSONObject)obj.get("range");


            //指定平台（渠道）
            Object channelNameObj = null;
            if(obj.has("channel")){
                channelNameObj = obj.get("channel");
            }
            String channelName = Optional.ofNullable(channelNameObj)
                .map( o -> o.toString())
                .orElse(null);

            //开始时间str
            String fromTimeStr = null;
            //结束时间str
            String toTimeStr = null;
            if(Optional.ofNullable(range).isPresent()){
                fromTimeStr = (String)range.get("from");
                toTimeStr = (String)range.get("to");
            }

            Map<String,Timestamp> dateMap = AssembleUtil.assemblerDateMap(fromTimeStr,toTimeStr);
            //开始时间
            Timestamp beginTime = dateMap.get("beginTime");
            //结束时间
            Timestamp endTime = dateMap.get("endTime");
            //环比时间
            Timestamp chainBeginTime = dateMap.get("chainBeginTime");
            Timestamp chainEndTime = dateMap.get("chainEndTime");


            //指标中文名称
            JSONArray  channelTargets = (JSONArray)obj.get("targets");
            String target = null;
            if (Optional.ofNullable( channelTargets)
                .filter((value) -> value.length() >0)
                .isPresent()) {
                JSONObject targetJsonObj = (JSONObject) channelTargets.get(0);
                if (Optional.ofNullable(targetJsonObj).isPresent()){
                    String targetObj =  (String)targetJsonObj.get("target");
                    target = Optional.ofNullable(targetObj)
                        .map( o ->  TargetsMap. channelTargets().get(o.toString()))
                        .orElse(null);
                }
            }

            //指定平台（渠道）
            Object platformObj =  obj.get("platform");
            String platform = Optional.ofNullable(platformObj)
                .map( o -> o.toString())
                .orElse(null);


            if (beginTime != null && endTime!= null
                && target != null && platform != null){

                switch (target) {
                    case "VisitTimeDistribution":
                        result =   visitTimeDistributionService.getVisitTimeDistribution(target,platform,beginTime,endTime);break;
                    case "VisitDeepDistribution":
                        result =   visitDeepDistributionService.getVisitDeepDistribution(target,platform,beginTime,endTime);break;
                    case "ManufacturerRank":
                        result =   manufacturerRankService.getManufacturerRank(target,platform,beginTime,endTime,10);break;
                    case "RegionRank":
                        result =   regionRankService.getRegionRank(target,platform,beginTime,endTime,10);break;
                    case "AgeDistribution":
                        result =   ageAndSexDistributionService.getAgeDistribution(target,platform,beginTime,endTime);break;
                    case "SexDistribution":
                        result =   ageAndSexDistributionService.getSexDistribution(target,platform,beginTime,endTime);break;
                    case "ChannelRevenue":
                        result =   channelRevenueService.getChannelRevenue(target,platform,channelName,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "ChannelOrderCount":
                        result =   channelOrderCountService.getChannelOrderCount(target,platform,channelName,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "ChannelAvgOrderRevenue":
                        result =   channelAvgOrderRevenueService.getChannelAvgOrderRevenue(target,platform,channelName,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "ChannelUniqueVisitors":
                        result =   channelUniqueVisitorsService.getChannelUniqueVisitors(target,platform,channelName,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "ChannelGrossMarginRate":
                        result =   channelGrossMarginRateService.getChannelGrossMarginRate(target,platform,channelName,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "ChannelConsumerCount":
                        result =   channelConsumerCountService.getChannelConsumerRevenue(target,platform,channelName,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "ChannelPageInfo":
                        result =   channelPageInfoService.getChannelPageInfo(target,platform,beginTime,endTime);break;
                    default:
                        throw new IllegalArgumentException("target=" + target);
                }
            }

            if(result != null){
                requestCacheService.putRequestCache(request,requestBody,result);
                return result;
            }
        }
        return null;
    }
}
