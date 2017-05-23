package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 渠道页面消费用户数
 *
 * @author liushengling
 * @create 2017-05-08-11:53
 **/
public interface ChannelConsumerCountRepository extends JpaRepository<Author,Long> {

    /**
     * 全渠道消费用户数（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT o.\"MemberId\") AS  memberCount " +
        "FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "AND cpr.paidTime BETWEEN ?1 AND ?2", nativeQuery = true)
    Double getChannelConsumerCountWithAllPlatformAllChannel(Timestamp beginTime, Timestamp endTime);

    /**
     * 全渠道消费用户数（单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT o.\"MemberId\") AS  memberCount " +
        "FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "AND cpr.paidTime BETWEEN ?1 AND ?2 AND o.\"Channel\" = ?3", nativeQuery = true)
    Double getChannelConsumerCountSinglePlatformAllChannel(Timestamp beginTime, Timestamp endTime, Integer platform);

    /**
     * 单渠道消费用户数（全平台）
     * @param beginTime
     * @param endTime
     * @param channelName
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT o.\"MemberId\") AS memberCount FROM songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "LEFT JOIN(SELECT mem.\"id\" AS memberId, " +
        "             CASE WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source) " +
        "             WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO' " +
        "             WHEN mem.\"multipleChannelsId\" = 2 THEN 'APPLESTORE' " +
        "             WHEN mem.\"multipleChannelsId\" = 3 THEN 'WEIXIN' " +
        "             WHEN mem.\"multipleChannelsId\" = 5 THEN 'WAP' " +
        "             ELSE 'WAP' END AS utm_source " +
        "             FROM songshu_cs_member mem " +
        "             LEFT JOIN songshu_shence_users u ON u.second_id = mem.\"id\" " +
        "         ) tsource ON o.\"MemberId\"=tsource.memberId " +
        "WHERE o.\"OrderStatus\" NOT IN (6, 7) AND op.\"PaymentStatus\" = 1 " +
        "AND  cpr.paidTime BETWEEN ?1 AND ?2  AND tsource.utm_source= ?3", nativeQuery = true)
    Double getChannelConsumerCountWithAllPlatformSingleChannel(Timestamp beginTime, Timestamp endTime,String channelName);

    /**
     * 单渠道消费用户数（单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @param channelName
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT o.\"MemberId\") AS memberCount FROM songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "LEFT JOIN(SELECT mem.\"id\" AS memberId, " +
        "             CASE WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source) " +
        "             WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO' " +
        "             WHEN mem.\"multipleChannelsId\" = 2 THEN 'APPLESTORE' " +
        "             WHEN mem.\"multipleChannelsId\" = 3 THEN 'WEIXIN' " +
        "             WHEN mem.\"multipleChannelsId\" = 5 THEN 'WAP' " +
        "             ELSE 'WAP' END AS utm_source " +
        "             FROM songshu_cs_member mem " +
        "             LEFT JOIN songshu_shence_users u ON u.second_id = mem.\"id\" " +
        "         ) tsource ON o.\"MemberId\"=tsource.memberId " +
        "WHERE o.\"OrderStatus\" NOT IN (6, 7) AND op.\"PaymentStatus\" = 1 " +
        "AND  cpr.paidTime BETWEEN ?1 AND ?2 AND o.\"Channel\" = ?3 AND tsource.utm_source= ?4", nativeQuery = true)
    Double getChannelConsumerCountSinglePlatformSingleChannel(Timestamp beginTime, Timestamp endTime, Integer platform,String channelName);



}
