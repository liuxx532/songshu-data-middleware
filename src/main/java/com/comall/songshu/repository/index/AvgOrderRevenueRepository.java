package com.comall.songshu.repository.index;

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
    @Query(value = "SELECT COALESCE(SUM(r.\"Amount\") / count(DISTINCT o.\"Id\"),0) as result \n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON (o.\"OrderNumber\" = r.\"MergePaymentNo\")\n" +
        "  JOIN songshu_cs_order_payable p ON (o.\"Id\" = p.\"OrderId\")\n" +
        "WHERE (o.\"OrderStatus\" NOT IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?1 AND ?2)\n" +
        "      AND (p.\"PaymentStatus\" = 1) \n" +
        "      AND (o.\"Channel\" IN (0, 1, 2, 3, 5))", nativeQuery = true)
    Double getAvgRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT COALESCE(SUM(r.\"Amount\")/ count(DISTINCT o.\"Id\"),0)  as result \n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON (o.\"OrderNumber\" = r.\"MergePaymentNo\")\n" +
        "  JOIN songshu_cs_order_payable p ON (o.\"Id\" = p.\"OrderId\")\n" +
        "WHERE (o.\"OrderStatus\" NOT IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?2 AND ?3)\n" +
        "      AND (p.\"PaymentStatus\" = 1) \n" +
        "      AND (o.\"Channel\" = ?1)", nativeQuery = true)
    Double getAvgRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);

//SQL
    //SELECT tss.stime AS stime, tss.etime AS etime, sum(comt.Amount) / count(DISTINCT comt.Id) AS result
    // FROM(select r."Amount" as Amount ,r."PaidTime" as PaidTime,o."Id" as Id from songshu_cs_order o
    // JOIN songshu_cs_payment_record r ON o."OrderNumber" = r."MergePaymentNo"
    // JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId" WHERE (o."OrderStatus" NOT IN (6, 7)) AND (r."PaymentModeType" = 2)
    // AND (r."PaidTime" BETWEEN '2015-11-01 00:00:00' AND '2016-01-01 00:00:00') AND (p."PaymentStatus" = 1) AND (o."Channel" = 0)) comt
    // RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + 86400 * INTERVAL '1 second' AS etime
    // FROM (SELECT generate_series('2015-11-01 00:00:00', '2016-01-01 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
    // ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime

    // 单个平台的趋势
    @Query(value = "SELECT tss.stime AS stime, tss.etime AS etime, COALESCE((sum(comt.Amount) / count(DISTINCT comt.Id)),0) AS result " +
        "FROM(select r.\"Amount\" as Amount ,r.\"PaidTime\" as PaidTime,o.\"Id\" as Id from songshu_cs_order o " +
        "JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "WHERE (o.\"OrderStatus\" NOT IN (6, 7)) AND (r.\"PaymentModeType\" = 2) AND (r.\"PaidTime\" " +
        "BETWEEN ?2 AND ?3) AND (p.\"PaymentStatus\" = 1) AND (o.\"Channel\" = ?1)) comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?4 * INTERVAL '1 second' AS etime " +
        "FROM (SELECT generate_series(?2, ?3, ?4 * INTERVAL '1 second')) ts) tss " +
        "ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getAvgRevenueTrendWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime, Integer interval);


    // 所有平台的趋势
    @Query(value = "SELECT tss.stime AS stime, tss.etime AS etime, COALESCE((sum(comt.Amount) / count(DISTINCT comt.Id)),0) AS result " +
        "FROM(select r.\"Amount\" as Amount ,r.\"PaidTime\" as PaidTime,o.\"Id\" as Id from songshu_cs_order o " +
        "JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "WHERE (o.\"OrderStatus\" NOT IN (6, 7)) AND (r.\"PaymentModeType\" = 2) AND (r.\"PaidTime\" " +
        "BETWEEN ?1 AND ?2) AND (p.\"PaymentStatus\" = 1) AND (o.\"Channel\" IN (0, 1, 2, 3, 5))) comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getAvgRevenueTrendWithAllPlatform(Timestamp beginTime, Timestamp endTime, Integer interval);

}
