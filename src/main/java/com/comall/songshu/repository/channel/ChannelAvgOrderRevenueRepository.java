package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * Created by wdc on 2017/4/19.
 */

// 渠道客单价
public interface ChannelAvgOrderRevenueRepository extends JpaRepository<Author, Long> {




    // 所有平台
    @Query(value = "SELECT COALESCE(SUM(r.\"Amount\") / count(DISTINCT o.\"Id\"),0) as result \n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON (o.\"OrderNumber\" = r.\"MergePaymentNo\")\n" +
        "  JOIN songshu_cs_order_payable p ON (o.\"Id\" = p.\"OrderId\")\n" +
        "WHERE (o.\"OrderStatus\" NOT IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?1 AND ?2)\n" +
        "      AND (p.\"PaymentStatus\" = 1) \n" +
        "      AND (o.\"Channel\" IN (0, 1, 2, 3, 5))", nativeQuery = true)
    Double getChannelAvgRevenueWithAllPlatformAllChannel(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT COALESCE(SUM(r.\"Amount\")/ count(DISTINCT o.\"Id\"),0)  as result \n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON (o.\"OrderNumber\" = r.\"MergePaymentNo\")\n" +
        "  JOIN songshu_cs_order_payable p ON (o.\"Id\" = p.\"OrderId\")\n" +
        "WHERE (o.\"OrderStatus\" NOT IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?2 AND ?3)\n" +
        "      AND (p.\"PaymentStatus\" = 1) \n" +
        "      AND (o.\"Channel\" = ?1)", nativeQuery = true)
    Double getChannelAvgRevenueWithSinglePlatformAllChannel(Integer platform, Timestamp beginTime, Timestamp endTime);

    //SQL
    //SELECT COALESCE(SUM(r."Amount") / count(DISTINCT o."Id"), 0) AS result FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON(o."OrderNumber" = r."MergePaymentNo") JOIN songshu_cs_order_payable p ON (o."Id" = p."OrderId") LEFT JOIN (SELECT mem."id" AS memberId, CASE WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source) WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO' WHEN mem."multipleChannelsId" = 2 THEN 'APPLESTORE' WHEN mem."multipleChannelsId" = 3 THEN 'WEIXIN' WHEN mem."multipleChannelsId" = 5 THEN 'WAP' ELSE 'WAP' END AS utm_source FROM songshu_cs_member mem LEFT JOIN songshu_shence_users u ON u.second_id = mem."id") tsource ON o."MemberId" = tsource.memberId WHERE (o."OrderStatus" NOT IN (6, 7)) AND (r."PaymentModeType" = 2) AND (r."PaidTime" BETWEEN '2016-01-01 00:00:00' AND '2017-02-01 00:00:00') AND (p."PaymentStatus" = 1) AND (o."Channel" IN (0, 1, 2, 3, 5)) AND tsource.utm_source = 'APPLESTORE';

    @Query(value = "SELECT COALESCE(SUM(r.\"Amount\") / count(DISTINCT o.\"Id\"), 0) AS result " +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON(o.\"OrderNumber\" = r.\"MergePaymentNo\") " +
        "JOIN songshu_cs_order_payable p ON (o.\"Id\" = p.\"OrderId\") LEFT JOIN (SELECT mem.\"id\" AS memberId, " +
        "CASE WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source) " +
        "WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO' WHEN mem.\"multipleChannelsId\" = 2 " +
        "THEN 'APPLESTORE' WHEN mem.\"multipleChannelsId\" = 3 THEN 'WEIXIN' WHEN mem.\"multipleChannelsId\" = 5 " +
        "THEN 'WAP' ELSE 'WAP' END AS utm_source FROM songshu_cs_member mem LEFT JOIN songshu_shence_users u ON u.second_id = mem.\"id\") tsource " +
        "ON o.\"MemberId\" = tsource.memberId WHERE (o.\"OrderStatus\" NOT IN (6, 7)) AND (r.\"PaymentModeType\" = 2) " +
        "AND (r.\"PaidTime\" BETWEEN ?1  AND ?2 ) AND (p.\"PaymentStatus\" = 1) " +
        "AND (o.\"Channel\" IN (0, 1, 2, 3, 5)) AND tsource.utm_source = ?3 ;", nativeQuery = true)
    Double getChannelAvgRevenueWithAllPlatformSingleChannel(Timestamp beginTime, Timestamp endTime,String channelName);

    @Query(value = "SELECT COALESCE(SUM(r.\"Amount\") / count(DISTINCT o.\"Id\"), 0) AS result " +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON(o.\"OrderNumber\" = r.\"MergePaymentNo\") " +
        "JOIN songshu_cs_order_payable p ON (o.\"Id\" = p.\"OrderId\") LEFT JOIN (SELECT mem.\"id\" AS memberId, " +
        "CASE WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source) " +
        "WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO' WHEN mem.\"multipleChannelsId\" = 2 " +
        "THEN 'APPLESTORE' WHEN mem.\"multipleChannelsId\" = 3 THEN 'WEIXIN' WHEN mem.\"multipleChannelsId\" = 5 " +
        "THEN 'WAP' ELSE 'WAP' END AS utm_source FROM songshu_cs_member mem LEFT JOIN songshu_shence_users u ON u.second_id = mem.\"id\") tsource " +
        "ON o.\"MemberId\" = tsource.memberId WHERE (o.\"OrderStatus\" NOT IN (6, 7)) AND (r.\"PaymentModeType\" = 2) " +
        "AND (r.\"PaidTime\" BETWEEN ?2  AND ?3 ) AND (p.\"PaymentStatus\" = 1) " +
        "AND (o.\"Channel\" = ?1 ) AND tsource.utm_source = ?4 ;", nativeQuery = true)
    Double getChannelAvgRevenueWithSinglePlatformSingleChannel(Integer platform, Timestamp beginTime, Timestamp endTime,String channelName);


}
