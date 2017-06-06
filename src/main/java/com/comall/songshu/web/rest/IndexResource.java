package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.cache.util.EhCacheKey;
import com.comall.songshu.constants.TrendConstants;
import com.comall.songshu.service.RequestCacheService;
import com.comall.songshu.service.index.*;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TargetsMap;
import org.joda.time.DateTime;
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
import java.util.*;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/index")
public class IndexResource {

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private OrderCountService orderCountService;

    @Autowired
    private AvgOrderRevenueService avgOrderRevenueService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private GrossMarginRateService grossMarginRateService;

    @Autowired
    private UniqueVisitorsService uniqueVisitorsService;

    @Autowired
    private OrderedConsumerCountService orderedConsumerCountService;

    @Autowired
    private  NewRegisterCountService newRegisterCountService;

    @Autowired
    private CategoryRevenueRankingService categoryRevenueRankingService;

    @Autowired
    private RequestCacheService requestCacheService;

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);


    public IndexResource(GrossMarginRateService grossMarginRateService){
        this.grossMarginRateService = grossMarginRateService;
    }

    @GetMapping("")
    @Timed
    public Map<String,String> getTargets() {
        return TargetsMap.getTargets();
    }

    @PostMapping("/search")
    @Timed
    public Collection<String> getKeys() {
        return TargetsMap.getTargets().keySet();
    }

    @PostMapping("/removeCache")
    @Timed
    public boolean removeCache() {
        return requestCacheService.removeAllRequestCache(EhCacheKey.INDEX_CACHE);
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

        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        if(Optional.ofNullable(requestBody).isPresent()){

            JSONObject obj = new JSONObject(requestBody);

            JSONObject range = (JSONObject)obj.get("range");
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
            JSONArray targets = (JSONArray)obj.get("targets");
            String target = null;
            if (Optional.ofNullable(targets)
                .filter((value) -> value.length() >0)
                .isPresent()) {
                JSONObject targetJsonObj = (JSONObject)targets.get(0);
                if (Optional.ofNullable(targetJsonObj).isPresent()){
                    String targetObj =  (String)targetJsonObj.get("target");
                    target = Optional.ofNullable(targetObj)
                        .map( o ->  TargetsMap.getTargets().get(o.toString()))
                        .orElse(null);
                }
            }

            //指定平台（渠道）
            Object platformObj =  obj.get("platform");
            String platform = Optional.ofNullable(platformObj)
                .map( o -> o.toString())
                .orElse(null);


            if (beginTime != null && endTime!= null
                && chainBeginTime != null && chainEndTime != null
                && target != null && platform != null){

                switch (target) {
                    // 单个指标
                    case "Revenue":
                        result =   revenueService.getRevenue(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "OrderCount":
                        result =   orderCountService.getOrderCount(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "AvgOrderRevenue":
                        result =   avgOrderRevenueService.getAvgOrderRevenue(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "UniqueVisitors":
                        result =   uniqueVisitorsService.getUniqueVisitors(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "Refund":
                        result =   refundService.getRefund(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);break;
                    case "GrossMarginRate":
                        result =   grossMarginRateService.getGrossMarginRate(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);break;

                    // 趋势
                    case "RevenueTrend" :
                        result =   revenueService.getRevenueTrend(target,platform,beginTime,endTime,chainBeginTime,chainEndTime,TrendConstants.aggCount);break;
                    case "OrderCountTrend" :
                        result =   orderCountService.getOrderCountTrend(platform, beginTime, endTime, chainBeginTime, chainEndTime,TrendConstants.aggCount);break;
                    case "AvgOrderRevenueTrend" :
                        result =   avgOrderRevenueService.getAvgOrderRevenueTrend(platform, beginTime, endTime, chainBeginTime, chainEndTime,TrendConstants.aggCount);break;
                    case "UniqueVisitorsTrend" :
                        result =   uniqueVisitorsService.getUniqueVisitorsTrend(platform, beginTime, endTime, chainBeginTime, chainEndTime,TrendConstants.aggCount);break;
                    case "RefundTrend" :
                        result =   refundService.getRefundTrend(platform, beginTime, endTime, chainBeginTime, chainEndTime,TrendConstants.aggCount);break;
                    case "GrossMarginRateTrend" :
                        result =   grossMarginRateService.getGrossMarginRateTrend(platform, beginTime, endTime, chainBeginTime, chainEndTime,TrendConstants.aggCount);break;
                    // 饼图
                    case "NewRegisterRate":
                        result =   newRegisterCountService.getNewRegisterCount(beginTime,endTime);break;
                    case "FirstOrderedRate":
                        result =    orderedConsumerCountService.getOrderedConsumerCount(platform, beginTime, endTime);break;
                    // 品类销售排行榜
                    case "CategoryRevenueRanking":
                        result =   categoryRevenueRankingService.getCategoryRevenueRanking(platform, beginTime, endTime);break;

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
