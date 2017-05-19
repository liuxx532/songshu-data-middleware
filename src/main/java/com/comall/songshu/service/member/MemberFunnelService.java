package com.comall.songshu.service.member;

import com.comall.songshu.repository.member.MemberFunnelRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
        String result = null;
        //TODO 访客这个文字可能要改
        getMemberFunnelForVisitor(target,platformName,beginTime,endTime);
        getMemberFunnelForRegister(target,platformName,beginTime,endTime);

        return result;
    }

    /**
     * 根据访客
     * @param target
     * @param platformName
     * @param beginTime
     * @param endTime
     * @return
     */
    private String getMemberFunnelForVisitor(String target, String platformName, Timestamp beginTime, Timestamp endTime){
        int platform = TransferUtil.getPlatform(platformName);
        if(platform != TransferUtil.CHANNEL_IOS || platform == TransferUtil.CHANNEL_ANDROID){
            platform = TransferUtil.CHANNEL_ALL;
        }
        String os = TransferUtil.getSensorsOS(platform);
        Integer openTimes;
        Integer productDetail;
        Integer addCard;
        Integer createOrder;
        Integer payOrder;

        if (platform == TransferUtil.CHANNEL_ALL) {
            openTimes = memberFunnelRepository.getMemberFunnelOpenTimesWithAllPlatformForVisitor(beginTime, endTime);
            productDetail = memberFunnelRepository.getMemberFunnelProductDetailWithAllPlatformForVisitor(beginTime, endTime);
            addCard = memberFunnelRepository.getMemberFunnelAddCardWithAllPlatformForVisitor(beginTime, endTime);
            createOrder = memberFunnelRepository.getMemberFunnelCreateOrderWithAllPlatformForVisitor(beginTime, endTime);
            payOrder = memberFunnelRepository.getMemberFunnelPayOrderWithAllPlatformForVisitor(beginTime, endTime);
        }else {
            openTimes = memberFunnelRepository.getMemberFunnelOpenTimesWithSinglePlatformForVisitor(beginTime, endTime,os,platformName);
            productDetail = memberFunnelRepository.getMemberFunnelProductDetailWithSinglePlatformForVisitor(beginTime, endTime,platformName);
            addCard = memberFunnelRepository.getMemberFunnelAddCardWithSinglePlatformForVisitor(beginTime, endTime, platformName);
            createOrder = memberFunnelRepository.getMemberFunnelCreateOrderWithSinglePlatformForVisitor(beginTime, endTime, platformName);
            payOrder = memberFunnelRepository.getMemberFunnelPayOrderWithSinglePlatformForVisitor(beginTime, endTime, platformName);
        }

        BigDecimal openTimesB = new BigDecimal(openTimes);
        BigDecimal productDetailB = new BigDecimal(productDetail);
        BigDecimal addCardB = new BigDecimal(addCard);
        BigDecimal createOrderB = new BigDecimal(createOrder);
        BigDecimal payOrderB = new BigDecimal(payOrder);

        BigDecimal openTimesScale = new BigDecimal(1);
        BigDecimal productDetailScale = productDetailB.divide(openTimesB, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal addCardScale = addCardB.divide(productDetailB, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal createOrderScale = createOrderB.divide(addCardB, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal payOrderScale = payOrderB.divide(createOrderB, 2, BigDecimal.ROUND_HALF_UP);

        System.out.println("openTimesScale:"+openTimesScale);
        System.out.println("productDetailScale:"+productDetailScale);
        System.out.println("addCardScale:"+addCardScale);
        System.out.println("createOrderScale:"+createOrderScale);
        System.out.println("payOrderScale:"+payOrderScale);
        //TODO 返回具体的数据格式
        return null;
    }
    /**
     * 根据注册用户
     * @param target
     * @param platformName
     * @param beginTime
     * @param endTime
     * @return
     */
    private String getMemberFunnelForRegister(String target, String platformName, Timestamp beginTime, Timestamp endTime){
        int platform = TransferUtil.getPlatform(platformName);
        if(platform != TransferUtil.CHANNEL_IOS || platform == TransferUtil.CHANNEL_ANDROID){
            platform = TransferUtil.CHANNEL_ALL;
        }
        String os = TransferUtil.getSensorsOS(platform);
        Integer openTimes;
        Integer productDetail;
        Integer addCard ;
        Integer createOrder;
        Integer payOrder;

        if (platform<0) {
            openTimes = memberFunnelRepository.getMemberFunnelOpenTimesWithAllPlatformForRegister(beginTime, endTime);
            productDetail = memberFunnelRepository.getMemberFunnelProductDetailWithAllPlatformForRegister(beginTime, endTime);
            addCard = memberFunnelRepository.getMemberFunnelAddCardWithAllPlatformForRegister(beginTime, endTime);
            createOrder = memberFunnelRepository.getMemberFunnelCreateOrderWithAllPlatformForRegister(beginTime, endTime);
            payOrder = memberFunnelRepository.getMemberFunnelPayOrderWithAllPlatformForRegister(beginTime, endTime);
        }else {
            openTimes = memberFunnelRepository.getMemberFunnelOpenTimesWithSinglePlatformForRegister(beginTime, endTime,os, platformName);
            productDetail = memberFunnelRepository.getMemberFunnelProductDetailWithSinglePlatformForRegister(beginTime, endTime, platformName);
            addCard = memberFunnelRepository.getMemberFunnelAddCardWithSinglePlatformForRegister(beginTime, endTime, platformName);
            createOrder = memberFunnelRepository.getMemberFunnelCreateOrderWithSinglePlatformForRegister(beginTime, endTime, platformName);
            payOrder = memberFunnelRepository.getMemberFunnelPayOrderWithSinglePlatformForRegister(beginTime, endTime, platformName);
        }

        BigDecimal openTimesB = new BigDecimal(openTimes);
        BigDecimal productDetailB = new BigDecimal(productDetail);
        BigDecimal addCardB = new BigDecimal(addCard);
        BigDecimal createOrderB = new BigDecimal(createOrder);
        BigDecimal payOrderB = new BigDecimal(payOrder);

        BigDecimal openTimesScale = new BigDecimal(1);
        BigDecimal productDetailScale = productDetailB.divide(openTimesB, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal addCardScale = addCardB.divide(productDetailB, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal createOrderScale = createOrderB.divide(addCardB, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal payOrderScale = payOrderB.divide(createOrderB, 2, BigDecimal.ROUND_HALF_UP);
        System.out.println("openTimesScale:"+openTimesScale);
        System.out.println("productDetailScale:"+productDetailScale);
        System.out.println("addCardScale:"+addCardScale);
        System.out.println("createOrderScale:"+createOrderScale);
        System.out.println("payOrderScale:"+payOrderScale);

        //TODO 返回具体的数据格式
        return null;
    }
}
