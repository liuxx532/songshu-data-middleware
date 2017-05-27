package com.comall.songshu.service.member;

import com.comall.songshu.constants.TitleConstants;
import com.comall.songshu.repository.index.UniqueVisitorsRepository;
import com.comall.songshu.repository.member.ChannelRegisterMemberRepository;
import com.comall.songshu.repository.member.MemberDetailRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 用户数据
 *
 * @author liushengling
 * @create 2017-05-04-16:38
 **/
@Service
public class MemberDetailService {

    @Autowired
    private MemberDetailRepository memberDetailRepository;

    @Autowired
    private UniqueVisitorsRepository uniqueVisitorsRepository;

    public String getMemberDetail(String target, String platformName, Timestamp beginTime, Timestamp endTime) {

        int platform = TransferUtil.getPlatform(platformName);
        String os = TransferUtil.getSensorsOS(platform);

        //访客数
        Double uniqueVisitorCount;
        //消费用户数
        Integer consumerCount;
        //注册用户数
        Integer newRegisterCount;
        //注册-消费转化率 (注册且消费用户数／注册用户数)*100%
        Double logonConsumeRate;
        //复购数
        Integer repeatPurchaseCount;

        //启动次数
        Integer openTimes;
        //访问时长
        Integer visitTime;
        //页面浏览量
        Integer visitDepth;

        List<Object[]> titleList = new LinkedList<>();
        List<Object[]> valueList = new LinkedList<>();


        if (platform<0){//全部
            uniqueVisitorCount = uniqueVisitorsRepository.getUniqueVisitorsAllPlatform(beginTime,endTime);
            consumerCount = memberDetailRepository.getConsumerCountAllPlatform(beginTime,endTime);
            newRegisterCount = memberDetailRepository.getMemberRegisterCountAllPlatform(beginTime,endTime);
            logonConsumeRate = memberDetailRepository.getLogonConsumeRateAllPlatform(beginTime,endTime);
            repeatPurchaseCount = memberDetailRepository.getRepeatPurchaseRateAllPlatform(beginTime,endTime);

            openTimes = memberDetailRepository.getOpenTimesAllPlatform(beginTime,endTime);
            visitTime = memberDetailRepository.getVisitTimeAllPlatform(beginTime,endTime);
            visitDepth = memberDetailRepository.getVisitDepthAllPlatform(beginTime,endTime);
        }else {
            uniqueVisitorCount = uniqueVisitorsRepository.getUniqueVisitorsSinglePlatform(platformName,beginTime,endTime);
            consumerCount = memberDetailRepository.getConsumerCountSinglePlatform(beginTime,endTime,platform);
            newRegisterCount = memberDetailRepository.getMemberRegisterCountSinglePlatform(beginTime,endTime,platform);
            logonConsumeRate = memberDetailRepository.getLogonConsumeRateSinglePlatform(beginTime,endTime,platform);
            repeatPurchaseCount = memberDetailRepository.getRepeatPurchaseRateSinglePlatform(beginTime,endTime,platform);
            if(platform == TransferUtil.CHANNEL_IOS || platform == TransferUtil.CHANNEL_ANDROID){
                openTimes = memberDetailRepository.getOpenTimesSinglePlatform(beginTime,endTime,os,platformName);
            }else{
                openTimes = 0;
            }
            visitTime = memberDetailRepository.getVisitTimeSinglePlatform(beginTime,endTime,platformName);
            visitDepth = memberDetailRepository.getVisitDepthSinglePlatform(beginTime,endTime,platformName);
        }
        //复购率
        Double repeatPurchaseRate = Optional.ofNullable(consumerCount)
            .filter(c -> c>0)
            .map(d -> new BigDecimal(repeatPurchaseCount).doubleValue()/new BigDecimal(d).doubleValue())
            .orElse(0.0);
        //平均访问时长
        Integer avgVisitTime = Optional.ofNullable(uniqueVisitorCount)
            .filter(c -> c>0)
            .map(d -> (int)(visitTime/uniqueVisitorCount))
            .orElse(0);
        //平均访问深度
        Integer avgVisitDepth = Optional.ofNullable(uniqueVisitorCount)
            .filter(c -> c>0)
            .map(d -> (int)(visitDepth/uniqueVisitorCount))
            .orElse(0);

        //表头信息
        titleList.add(new Object[]{TitleConstants.TAG_NAME,"指标名"});
        titleList.add(new Object[]{TitleConstants.TAG_VALUE,"指标值"});
        //表内信息
        valueList.add(new Object[]{"访客数",uniqueVisitorCount});
        valueList.add(new Object[]{"注册用户数",newRegisterCount});
        valueList.add(new Object[]{"注册-消费转化率",logonConsumeRate});
        valueList.add(new Object[]{"消费用户数",consumerCount});
        valueList.add(new Object[]{"复购率",repeatPurchaseRate});
        valueList.add(new Object[]{"启动次数",openTimes});
        valueList.add(new Object[]{"平均访问时长",avgVisitTime});
        valueList.add(new Object[]{"平均访问深度",avgVisitDepth});


        return JsonStringBuilder.buildTableJsonString(valueList,titleList,target);
    }


}
