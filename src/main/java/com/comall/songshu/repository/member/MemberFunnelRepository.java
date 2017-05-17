package com.comall.songshu.repository.member;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * 会员漏斗Repository
 * Created by huanghaizhou on 2017/5/5.
 */
public interface MemberFunnelRepository  extends JpaRepository<Author,Long> {


    /**
     * 会员漏斗图-启动-访客(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='AppLaunch'
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND (e.os ='iOS' OR e.platform ='ios');
    @Query(value = "SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='AppLaunch'   " +
        "AND e.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getMemberFunnelOpenTimesWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);


    /**
     * 会员漏斗图-启动-访客（单平台）
     * @param beginTime
     * @param endTime
     * @param os
     * @param plateFormName
     * @return
     */
    @Query(value = "SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='AppLaunch'   " +
        "AND e.times BETWEEN ?1 AND ?2 AND (e.os =?3 OR e.platform =?4) ", nativeQuery = true)
    Integer getMemberFunnelOpenTimesWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, String os, String plateFormName);

    /**
     * 会员漏斗图-商品详情页-访客(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e
//    WHERE e.event = '$pageview'
//    AND (e.url like '#/tabs/categories/productInfo%'
//        OR e.url like '#/tabs/cart/productInfo%'
//        OR e.url like '#/tabs/index/productInfo%'
//        OR e.url like '#/tabs/user/productInfo%'
//        OR e.url like '#/tabs/integral/integralInfo%')
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00'  ;
    @Query(value = "SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e  " +
        "WHERE e.event = '$pageview'  " +
        "AND (e.url LIKE '#/tabs/categories/productInfo%'  " +
        "OR e.url LIKE '#/tabs/cart/productInfo%'  " +
        "OR e.url LIKE '#/tabs/index/productInfo%'  " +
        "OR e.url LIKE '#/tabs/user/productInfo%'  " +
        "OR e.url LIKE '#/tabs/integral/integralInfo%')  " +
        "AND e.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getMemberFunnelProductDetailWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);


    /**
     * 会员漏斗图-商品详情页-访客(单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e  " +
        "WHERE e.event = '$pageview'  " +
        "AND (e.url LIKE '#/tabs/categories/productInfo%'  " +
        "OR e.url LIKE '#/tabs/cart/productInfo%'  " +
        "OR e.url LIKE '#/tabs/index/productInfo%'  " +
        "OR e.url LIKE '#/tabs/user/productInfo%'  " +
        "OR e.url LIKE '#/tabs/integral/integralInfo%')  " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform = ?3 ", nativeQuery = true)
    Integer getMemberFunnelProductDetailWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, String platform);

    /**
     * 会员漏斗图-加入购物车-访客(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='AddCardEvent' AND e.invokesource = 'productInfo'
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform ='ios';
    @Query(value = "SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='AddCardEvent' AND e.invokesource = 'productInfo' " +
        "AND e.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getMemberFunnelAddCardWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);

    /**
     * 会员漏斗图-加入购物车-访客(单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='AddCardEvent' AND e.invokesource = 'productInfo' " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform =?3 ", nativeQuery = true)
    Integer getMemberFunnelAddCardWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, String platform);

    /**
     * 会员漏斗图-提交订单-访客(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='CreateOrderEvent'
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform ='ios';
    @Query(value = "SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='CreateOrderEvent'  " +
        "AND e.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getMemberFunnelCreateOrderWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);

    /**
     * 会员漏斗图-提交订单-访客(单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='CreateOrderEvent'  " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform =?3 ", nativeQuery = true)
    Integer getMemberFunnelCreateOrderWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, String platform);

    /**
     * 会员漏斗图-支付订单-访客(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='OrderPaymentEvent'
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform ='ios';
    @Query(value = "SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='OrderPaymentEvent'  " +
        "AND e.times BETWEEN ?1 AND ?2  ", nativeQuery = true)
    Integer getMemberFunnelPayOrderWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);

    /**
     * 会员漏斗图-支付订单-访客(单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT count(DISTINCT e.distinct_id) FROM songshu_shence_events e WHERE e.event ='OrderPaymentEvent'  " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform =?3 ", nativeQuery = true)
    Integer getMemberFunnelPayOrderWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, String platform);

    //---------------------------------------------------------------------------

    /**
     * 会员漏斗图-启动-注册用户(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT count(DISTINCT launch.memberId) FROM
//(SELECT e.distinct_id as memberId FROM songshu_shence_events e
//    WHERE e.event ='AppLaunch' AND (e.os ='iOS' OR e.platform ='ios')
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00')launch
//    INNER JOIN
//        (SELECT DISTINCT e.distinct_id as memberId FROM songshu_shence_events e
//            WHERE e.event ='RegisterEvent' AND e.platform ='ios'
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
//    ON launch.memberId = register.memberId;
    @Query(value = "SELECT count(DISTINCT launch.memberId) FROM  " +
        "(SELECT e.distinct_id AS memberId FROM songshu_shence_events e   " +
        "WHERE e.event ='AppLaunch'   " +
        "AND e.times BETWEEN ?1 AND ?2)launch  " +
        "INNER JOIN  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='RegisterEvent'   " +
        "AND e.times BETWEEN ?1 AND ?2) register  " +
        "ON launch.memberId = register.memberId;", nativeQuery = true)
    Integer getMemberFunnelOpenTimesWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);

    /**
     * 会员漏斗图-启动-注册用户(单平台)
     * @param beginTime
     * @param endTime
     * @param os
     * @param plateFormName
     * @return
     */
    @Query(value = "SELECT count(DISTINCT launch.memberId) FROM  " +
        "(SELECT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='AppLaunch' AND (e.os =?3 OR e.platform =?4)  " +
        "AND e.times BETWEEN ?1 AND ?2)launch  " +
        "INNER JOIN  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='RegisterEvent' AND e.platform =?4  " +
        "AND e.times BETWEEN ?1 AND ?2 ) register  " +
        "ON launch.memberId = register.memberId;", nativeQuery = true)
    Integer getMemberFunnelOpenTimesWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, String os, String plateFormName);

    /**
     * 会员漏斗图-商品详情页-注册用户(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT count(DISTINCT pageView.memberId) FROM
//(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e
//    WHERE e.event = '$pageview'
//    AND (e.url like '#/tabs/categories/productInfo%'
//        OR e.url like '#/tabs/cart/productInfo%'
//        OR e.url like '#/tabs/index/productInfo%'
//        OR e.url like '#/tabs/user/productInfo%'
//        OR e.url like '#/tabs/integral/integralInfo%')
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform ='ios')pageView
//    INNER JOIN
//        (SELECT DISTINCT e.distinct_id as memberId FROM songshu_shence_events e
//            WHERE e.event ='RegisterEvent' AND e.platform ='ios'
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
//    ON pageView.memberId = register.memberId;
    @Query(value = "SELECT count(DISTINCT pageView.memberId) FROM  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event = '$pageview'  " +
        "AND (e.url LIKE '#/tabs/categories/productInfo%'  " +
        "OR e.url LIKE '#/tabs/cart/productInfo%'  " +
        "OR e.url LIKE '#/tabs/index/productInfo%'  " +
        "OR e.url LIKE '#/tabs/user/productInfo%'  " +
        "OR e.url LIKE '#/tabs/integral/integralInfo%')  " +
        "AND e.times BETWEEN ?1 AND ?2 )pageView  " +
        "INNER JOIN  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='RegisterEvent'   " +
        "AND e.times BETWEEN ?1 AND ?2) register  " +
        "ON pageView.memberId = register.memberId;", nativeQuery = true)
    Integer getMemberFunnelProductDetailWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);

    /**
     * 会员漏斗图-商品详情页-注册用户(单平台)
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT count(DISTINCT pageView.memberId) FROM  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event = '$pageview'  " +
        "AND (e.url LIKE '#/tabs/categories/productInfo%'  " +
        "OR e.url LIKE '#/tabs/cart/productInfo%'  " +
        "OR e.url LIKE '#/tabs/index/productInfo%'  " +
        "OR e.url LIKE '#/tabs/user/productInfo%'  " +
        "OR e.url LIKE '#/tabs/integral/integralInfo%')  " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform = ?3 )pageView  " +
        "INNER JOIN  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='RegisterEvent' AND e.platform =?3   " +
        "AND e.times BETWEEN ?1 AND ?2 ) register  " +
        "ON pageView.memberId = register.memberId;", nativeQuery = true)
    Integer getMemberFunnelProductDetailWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, String platform);

    /**
     * 会员漏斗图-加入购物车-注册用户(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT count(DISTINCT addCart.memberId) FROM
//(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e WHERE e.event ='AddCardEvent' AND e.invokesource = 'productInfo'
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform ='ios')addCart
//    INNER JOIN
//        (SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e
//            WHERE e.event ='RegisterEvent' AND  e.platform ='ios'
//            AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
//    ON addCart.memberId = register.memberId;
    @Query(value = "SELECT count(DISTINCT addCart.memberId) FROM  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e WHERE e.event ='AddCardEvent' AND e.invokesource = 'productInfo' " +
        "AND e.times BETWEEN ?1 AND ?2 )addCart  " +
        "INNER JOIN  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='RegisterEvent'   " +
        "AND e.times BETWEEN ?1 AND ?2) register  " +
        "ON addCart.memberId = register.memberId;", nativeQuery = true)
    Integer getMemberFunnelAddCardWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);

    /**
     * 会员漏斗图-加入购物车-注册用户(单平台)
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT count(DISTINCT addCart.memberId) FROM  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e WHERE e.event ='AddCardEvent' AND e.invokesource = 'productInfo' " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform =?3)addCart  " +
        "INNER JOIN  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='RegisterEvent' AND  e.platform =?3  " +
        "AND e.times BETWEEN ?1 AND ?2) register  " +
        "ON addCart.memberId = register.memberId;", nativeQuery = true)
    Integer getMemberFunnelAddCardWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, String platform);

    /**
     * 会员漏斗图-提交订单-注册用户(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT count(DISTINCT createOrder.memberId) FROM
//(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e WHERE e.event ='CreateOrderEvent'
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform ='ios')createOrder
//    INNER JOIN
//        (SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e
//            WHERE e.event ='RegisterEvent' AND e.platform ='ios'
//            AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
//    ON createOrder.memberId = register.memberId;
    @Query(value = "SELECT count(DISTINCT createOrder.memberId) FROM  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e WHERE e.event ='CreateOrderEvent'  " +
        "AND e.times BETWEEN ?1 AND ?2 ) createOrder  " +
        "INNER JOIN  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='RegisterEvent'  " +
        "AND e.times BETWEEN ?1 AND ?2) register  " +
        "ON createOrder.memberId = register.memberId;", nativeQuery = true)
    Integer getMemberFunnelCreateOrderWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);


    /**
     * 会员漏斗图-提交订单-注册用户(单平台)
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT count(DISTINCT createOrder.memberId) FROM  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e WHERE e.event ='CreateOrderEvent'  " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform =?3 )createOrder  " +
        "INNER JOIN  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='RegisterEvent' AND e.platform =?3   " +
        "AND e.times BETWEEN ?1 AND ?2) register  " +
        "ON createOrder.memberId = register.memberId;", nativeQuery = true)
    Integer getMemberFunnelCreateOrderWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, String platform);

    /**
     * 会员漏斗图-支付订单-注册用户(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT count(DISTINCT orderPayment.memberId) FROM
//(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e WHERE e.event ='OrderPaymentEvent'
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform ='ios')orderPayment
//    INNER JOIN
//        (SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e
//            WHERE e.event ='RegisterEvent' AND   e.platform ='ios'
//            AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
//    ON orderPayment.memberId = register.memberId;
    @Query(value = "SELECT count(DISTINCT orderPayment.memberId) FROM  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e WHERE e.event ='OrderPaymentEvent'  " +
        "AND e.times BETWEEN ?1 AND ?2)orderPayment  " +
        "INNER JOIN  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='RegisterEvent'   " +
        "AND e.times BETWEEN ?1 AND ?2) register  " +
        "ON orderPayment.memberId = register.memberId;", nativeQuery = true)
    Integer getMemberFunnelPayOrderWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);


    /**
     * 会员漏斗图-支付订单-注册用户(单平台)
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT count(DISTINCT orderPayment.memberId) FROM  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e WHERE e.event ='OrderPaymentEvent'  " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform =?3)orderPayment  " +
        "INNER JOIN  " +
        "(SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e  " +
        "WHERE e.event ='RegisterEvent' AND e.platform = ?3  " +
        "AND e.times BETWEEN ?1 AND ?2) register  " +
        "ON orderPayment.memberId = register.memberId", nativeQuery = true)
    Integer getMemberFunnelPayOrderWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, String platform);
}
