package com.comall.songshu.repository;


import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;


/**
 * Created by lgx on 17/4/18.
 */
// 销售额
public interface RevenueRepository extends JpaRepository<Author, Long> {
// SQL
//    SELECT sum(r."Amount")
//    FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o."OrderNumber" = r."MergePaymentNo"
//    JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
//    WHERE (o."OrderStatus" not IN (7, 8)) -- 排除取消，关闭
//    AND (r."PaymentModeType" = 2) -- 外部支付
//    AND (r."PaidTime" BETWEEN '2013-01-06 18:22:50' AND '2017-01-06 18:22:50')  -- 支付时间? 使用订单创建时间？
//     --  AND (o."OrderCreateTime" BETWEEN '2013-01-06 18:22:50' AND '2017-01-06 18:22:50')
//    AND (p."PaymentStatus" = 1) -- 已支付
//    AND (o."Channel" IN (0, 1, 2, 3, 5)); -- 销售渠道（1:安卓 2:IOS 3:微信 5:WAP）

    // 所有平台（5 个）
    @Query(value = "SELECT sum(r.\"Amount\")\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\"\n" +
        "  JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "WHERE (o.\"OrderStatus\" not IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?1 AND ?2)\n" +
        "      AND (p.\"PaymentStatus\" = 1)\n" +
        "      AND (o.\"Channel\" IN (0, 1, 2, 3, 5))", nativeQuery = true)
    Double getRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT sum(r.\"Amount\")\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\"\n" +
        "  JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "WHERE (o.\"OrderStatus\" not IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?2 AND ?3)\n" +
        "      AND (p.\"PaymentStatus\" = 1)\n" +
        "      AND (o.\"Channel\" = ?1)", nativeQuery = true)
    Double getRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);


    // TODO trend

    // 所有平台趋势
    @Query(value = "SELECT tss.stime as stime, tss.etime as etime, sum(r.\"Amount\")\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\"\n" +
        "  JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "  JOIN\n" +
        "  (SELECT ts.generate_series as stime, ts.generate_series + ?3 * interval '1 second' as etime\n" +
        "   FROM (select generate_series(?1, ?2, ?3 * interval '1 second')) ts) tss\n" +
        "    on (r.\"PaidTime\" < tss.etime AND r.\"PaidTime\" >= tss.stime)\n" +
        "WHERE (o.\"OrderStatus\" not IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?1 AND ?2)\n" +
        "      AND (p.\"PaymentStatus\" = 1)\n" +
        "      AND (o.\"Channel\" IN (0, 1, 2, 3, 5))\n" +
        "GROUP BY tss.stime, tss.etime\n" +
        "ORDER BY tss.stime", nativeQuery = true)

    List<Object[]> getRevenueTrendWithAllPlatform(Timestamp beginTime, Timestamp endTime, Integer interval);


    // 单个平台趋势
    @Query(value = "SELECT tss.stime as stime, tss.etime as etime, sum(r.\"Amount\")\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\"\n" +
        "  JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "  JOIN\n" +
        "  (SELECT ts.generate_series as stime, ts.generate_series + ?4 * interval '1 second' as etime\n" +
        "   FROM (select generate_series(?2, ?3, ?4 * interval '1 second')) ts) tss\n" +
        "    on (r.\"PaidTime\" < tss.etime AND r.\"PaidTime\" >= tss.stime)\n" +
        "WHERE (o.\"OrderStatus\" not IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?2 AND ?3)\n" +
        "      AND (p.\"PaymentStatus\" = 1)\n" +
        "      AND (o.\"Channel\" = ?1)\n"+
        "GROUP BY tss.stime, tss.etime\n" +
        "ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getRevenueTrendWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime, Integer interval);


}
