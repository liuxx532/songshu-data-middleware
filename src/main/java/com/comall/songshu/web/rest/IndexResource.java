package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.repository.OrderCountRepository;
import com.comall.songshu.service.*;
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
import java.text.SimpleDateFormat;
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
    private  NotFirstOrderedConsumerCountService notFirstOrderedConsumerCountService;

    @Autowired
    private CategoryRevenueRankingService categoryRevenueRankingService;

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

    @PostMapping("/query")
    @Timed
    public String query(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestBody String requestBody) throws Exception{

        log.debug("[RequestBody] {}", requestBody);

        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        if(Optional.ofNullable(requestBody).isPresent()){

            JSONObject obj = new JSONObject(requestBody);

            JSONObject range = (JSONObject)obj.get("range");
            //开始时间str
            String fromTimeStr = null;
            //结束时间str
            String toTimeStr = null;
            if(Optional.ofNullable(range).isPresent()){
                Object fromObj = range.get("from");
                Object toObj = range.get("to");
                fromTimeStr = Optional.ofNullable(fromObj)
                    .map( o -> (String)fromObj)
                    .orElse(null);
                toTimeStr = Optional.ofNullable(toObj)
                    .map( o -> (String)toObj)
                    .orElse(null);
            }


            //开始时间
            Timestamp beginTime = null;
            //结束时间
            Timestamp endTime = null;
            //环比时间
            Timestamp chainBeginTime = null;
            Timestamp chainEndTime = null;

            if(Optional.ofNullable(fromTimeStr).isPresent()
                && Optional.ofNullable(toTimeStr).isPresent()){
                beginTime = Optional.of(fromTimeStr)
                    .map( s -> ServiceUtil.getInstance().parseTimestamp(s))
                    .orElse(null);
                endTime = Optional.of(toTimeStr)
                    .map( s -> ServiceUtil.getInstance().parseTimestamp(s))
                    .orElse(null);
                //环比时间
                String[] chainCreateTime = ServiceUtil.getInstance().getChainIndexDateTime(fromTimeStr,toTimeStr);
                if(Optional.ofNullable(chainCreateTime).isPresent()){
                    chainBeginTime = Optional.of(chainCreateTime)
                        .map( a -> a[0])
                        .map( s -> DateTime.parse(s))
                        .map( d -> d.toString(dateTimeFormat))
                        .map( t -> Timestamp.valueOf(t))
                        .orElse(null);
                    chainEndTime = Optional.of(chainCreateTime)
                        .map( a -> a[1])
                        .map( s -> DateTime.parse(s))
                        .map( d -> d.toString(dateTimeFormat))
                        .map( t -> Timestamp.valueOf(t))
                        .orElse(null);
                }
            }


            //指标中文名称
            JSONArray targets = (JSONArray)obj.get("targets");
            String target = null;
            if (Optional.ofNullable(targets)
                .filter((value) -> value.length() >0)
                .isPresent()) {
                JSONObject targetJsonObj = (JSONObject)targets.get(0);
                if (Optional.ofNullable(targetJsonObj).isPresent()){
                    Object targetObj =  targetJsonObj.get("target");
                    target = Optional.ofNullable(targetObj)
                        .map( o -> o.toString())
                        .map( s -> TargetsMap.getTargets().get(s))
                        .orElse(null);
                }
            }

            //指定平台（渠道）
            Object platformObj =  obj.get("platform");
            String platform = Optional.ofNullable(platformObj)
                .map( o -> o.toString())
                .orElse(null);


            if (Optional.ofNullable(beginTime).isPresent()
                && Optional.ofNullable(endTime).isPresent()
                && Optional.ofNullable(chainBeginTime).isPresent()
                && Optional.ofNullable(chainEndTime).isPresent()
                && Optional.ofNullable(target).isPresent()
                && Optional.ofNullable(platform).isPresent()){

                switch (target) {
                    // 单个指标
                    case "Revenue":
                        return revenueService.getRevenue(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);
                    case "OrderCount":
                        return orderCountService.getOrderCount(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);
                    case "AvgOrderRevenue":
                        return avgOrderRevenueService.getAvgOrderRevenue(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);
                    case "UniqueVisitors":
                        return uniqueVisitorsService.getUniqueVisitors(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);
                    case "Refund":
                        return refundService.getRefund(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);
                    case "GrossMarginRate":
                        return grossMarginRateService.getGrossMarginRate(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);

                    // 趋势
                    case "RevenueTrend" :
                        return revenueService.getRevenueTrend(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);
                    case "OrderCountTrend" :
                        return orderCountService.getOrderCountTrend(platform, beginTime, endTime, chainBeginTime, chainEndTime);
                    case "AvgOrderRevenueTrend" :
                        return avgOrderRevenueService.getAvgOrderRevenueTrend(platform, beginTime, endTime, chainBeginTime, chainEndTime);
                    case "UniqueVisitorsTrend" :
                        return uniqueVisitorsService.getUniqueVisitorsTrend(platform, beginTime, endTime, chainBeginTime, chainEndTime);
                    case "RefundTrend" :
                        return refundService.getRefundTrend(platform, beginTime, endTime, chainBeginTime, chainEndTime);
                    case "GrossMarginRateTrend" :
                        return grossMarginRateService.getGrossMarginRateTrend(platform, beginTime, endTime, chainBeginTime, chainEndTime);
                    // 饼图
                    case "NewRegisterRate":
                        return newRegisterCountService.getNewRegisterCount(platform,beginTime,endTime);
                    case "FirstOrderedRate":
                        return  orderedConsumerCountService.getOrderedConsumerCount(platform, beginTime, endTime);
                    // 品类销售排行榜
                    case "CategoryRevenueRanking":
                        return categoryRevenueRankingService.getCategoryRevenueRanking(platform, beginTime, endTime);

                    default:
                        throw new IllegalArgumentException("target=" + target);
                }
            }
        }
        return null;
    }
}
