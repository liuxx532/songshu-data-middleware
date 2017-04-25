package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.repository.AuthorRepository;
import com.comall.songshu.service.*;
import com.comall.songshu.web.rest.util.TargetsMap;
import jdk.nashorn.internal.runtime.options.Option;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
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
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/index")
public class IndexResource {

    @Autowired
    private RevenueService revenueService; //销售额

    @Autowired
    private OrderService orderService;

    @Autowired
    private AvgOrderRevenueService avgOrderRevenueService;

    @Autowired
    private VisitorsService visitorsService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private GrossMarginService grossMarginService;



    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

//    public IndexResource(RevenueService revenueService){
//        this.revenueService = revenueService;
//    }

    /**
    public IndexResource(OrderService orderService){
        this.orderService = orderService;
    }



    public IndexResource(AvgOrderRevenueService avgOrderRevenueService){
        this.avgOrderRevenueService = avgOrderRevenueService;
    }


    public IndexResource(VisitorsService visitorsService){
        this.visitorsService = visitorsService;
    }


    public IndexResource(RefundService refundService){
        this.refundService = refundService;
    }

    public IndexResource(GrossMarginService grossMarginService){
        this.grossMarginService = grossMarginService;
    }

     */

    public IndexResource(GrossMarginService grossMarginService){
        this.grossMarginService = grossMarginService;
    }

    @GetMapping("")
    @Timed
    public Map<String,String> getTargets() {
        return TargetsMap.getTargets();
    }

    @PostMapping("/search")
    @Timed
    public Collection<String> getKeys() {
        return TargetsMap.getTargets().values();
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
            //开始时间
            String fromTime = null;
            //结束时间
            String toTime = null;
            if(Optional.ofNullable(range).isPresent()){
                Object fromObj = range.get("from");
                Object toObj = range.get("to");
                fromTime = Optional.ofNullable(fromObj)
                    .map( o -> (String)fromObj)
                    .orElse(null);
                toTime = Optional.ofNullable(toObj)
                    .map( o -> (String)toObj)
                    .orElse(null);
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


            if (Optional.ofNullable(fromTime).isPresent()
                && Optional.ofNullable(toTime).isPresent()
                && Optional.ofNullable(target).isPresent()
                && Optional.ofNullable(platform).isPresent()){
                switch (target) {
                        case "Revenue":
                            return revenueService.getRevenue(target,platform,fromTime,toTime);
                        case "OrderCount":
                            // return orderService.getOrder();
                        case "AvgOrderRevenue":
                            return avgOrderRevenueService.getAvgOrderRevenue(platform,range.getString("from"),range.getString("to"));
                        case "UniqueVisitors":
                            //   return visitorsService.getVisitors();
                        case "Refund":
                            //   return refundService.getRefund();
                        case "GrossMarginRate":
                            //  return grossMarginService.getGrossMargin();
                        default:
                            throw new IllegalArgumentException("target=" + target);

                }
            }

        }
        return null;
    }

}
