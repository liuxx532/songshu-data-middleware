package com.comall.songshu.repository.index;


import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;


/**
 * Created by lgx on 17/4/18.
 * 销售额
 */
public interface RevenueRepository extends JpaRepository<Author, Long> {

    /**
     * 销售额（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COALESCE(SUM(cpr.amount),0) FROM songshu_cs_order o\n" +
        "INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime,SUM(\"Amount\") AS amount FROM (SELECT * FROM songshu_cs_payment_record\n" +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr\n" +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\"\n" +
        "WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1 \n" +
        "AND cpr.paidTime BETWEEN ?1 AND ?2", nativeQuery = true)
    Double getRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 销售额（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COALESCE(SUM(cpr.amount),0) FROM songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime,SUM(\"Amount\") AS amount FROM (SELECT * FROM songshu_cs_payment_record " +
        "            WHERE \"PaymentModeType\" = 2 ) pr " +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1 AND o.\"Channel\" = ?1 " +
        "AND cpr.paidTime BETWEEN ?2 AND ?3", nativeQuery = true)
    Double getRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);

    /**
     * 销售额趋势图（全平台）
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT tss.stime AS stime, tss.etime AS etime, COALESCE(SUM(comt.Amount),0) " +
        "FROM(SELECT cpr.amount, cpr.paidTime " +
        "     FROM songshu_cs_order o " +
        "     INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "     INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime,SUM(\"Amount\") AS amount FROM (SELECT * FROM songshu_cs_payment_record " +
        "                WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "     WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1  " +
        "     AND cpr.paidTime BETWEEN ?1 AND ?2 " +
        "    ) comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "        FROM (SELECT generate_series(?1,?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "        ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getRevenueTrendWithAllPlatform(Timestamp beginTime, Timestamp endTime, Integer interval);

    /**
     * 销售额趋势图（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT tss.stime AS stime, tss.etime AS etime, COALESCE(SUM(comt.Amount),0) " +
        "FROM(SELECT cpr.amount, cpr.paidTime " +
        "     FROM songshu_cs_order o " +
        "     INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "     INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime,SUM(\"Amount\") AS amount FROM (SELECT * FROM songshu_cs_payment_record " +
        "                WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?2 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?3 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = p.\"MergePaymentId\" " +
        "     WHERE o.\"OrderStatus\" NOT IN (6, 7) AND p.\"PaymentStatus\" = 1 AND o.\"Channel\" = ?1 " +
        "     AND cpr.paidTime BETWEEN ?2 AND ?3 " +
        "    ) comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?4 * INTERVAL '1 second' AS etime " +
        "        FROM (SELECT generate_series(?2,?3, ?4 * INTERVAL '1 second')) ts) tss " +
        "        ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getRevenueTrendWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime, Integer interval);


}
