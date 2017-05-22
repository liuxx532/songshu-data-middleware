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


    /**
     * 支付订单量统计（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT o.\"Id\") FROM songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1  " +
        "AND cpr.paidTime BETWEEN ?1 AND ?2", nativeQuery = true)
    Double getOrderCountWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    /**
     * 支付订单量统计（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT o.\"Id\") FROM songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?2 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?3 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1 AND o.\"Channel\" = ?1 " +
        "AND cpr.paidTime BETWEEN ?2 AND ?3", nativeQuery = true)
    Double getOrderCountWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);



    /**
     * 支付订单量趋势图统计（全平台）
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT tss.stime AS stime, tss.etime AS etime, COUNT(DISTINCT comt.Id) AS result " +
        "FROM(SELECT cpr.paidTime,o.\"Id\" AS Id FROM songshu_cs_order o " +
        "     INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "     INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "                 WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                 GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "     WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1  " +
        "     AND cpr.paidTime BETWEEN ?1 AND ?2 " +
        "    )comt " +
        "RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "            FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "            ON (comt.paidTime < tss.etime AND comt.paidTime >= tss.stime) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getOrderCountTrendWithAllPlatform (Timestamp beginTime, Timestamp endTime, Integer interval);


    /**
     * 支付订单量趋势图统计（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT tss.stime AS stime, tss.etime AS etime, COUNT(DISTINCT comt.Id) AS result " +
        "FROM(SELECT cpr.paidTime,o.\"Id\" AS Id FROM songshu_cs_order o " +
        "     INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "     INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "                 WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?2 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?3 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                 GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "     WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1 AND o.\"Channel\" = ?1 " +
        "     AND cpr.paidTime BETWEEN ?2 AND ?3 " +
        "    )comt " +
        "RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + ?4 * INTERVAL '1 second' AS etime " +
        "            FROM (SELECT generate_series(?2, ?3, ?4 * INTERVAL '1 second')) ts) tss " +
        "            ON (comt.paidTime < tss.etime AND comt.paidTime >= tss.stime) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getOrderCountTrendWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime, Integer interval);
}
