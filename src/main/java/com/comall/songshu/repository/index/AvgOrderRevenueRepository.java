package com.comall.songshu.repository.index;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by wdc on 2017/4/19.
 * 客单价
 */
public interface AvgOrderRevenueRepository extends JpaRepository<Author, Long> {


    /**
     * 平均客单价（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COALESCE(SUM(r.amount) / COUNT(DISTINCT o.\"Id\"), 0) AS result " +
        "FROM songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime, SUM(pr.\"Amount\") AS amount FROM (select * from songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\"  " +
        "            BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") r ON p.\"MergePaymentId\" = r.\"MergePaymentNo\" " +
        "WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1 " +
        "AND (r.paidTime BETWEEN ?1 AND ?2)", nativeQuery = true)
    Double getAvgRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 平均客单价（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COALESCE(SUM(r.amount) / COUNT(DISTINCT o.\"Id\"), 0) AS result " +
        "FROM songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime, SUM(pr.\"Amount\") AS amount FROM (select * from songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\"  " +
        "            BETWEEN (CAST(?2 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?3 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") r ON p.\"MergePaymentId\" = r.\"MergePaymentNo\" " +
        "WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1 " +
        "AND (r.paidTime BETWEEN ?2 AND ?3) AND o.\"Channel\" = ?1", nativeQuery = true)
    Double getAvgRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);

    /**
     * 平均客单价趋势图（全平台）
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT tss.stime AS stime, tss.etime AS etime, COALESCE((SUM(comt.Amount) / COUNT(DISTINCT comt.Id)),0) AS result " +
        "FROM(SELECT r.amount AS Amount ,r.paidTime AS PaidTime,o.\"Id\" AS Id " +
        "     FROM songshu_cs_order o " +
        "         INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "         INNER JOIN (select pr.\"MergePaymentNo\",MAX(pr.\"PaidTime\") AS paidTime,SUM(pr.\"Amount\") AS amount from (select * from songshu_cs_payment_record WHERE \"PaymentModeType\" =2 " +
        "                     AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 day') AND  (CAST(?2 AS TIMESTAMP) + INTERVAL '1 day')) pr " +
        "         GROUP BY \"MergePaymentNo\",\"PaymentModeType\") r ON p.\"MergePaymentId\" = r.\"MergePaymentNo\" " +
        "     WHERE o.\"OrderStatus\" NOT IN (6,7) AND p.\"PaymentStatus\" = 1 " +
        "     AND (r.paidTime BETWEEN ?1 AND ?2)  " +
        "    )comt " +
        "    RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "                FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts " +
        "               ) tss ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getAvgRevenueTrendWithAllPlatform(Timestamp beginTime, Timestamp endTime, Integer interval);

    /**
     * 平均客单价趋势图（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT tss.stime AS stime, tss.etime AS etime, COALESCE((SUM(comt.Amount) / COUNT(DISTINCT comt.Id)),0) AS result " +
        "FROM(SELECT r.amount AS Amount ,r.paidTime AS PaidTime,o.\"Id\" AS Id " +
        "     FROM songshu_cs_order o " +
        "         INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "         INNER JOIN (select pr.\"MergePaymentNo\",MAX(pr.\"PaidTime\") AS paidTime,SUM(pr.\"Amount\") AS amount from (select * from songshu_cs_payment_record WHERE \"PaymentModeType\" =2 " +
        "                     AND \"PaidTime\" BETWEEN (CAST(?2 AS TIMESTAMP) - INTERVAL '1 day') AND  (CAST(?3 AS TIMESTAMP) + INTERVAL '1 day')) pr " +
        "         GROUP BY \"MergePaymentNo\",\"PaymentModeType\") r ON p.\"MergePaymentId\" = r.\"MergePaymentNo\" " +
        "     WHERE o.\"OrderStatus\" NOT IN (6,7) AND p.\"PaymentStatus\" = 1 " +
        "     AND (r.paidTime BETWEEN ?2 AND ?3) AND o.\"Channel\" = ?1  " +
        "    )comt " +
        "    RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?4 * INTERVAL '1 second' AS etime " +
        "                FROM (SELECT generate_series(?2, ?3, ?4 * INTERVAL '1 second')) ts " +
        "               ) tss ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getAvgRevenueTrendWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime, Integer interval);

}
