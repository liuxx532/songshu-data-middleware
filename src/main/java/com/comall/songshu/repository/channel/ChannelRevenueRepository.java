package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * 渠道销售额
 * Created by huanghaizhou on 2017/5/8.
 */
public interface ChannelRevenueRepository  extends JpaRepository<Author,Long> {

    // 所有平台
    @Query(value = "SELECT sum(r.\"Amount\")\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\"\n" +
        "  JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "WHERE (o.\"OrderStatus\" not IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?1 AND ?2)\n" +
        "      AND (p.\"PaymentStatus\" = 1)\n" +
        "      AND (o.\"Channel\" IN (0, 1, 2, 3, 5))", nativeQuery = true)
    Double getChannelRevenueWithAllPlatformAllChannel(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT sum(r.\"Amount\")\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\"\n" +
        "  JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "WHERE (o.\"OrderStatus\" not IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?2 AND ?3)\n" +
        "      AND (p.\"PaymentStatus\" = 1)\n" +
        "      AND (o.\"Channel\" = ?1)", nativeQuery = true)
    Double getChannelRevenueWithSinglePlatformAllChannel(Integer platform, Timestamp beginTime, Timestamp endTime);

    // 所有平台
    @Query(value = "SELECT now()", nativeQuery = true)
    Double getChannelRevenueWithAllPlatformSingleChannel(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT now()", nativeQuery = true)
    Double getChannelRevenueWithSinglePlatformSingleChannel(Integer platform, Timestamp beginTime, Timestamp endTime);
}
