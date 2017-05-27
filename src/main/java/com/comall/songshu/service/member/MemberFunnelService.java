package com.comall.songshu.service.member;

import com.comall.songshu.repository.member.MemberFunnelRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 会员漏斗Service
 * Created by huanghaizhou on 2017/5/5.
 */
@Service
public class MemberFunnelService {

    @Autowired
    private MemberFunnelRepository memberFunnelRepository;

    /**
     * @param target
     * @param platformName
     * @param beginTime
     * @param endTime
     * @return
     */
    public String getMemberFunnel( String target, String platformName, Timestamp beginTime, Timestamp endTime){
        Object[] visitorArray = getMemberFunnelForVisitor(target,platformName,beginTime,endTime);
        Object[] registerArray = getMemberFunnelForRegister(target,platformName,beginTime,endTime);
        List<Object[]> arrayList =  new LinkedList<>();
        arrayList.add(visitorArray);
        arrayList.add(registerArray);

        return JsonStringBuilder.buildFunnelJsonString(arrayList,new Object[]{"启动","商品详情页","加入购物车","提交订单","支付订单"});
    }

    /**
     * 根据访客
     * @param target
     * @param platformName
     * @param beginTime
     * @param endTime
     * @return
     */
    private Object[] getMemberFunnelForVisitor(String target, String platformName, Timestamp beginTime, Timestamp endTime){
        int platform = TransferUtil.getPlatform(platformName);
        if(platform != TransferUtil.CHANNEL_IOS || platform != TransferUtil.CHANNEL_ANDROID){
            platform = TransferUtil.CHANNEL_ALL;
        }
        String os = TransferUtil.getSensorsOS(platform);
        Object openTimes;
        Object productDetail;
        Object addCart;
        Object createOrder;
        Object payOrder;

        if (platform == TransferUtil.CHANNEL_ALL) {
            List<Object[]> queryResult = memberFunnelRepository.getMemberFunnelWithAllPlatformForVisitor(beginTime, endTime);
            openTimes = queryResult.get(0)[0];
            productDetail = queryResult.get(0)[1];
            addCart = queryResult.get(0)[2];
            createOrder = queryResult.get(0)[3];
            payOrder = queryResult.get(0)[4];
        }else {
            List<Object[]> queryResult = memberFunnelRepository.getMemberFunnelWithSinglePlatformForVisitor(beginTime,endTime,os,platformName);
            openTimes = queryResult.get(0)[0];
            productDetail = queryResult.get(0)[1];
            addCart = queryResult.get(0)[2];
            createOrder = queryResult.get(0)[3];
            payOrder = queryResult.get(0)[4];
        }


//        BigDecimal openTimesB = new BigDecimal(openTimes);
//        BigDecimal productDetailB = new BigDecimal(productDetail);
//        BigDecimal addCardB = new BigDecimal(addCard);
//        BigDecimal createOrderB = new BigDecimal(createOrder);
//        BigDecimal payOrderB = new BigDecimal(payOrder);

//        BigDecimal openTimesScale = new BigDecimal(1);
//        BigDecimal productDetailScale = productDetailB.divide(openTimesB, 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal addCardScale = addCardB.divide(productDetailB, 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal createOrderScale = createOrderB.divide(addCardB, 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal payOrderScale = payOrderB.divide(createOrderB, 2, BigDecimal.ROUND_HALF_UP);
        return  new Object[]{openTimes,productDetail,addCart,createOrder,payOrder};
    }
    /**
     * 根据注册用户
     * @param target
     * @param platformName
     * @param beginTime
     * @param endTime
     * @return
     */
    private Object[] getMemberFunnelForRegister(String target, String platformName, Timestamp beginTime, Timestamp endTime){
        int platform = TransferUtil.getPlatform(platformName);
        if(platform != TransferUtil.CHANNEL_IOS || platform != TransferUtil.CHANNEL_ANDROID){
            platform = TransferUtil.CHANNEL_ALL;
        }
        String os = TransferUtil.getSensorsOS(platform);
        Object openTimes;
        Object productDetail;
        Object addCart ;
        Object createOrder;
        Object payOrder;

        if (platform<0) {
            List<Object[]> queryResult = memberFunnelRepository.getMemberFunnelWithAllPlatformForRegister(beginTime, endTime);
            openTimes = queryResult.get(0)[0];
            productDetail = queryResult.get(0)[1];
            addCart = queryResult.get(0)[2];
            createOrder = queryResult.get(0)[3];
            payOrder = queryResult.get(0)[4];
        }else {
            List<Object[]> queryResult = memberFunnelRepository.getMemberFunnelWithSinglePlatformForRegister(beginTime,endTime,os,platformName);
            openTimes = queryResult.get(0)[0];
            productDetail = queryResult.get(0)[1];
            addCart = queryResult.get(0)[2];
            createOrder = queryResult.get(0)[3];
            payOrder = queryResult.get(0)[4];
        }

//        BigDecimal openTimesB = new BigDecimal(openTimes);
//        BigDecimal productDetailB = new BigDecimal(productDetail);
//        BigDecimal addCardB = new BigDecimal(addCard);
//        BigDecimal createOrderB = new BigDecimal(createOrder);
//        BigDecimal payOrderB = new BigDecimal(payOrder);

//        BigDecimal openTimesScale = new BigDecimal(1);
//        BigDecimal productDetailScale = productDetailB.divide(openTimesB, 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal addCardScale = addCardB.divide(productDetailB, 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal createOrderScale = createOrderB.divide(addCardB, 2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal payOrderScale = payOrderB.divide(createOrderB, 2, BigDecimal.ROUND_HALF_UP);

        return  new Object[]{openTimes,productDetail,addCart,createOrder,payOrder};
    }
}
