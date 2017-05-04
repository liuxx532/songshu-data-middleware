package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TargetsMap;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Created by huanghaizhou on 2017/5/4.
 */
@RestController
@RequestMapping("/member")
public class MemberResource {

    private final Logger log = LoggerFactory.getLogger(MemberResource.class);

    @GetMapping("")
    @Timed
    public Map<String,String> getTargets() {
        return TargetsMap.memberTargets();
    }

    @PostMapping("/search")
    @Timed
    public Collection<String> getKeys() {
        return TargetsMap.memberTargets().keySet();
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
                //环比时间
                String[] chainCreateTime = ServiceUtil.getInstance().getChainIndexDateTime(fromTimeStr,toTimeStr);
                if(chainCreateTime != null){
                    chainBeginTime = Optional.of(chainCreateTime)
                        .filter( array -> array.length >0)
                        .map( a -> a[0])
                        .map(String::trim)
                        .filter(s -> s.length() >0)
                        .map( s -> DateTime.parse(s))
                        .map( d-> Timestamp.valueOf(d.toString(dateTimeFormat)))
                        .orElse(null);
                    chainEndTime = Optional.of(chainCreateTime)
                        .filter( array -> array.length >1)
                        .map( a -> a[1])
                        .map(String::trim)
                        .filter(s -> s.length() >0)
                        .map( s -> DateTime.parse(s))
                        .map( d-> Timestamp.valueOf(d.toString(dateTimeFormat)))
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
//                    case "Revenue":
//                        return revenueService.getRevenue(target,platform,beginTime,endTime,chainBeginTime,chainEndTime);

                    // 趋势
//                    case "RevenueTrend" :
//                        return revenueService.getRevenueTrend(target,platform,beginTime,endTime,chainBeginTime,chainEndTime, TrendConstants.aggCount);

                    default:
                        throw new IllegalArgumentException("target=" + target);
                }
            }
        }
        return null;
    }
}
