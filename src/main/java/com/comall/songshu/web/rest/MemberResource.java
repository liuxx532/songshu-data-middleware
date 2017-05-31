package com.comall.songshu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comall.songshu.constants.TrendConstants;
import com.comall.songshu.service.member.ChannelRegisterMemberService;
import com.comall.songshu.service.member.MemberDetailService;
import com.comall.songshu.service.member.MemberFunnelService;
import com.comall.songshu.service.member.MemberShareService;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TargetsMap;
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
 * Created by huanghaizhou on 2017/5/4.
 */
@RestController
@RequestMapping("/member")
public class MemberResource {

    private final Logger log = LoggerFactory.getLogger(MemberResource.class);


    @Autowired
    private MemberShareService memberShareService;

    @Autowired
    private ChannelRegisterMemberService channelRegisterMemberService;

    @Autowired
    private MemberDetailService memberDetailService;

    @Autowired
    private MemberFunnelService memberFunnelService;

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
//            Timestamp chainBeginTime = dateMap.get("chainBeginTime");
//            Timestamp chainEndTime = dateMap.get("chainEndTime");



            //指标中文名称
            JSONArray memberTargets = (JSONArray)obj.get("targets");
            String target = null;
            if (Optional.ofNullable(memberTargets)
                .filter((value) -> value.length() >0)
                .isPresent()) {
                JSONObject targetJsonObj = (JSONObject)memberTargets.get(0);
                if (Optional.ofNullable(targetJsonObj).isPresent()){
                    String targetObj =  (String)targetJsonObj.get("target");
                    target = Optional.ofNullable(targetObj)
                        .map( o ->  TargetsMap.memberTargets().get(o.toString()))
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
                    // 单个指标
                   case "MemberShareDetail":
                       return memberShareService.getMemberShareDetailByName(target,platform,beginTime,endTime);
                    case "ChannelRegisterMember":
                       return channelRegisterMemberService.getChannelMemberRegisterCount(target,platform,beginTime,endTime,10);
                    case "MemberDetail":
                        return memberDetailService.getMemberDetail(target,platform,beginTime,endTime);

                    // 趋势
                    case "MemberShareTrend" :
                        return memberShareService.getMemberShareTrendByName(target,platform,beginTime,endTime, TrendConstants.aggCount);

                    //漏斗
                    case "MemberFunnel" :
                        return memberFunnelService.getMemberFunnel(target,platform,beginTime,endTime);
                    default:
                        throw new IllegalArgumentException("target=" + target);
                }
            }
        }
        return null;
    }
}
