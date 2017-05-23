package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * Created by liugaoyu on 2017/4/20.
 * 渠道订单量
 */
public interface ChannelOrderCountRepository extends JpaRepository<Author,Long> {

    /**
     * 全渠道订单量(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT o.\"Id\") " +
        "FROM songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "WHERE o.\"OrderStatus\" not IN (6,7) AND p.\"PaymentStatus\" = 1 " +
        "AND cpr.paidTime BETWEEN ?1 AND ?2", nativeQuery = true)
    Double getChannelOrderCountWithAllPlatformAllChannel(Timestamp beginTime, Timestamp endTime);

    /**
     * 全渠道订单量(单平台)
     * @param platform
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT o.\"Id\") " +
        "FROM songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?2 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?3 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "WHERE o.\"OrderStatus\" not IN (6,7) AND p.\"PaymentStatus\" = 1 " +
        "AND cpr.paidTime BETWEEN ?2 AND ?3 AND o.\"Channel\" = ?1", nativeQuery = true)
    Double getChannelOrderCountWithSinglePlatformAllChannel(Integer platform, Timestamp beginTime, Timestamp endTime);

    /**
     * 单渠道订单量(全平台)
     * @param beginTime
     * @param endTime
     * @param channelName
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT o.\"Id\") " +
        "FROM songshu_cs_order o " +
        "    INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "    INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime,SUM(\"Amount\") AS amount FROM (SELECT * FROM songshu_cs_payment_record " +
        "               WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "               GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "    LEFT JOIN(SELECT mem.\"id\" AS memberId, " +
        "                     CASE WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source) " +
        "                     WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO' " +
        "                     WHEN mem.\"multipleChannelsId\" = 2 THEN 'APPLESTORE' " +
        "                     WHEN mem.\"multipleChannelsId\" = 3 THEN 'WEIXIN' " +
        "                     WHEN mem.\"multipleChannelsId\" = 5 THEN 'WAP' " +
        "                     ELSE 'WAP' END AS utm_source " +
        "              FROM songshu_cs_member mem " +
        "                  LEFT JOIN songshu_shence_users u ON u.second_id = mem.\"id\" " +
        "             ) tsource ON o.\"MemberId\"=tsource.memberId " +
        "WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1 " +
        "AND  cpr.paidTime BETWEEN ?1 AND ?2 AND  tsource.utm_source= ?3 ", nativeQuery = true)
    Double getChannelOrderCountWithAllPlatformSingleChannel(Timestamp beginTime, Timestamp endTime,String channelName);

    /**
     * 单渠道订单量(单平台)
     * @param platform
     * @param beginTime
     * @param endTime
     * @param channelName
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT o.\"Id\") " +
        "FROM songshu_cs_order o " +
        "    INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "    INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime,SUM(\"Amount\") AS amount FROM (SELECT * FROM songshu_cs_payment_record " +
        "               WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?2 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?3 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "               GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "    LEFT JOIN(SELECT mem.\"id\" AS memberId, " +
        "                     CASE WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source) " +
        "                     WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO' " +
        "                     WHEN mem.\"multipleChannelsId\" = 2 THEN 'APPLESTORE' " +
        "                     WHEN mem.\"multipleChannelsId\" = 3 THEN 'WEIXIN' " +
        "                     WHEN mem.\"multipleChannelsId\" = 5 THEN 'WAP' " +
        "                     ELSE 'WAP' END AS utm_source " +
        "              FROM songshu_cs_member mem " +
        "                  LEFT JOIN songshu_shence_users u ON u.second_id = mem.\"id\" " +
        "             ) tsource ON o.\"MemberId\"=tsource.memberId " +
        "WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1 " +
        "AND  cpr.paidTime BETWEEN ?2 AND ?3 AND o.\"Channel\" = ?1 AND tsource.utm_source= ?4 ", nativeQuery = true)
    Double getChannelOrderCountWithSinglePlatformSingleChannel(Integer platform, Timestamp beginTime, Timestamp endTime,String channelName);


}
