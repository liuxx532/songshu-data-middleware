package com.comall.songshu.repository;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by wdc on 2017/4/19.
 */

// 客单价
public interface AvgOrderRevenueRepository extends JpaRepository<Author, Long> {



// SQL 说明
//    @Query(value = "SELECT sum(r.\"Amount\") / count(DISTINCT o.\"Id\") as result \n" +
//        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON (o.\"OrderNumber\" = r.\"MergePaymentNo\")\n" +
//        "  JOIN songshu_cs_order_payable p ON (o.\"Id\" = p.\"OrderId\")\n" +
//        "WHERE (o.\"OrderStatus\" NOT IN (7, 8)) -- 排除取消，关闭\n" +
//        "      AND (r.\"PaymentModeType\" = 2) -- 外部支付\n" +
//        "      AND (r.\"PaidTime\" BETWEEN '2013-01-06 18:22:50' AND '2017-01-06 18:22:50')  -- 支付时间? 使用订单创建时间？\n" +
//        "--       AND (o.OrderCreateTime BETWEEN '2013-01-06 18:22:50' AND '2017-01-06 18:22:50')\n" +
//        "      AND (p.\"PaymentStatus\" = 1) -- 已支付\n" +
//        "      AND (o.\"Channel\" IN (1, 2, 3, 5)); -- 销售渠道（1:安卓 2:IOS 3:微信 5:WAP", nativeQuery = true)

    // 所有平台
    @Query(value = "SELECT sum(r.\"Amount\") / count(DISTINCT o.\"Id\") as result \n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON (o.\"OrderNumber\" = r.\"MergePaymentNo\")\n" +
        "  JOIN songshu_cs_order_payable p ON (o.\"Id\" = p.\"OrderId\")\n" +
        "WHERE (o.\"OrderStatus\" NOT IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?1 AND ?2)\n" +
        "      AND (p.\"PaymentStatus\" = 1) \n" +
        "      AND (o.\"Channel\" IN (0, 1, 2, 3, 5))", nativeQuery = true)
    Double getAvgRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT sum(r.\"Amount\") / count(DISTINCT o.\"Id\") as result \n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON (o.\"OrderNumber\" = r.\"MergePaymentNo\")\n" +
        "  JOIN songshu_cs_order_payable p ON (o.\"Id\" = p.\"OrderId\")\n" +
        "WHERE (o.\"OrderStatus\" NOT IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?2 AND ?3)\n" +
        "      AND (p.\"PaymentStatus\" = 1) \n" +
        "      AND (o.\"Channel\" = ?1)", nativeQuery = true)
    Double getAvgRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);

    // 单个平台的趋势
    @Query(value = "SELECT tss.stime as stime, tss.etime as etime, sum(r.\"Amount\") as amount\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\"\n" +
        "  JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "  JOIN\n" +
        "  (SELECT ts.generate_series as stime, ts.generate_series + ?4 * interval '1 second' as etime\n" +
        "   FROM (select generate_series(?2, ?3, ?4 * interval '1 second')) ts) tss\n" +
        "WHERE\n" +
        "  (o.\"OrderStatus\" not IN (6, 7))\n" +
        "  AND (r.\"PaymentModeType\" = 2)\n" +
        "  AND (r.\"PaidTime\" BETWEEN ?2 AND ?3)\n" +
        "  AND (p.\"PaymentStatus\" = 1)\n" +
        "  AND (o.\"Channel\" = ?1)\n" +
        "GROUP BY tss.stime, tss.etime\n" +
        "ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getAvgRevenueTrendWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime, Integer interval);


    // 所有平台的趋势
    @Query(value = "SELECT tss.stime as stime, tss.etime as etime, sum(r.\"Amount\") as amount\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\"\n" +
        "  JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "  JOIN\n" +
        "  (SELECT ts.generate_series as stime, ts.generate_series + ?3 * interval '1 second' as etime\n" +
        "   FROM (select generate_series(?1, ?2, ?3 * interval '1 second')) ts) tss\n" +
        "    on (o.\"OrderCreateTime\" < tss.etime AND o.\"OrderCreateTime\" >= tss.stime)\n" +
        "WHERE\n" +
        "  (o.\"OrderStatus\" not IN (6, 7))\n" +
        "  AND (r.\"PaymentModeType\" = 2)\n" +
        "  AND (r.\"PaidTime\" BETWEEN ?1 AND ?2)\n" +
        "  AND (p.\"PaymentStatus\" = 1)\n" +
        "  AND (o.\"Channel\" IN (0, 1, 2, 3, 5))\n" +
        "GROUP BY tss.stime, tss.etime\n" +
        "ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getAvgRevenueTrendWithAllPlatform(Timestamp beginTime, Timestamp endTime, Integer interval);

}
