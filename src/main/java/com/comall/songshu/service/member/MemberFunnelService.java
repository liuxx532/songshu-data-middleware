package com.comall.songshu.service.member;

import com.comall.songshu.repository.member.MemberFunnelRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
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
            List<Integer[]> queryResult = memberFunnelRepository.getMemberFunnelWithAllPlatformForVisitor(beginTime, endTime);
            openTimes = queryResult.get(0)[0];
            productDetail = queryResult.get(0)[1];
            addCard = queryResult.get(0)[2];
            createOrder = queryResult.get(0)[3];
            payOrder = queryResult.get(0)[4];
        }else {
            List<Integer[]> queryResult = memberFunnelRepository.getMemberFunnelWithSinglePlatformForVisitor(beginTime,endTime,os,platformName);
            openTimes = queryResult.get(0)[0];
            productDetail = queryResult.get(0)[1];
            addCard = queryResult.get(0)[2];
            createOrder = queryResult.get(0)[3];
            payOrder = queryResult.get(0)[4];
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
            List<Integer[]> queryResult = memberFunnelRepository.getMemberFunnelWithAllPlatformForRegister(beginTime, endTime);
            openTimes = queryResult.get(0)[0];
            productDetail = queryResult.get(0)[1];
            addCard = queryResult.get(0)[2];
            createOrder = queryResult.get(0)[3];
            payOrder = queryResult.get(0)[4];
        }else {
            List<Integer[]> queryResult = memberFunnelRepository.getMemberFunnelWithSinglePlatformForRegister(beginTime,endTime,os,platformName);
            openTimes = queryResult.get(0)[0];
            productDetail = queryResult.get(0)[1];
            addCard = queryResult.get(0)[2];
            createOrder = queryResult.get(0)[3];
            payOrder = queryResult.get(0)[4];
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
