package com.comall.songshu.service.member;

import com.comall.songshu.repository.index.UniqueVisitorsRepository;
import com.comall.songshu.repository.member.ChannelRegisterMemberRepository;
import com.comall.songshu.repository.member.MemberDetailRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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
        List<Object[] > memberDetailResult;
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

        if (platform<0){//全部
            uniqueVisitorCount = uniqueVisitorsRepository.getUniqueVisitorsAllPlatform(beginTime,endTime);
            consumerCount = memberDetailRepository.getConsumerCountAllPlatform(beginTime,endTime);
            newRegisterCount = memberDetailRepository.getMemberRegisterCountAllPlatform(beginTime,endTime);
            logonConsumeRate = memberDetailRepository.getLogonConsumeRateAllPlatform(beginTime,endTime);
            repeatPurchaseCount = memberDetailRepository.getRepeatPurchaseRateAllPlatform(beginTime,endTime);
        }else {
            uniqueVisitorCount = uniqueVisitorsRepository.getUniqueVisitorsSinglePlatform(platformName,beginTime,endTime);
            consumerCount = memberDetailRepository.getConsumerCountSinglePlatform(beginTime,endTime,platform);
            newRegisterCount = memberDetailRepository.getMemberRegisterCountSinglePlatform(beginTime,endTime,platform);
            logonConsumeRate = memberDetailRepository.getLogonConsumeRateSinglePlatform(beginTime,endTime,platform);
            repeatPurchaseCount = memberDetailRepository.getRepeatPurchaseRateSinglePlatform(beginTime,endTime,platform);
        }
        //复购率
        Double repeatPurchaseRate = Optional.ofNullable(consumerCount)
            .filter(c -> c>0)
            .map(d -> new BigDecimal(repeatPurchaseCount).doubleValue()/new BigDecimal(d).doubleValue())
            .orElse(0.0);

        return "uniqueVisitorCount:"+uniqueVisitorCount
            + "consumerCount:"+consumerCount
            + "newRegisterCount:"+newRegisterCount
            + "logonConsumeRate:"+logonConsumeRate
            + "repeatPurchaseRate:"+repeatPurchaseRate;
    }

    public static void main(String[] at){
        Integer a = 1231;
        Integer b = 231312;
        BigDecimal aa = new BigDecimal(a);
        BigDecimal bb = new BigDecimal(b);
        System.out.println(aa.doubleValue());
        System.out.println(bb.doubleValue());

        Double cc = new BigDecimal(a).doubleValue()/new BigDecimal(b).doubleValue();
        System.out.println(cc.doubleValue());
    }

}
