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

    //所有平台

    /**
     * 会员漏斗图-启动-访客
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelOpenTimesWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);
    /**
     * 会员漏斗图-商品详情页-访客
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelProductDetailWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);
    /**
     * 会员漏斗图-加入购物车-访客
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelAddCardWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);
    /**
     * 会员漏斗图-提交订单-访客
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelCreateOrderWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);
    /**
     * 会员漏斗图-支付订单-访客
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelPayOrderWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);

    //---------------------------------------------------------------------------

    //单个平台

    /**
     * 会员漏斗图-启动-访客
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelOpenTimesWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, Integer platform);
    /**
     * 会员漏斗图-商品详情页-访客
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelProductDetailWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, Integer platform);
    /**
     * 会员漏斗图-加入购物车-访客
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelAddCardWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, Integer platform);
    /**
     * 会员漏斗图-提交订单-访客
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelCreateOrderWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, Integer platform);
    /**
     * 会员漏斗图-支付订单-访客
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelPayOrderWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, Integer platform);

    //---------------------------------------------------------------------------

    //所有平台

    /**
     * 会员漏斗图-启动-注册用户
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelOpenTimesWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);
    /**
     * 会员漏斗图-商品详情页-注册用户
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelProductDetailWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);
    /**
     * 会员漏斗图-加入购物车-注册用户
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelAddCardWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);
    /**
     * 会员漏斗图-提交订单-注册用户
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelCreateOrderWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);
    /**
     * 会员漏斗图-支付订单-注册用户
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelPayOrderWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);
    //---------------------------------------------------------------------------

    //单个平台

    /**
     * 会员漏斗图-启动-注册用户
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelOpenTimesWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, Integer platform);
    /**
     * 会员漏斗图-商品详情页-注册用户
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelProductDetailWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, Integer platform);
    /**
     * 会员漏斗图-加入购物车-注册用户
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelAddCardWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, Integer platform);
    /**
     * 会员漏斗图-提交订单-注册用户
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelCreateOrderWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, Integer platform);
    /**
     * 会员漏斗图-支付订单-注册用户
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberFunnelPayOrderWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, Integer platform);
}
