package com.comall.songshu.repository.index;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by liugaoyu on 2017/4/20.
 * 订单量
 */
public interface OrderCountRepository extends JpaRepository<Author,Long> {

// SQL

//    SELECT count(DISTINCT o."Id")
//    FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o."OrderNumber" = r."MergePaymentNo"
//    JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
//    WHERE (o."OrderStatus" not IN (7, 8)) -- 排除取消，关闭
//    AND (r."PaymentModeType" = 2) -- 外部支付
//    AND (r."PaidTime" BETWEEN '2013-01-06 18:22:50' AND '2017-01-06 18:22:50')  -- 支付时间? 使用订单创建时间？
//        --       AND (o."OrderCreateTime" BETWEEN '2013-01-06 18:22:50' AND '2017-01-06 18:22:50')
//    AND (p."PaymentStatus" = 1) -- 已支付
//    AND (o."Channel" IN (0, 1, 2, 3, 5));

    // 所有平台
    @Query(value = "SELECT count(DISTINCT o.\"Id\")\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\"\n" +
        "  JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "WHERE (o.\"OrderStatus\" not IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?1 AND ?2)\n" +
        "      AND (p.\"PaymentStatus\" = 1)\n" +
        "      AND (o.\"Channel\" IN (0, 1, 2, 3, 5))", nativeQuery = true)
    Double getOrderCountWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT count(DISTINCT o.\"Id\")\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\"\n" +
        "  JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "WHERE (o.\"OrderStatus\" not IN (6, 7))\n" +
        "      AND (r.\"PaymentModeType\" = 2)\n" +
        "      AND (r.\"PaidTime\" BETWEEN ?2 AND ?3)\n" +
        "      AND (p.\"PaymentStatus\" = 1)\n" +
        "      AND (o.\"Channel\" = ?1)", nativeQuery = true)
    Double getOrderCountWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);

    // TODO and trend

    //趋势图



    /**
     *
     * @param platform
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     *
     */
//SQL
    //SELECT tss.stime AS stime, tss.etime AS etime, count(DISTINCT comt.Id) AS result
    // FROM(select r."PaidTime" as PaidTime ,o."Id" as Id from songshu_cs_order o
    // JOIN songshu_cs_payment_record r ON o."OrderNumber" = r."MergePaymentNo"
    // JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId" WHERE (o."OrderStatus" NOT IN (6, 7)) AND (r."PaymentModeType" = 2)
    // AND (r."PaidTime" BETWEEN '2015-11-01 00:00:00' AND '2016-01-01 00:00:00') AND (p."PaymentStatus" = 1) AND (o."Channel" = 0))comt
    // RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + 86400 * INTERVAL '1 second' AS etime
    // FROM (SELECT generate_series('2015-11-01 00:00:00', '2016-01-01 00:00:00', 86400 * INTERVAL '1 second')) ts) tss ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime)
    // GROUP BY tss.stime, tss.etime ORDER BY tss.stime

    //单个平台趋势图
    @Query(value = "SELECT tss.stime AS stime, tss.etime AS etime, count(DISTINCT comt.Id) AS result " +
        "FROM(select r.\"PaidTime\" as PaidTime ,o.\"Id\" as Id from songshu_cs_order o " +
        "JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" " +
        "JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "WHERE (o.\"OrderStatus\" NOT IN (6, 7)) AND (r.\"PaymentModeType\" = 2) AND (r.\"PaidTime\" " +
        "BETWEEN ?2 AND ?3) AND (p.\"PaymentStatus\" = 1) AND (o.\"Channel\" = ?1))comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?4 * INTERVAL '1 second' AS etime " +
        "FROM (SELECT generate_series(?2, ?3, ?4 * INTERVAL '1 second')) ts) tss " +
        "ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getOrderCounTrendtWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime, Integer interval);


    // 所有平台趋势图
    @Query(value = "SELECT tss.stime AS stime, tss.etime AS etime, count(DISTINCT comt.Id) AS result " +
        "FROM(select r.\"PaidTime\" as PaidTime ,o.\"Id\" as Id from songshu_cs_order o " +
        "JOIN songshu_cs_payment_record r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" " +
        "JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "WHERE (o.\"OrderStatus\" NOT IN (6, 7)) AND (r.\"PaymentModeType\" = 2) AND (r.\"PaidTime\" " +
        "BETWEEN ?1 AND ?2) AND (p.\"PaymentStatus\" = 1) AND (o.\"Channel\" IN (0, 1, 2, 3, 5) ))comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getOrderCountTrendWithAllPlatform (Timestamp beginTime, Timestamp endTime, Integer interval);

}
