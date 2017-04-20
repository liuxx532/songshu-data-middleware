package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.repository.AuthorRepository;
import com.comall.songshu.service.*;
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
import java.util.Collection;
import java.util.Map;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/index")
public class IndexResource {

    @Autowired
    private RevenueService revenueService;

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
    public Object[] query(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody String requestBody) throws Exception{

        log.debug("[RequestBody] {}", requestBody);

        JSONObject obj = new JSONObject(requestBody);

        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        JSONObject range = (JSONObject)obj.get("range");


        Timestamp fromTime = Timestamp.valueOf(new DateTime(range.get("from")).toString(dateTimeFormat));
        Timestamp toTime = Timestamp.valueOf(new DateTime(range.get("to")).toString(dateTimeFormat));
        JSONArray targets = (JSONArray)obj.get("targets");
        String target = "";
        if (targets.length() > 0) {
            JSONObject targetObj = (JSONObject)targets.get(0);
            target = targetObj.get("target").toString();
        }

        String platform = obj.get("platform").toString();


//        if (target.endsWith("Trend")){
//
//            switch (target){
//                case "RevenueTrend" :
//
//                    break;
//
//                case "OrderTrend" :
//
//                    break;
//                case "AvgOrderRevenueTrend" :
//
//                    break;
//                case "VisitorsTrend" :
//
//                    break;
//                case "RefundTrend" :
//
//                    break;
//                case "GrossMarginTrend" :
//
//                    break;
//                case "NewSubscribersTrend" :
//
//                    break;
//                case "FirstOrderTrend" :
//
//                    break;
//                case "NotFirstOrderTrend" :
//
//                    break;
//                default:
//                    throw new IllegalArgumentException("target Trend"+target);
//            }

      //  }else {
            switch (target) {

                case "Revenue":
                    return revenueService.getRevenue();
                case "Order":
                    return orderService.getOrder();
                case "AvgOrderRevenue":
                    return avgOrderRevenueService.getAvgOrderRevenue();
                case "Visitors":
                    return visitorsService.getVisitors();
                case "Refund":
                    return refundService.getRefund();
                case "GrossMargin":
                    return grossMarginService.getGrossMargin();
                default:
                    throw new IllegalArgumentException("target=" + target);


            }
       // }










       // return null;
    }

}
