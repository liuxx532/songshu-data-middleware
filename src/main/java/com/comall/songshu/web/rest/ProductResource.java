package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.service.product.ProductRadarService;
import com.comall.songshu.service.product.ProductRevenueService;
import com.comall.songshu.web.rest.util.ServiceUtil;
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
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/product")
public class ProductResource {

    @Autowired
    private ProductRevenueService productRevenueService;

    @Autowired
    private ProductRadarService productRadarService;

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    @GetMapping("")
    @Timed
    public Map<String,String> getTargets() {
        return TargetsMap.productTargets();
    }

    @PostMapping("/search")
    @Timed
    public Collection<String> getKeys() {
        return TargetsMap.productTargets().keySet();
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
                 fromTimeStr = (String)range.get("from");
                 toTimeStr = (String)range.get("to");
            }


            //开始时间
            Timestamp beginTime = null;
            //结束时间
            Timestamp endTime = null;
            //环比时间
            Timestamp chainBeginTime = null;
            Timestamp chainEndTime = null;

            if(fromTimeStr != null && toTimeStr != null){
                beginTime = Optional.of(fromTimeStr)
                    .map(String::trim)
                    .filter(s -> s.length() >0)
                    .map(v -> ServiceUtil.getInstance().parseTimestamp(v))
                    .orElse(null);
                endTime = Optional.of(toTimeStr)
                    .map(String::trim)
                    .filter(s -> s.length() >0)
                    .map( s -> ServiceUtil.getInstance().parseTimestamp(s))
                    .orElse(null);
            }


            //指标中文名称
            JSONArray productTargets = (JSONArray)obj.get("targets");
            String target = null;
            if (Optional.ofNullable(productTargets)
                .filter((value) -> value.length() >0)
                .isPresent()) {
                JSONObject targetJsonObj = (JSONObject)productTargets.get(0);
                if (Optional.ofNullable(targetJsonObj).isPresent()){
                    String targetObj =  (String)targetJsonObj.get("target");
                    target = Optional.ofNullable(targetObj)
                        .map( o ->  TargetsMap.productTargets().get(o.toString()))
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
                    case "ProductRevenue":
                        return productRevenueService.getProductRevenue(target,platform,beginTime,endTime,chainBeginTime,chainEndTime,20);

                     //TODO 由于需要传入被过滤的品类ID，但是不知道前端字段具体怎么传，先待定
//                    case "ProductRadar":
//                        return
                    default:
                        throw new IllegalArgumentException("target=" + target);
                }
            }
        }
        return null;
    }
}
