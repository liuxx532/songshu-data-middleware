package com.comall.songshu.repository.index;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by liugaoyu on 2017/4/20.
 * 退款金额
 */
public interface RefundRepository extends JpaRepository<Author,Long> {


    /**
     * 退款金额（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COALESCE(SUM(item.\"ActualRefundMoney\"),0) FROM songshu_cs_order oo " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = oo.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record r ON op.\"MergePaymentId\" = r.\"MergePaymentNo\" " +
        "INNER JOIN songshu_cs_refund_item item ON item.\"PaymentRecordId\" = r.\"Id\" " +
        "WHERE r.\"PaymentModeType\" = 2 AND item. \"Status\" = 5 AND item.\"MoneyType\" = 1 " +
        "AND item.\"RefundType\" = 1 AND oo.\"orderType\" IN(0, 1)  " +
        "AND item.\"LastModifyTime\" BETWEEN ?1 AND ?2", nativeQuery = true)
    Double getRefundWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 退款金额（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COALESCE(SUM(item.\"ActualRefundMoney\"),0) FROM songshu_cs_order oo " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = oo.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record r ON op.\"MergePaymentId\" = r.\"MergePaymentNo\" " +
        "INNER JOIN songshu_cs_refund_item item ON item.\"PaymentRecordId\" = r.\"Id\" " +
        "WHERE r.\"PaymentModeType\" = 2 AND item. \"Status\" = 5 AND item.\"MoneyType\" = 1 " +
        "AND item.\"RefundType\" = 1 AND oo.\"orderType\" IN(0, 1) AND oo.\"Channel\" = ?1 " +
        "AND item.\"LastModifyTime\" BETWEEN ?2 AND ?3", nativeQuery = true)
    Double getRefundWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);


    /**
     *  退款金额趋势图（全平台）
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT tss.stime, tss.etime, COALESCE(SUM(comt.ActualRefundMoney),0) " +
        "FROM(SELECT item.\"ActualRefundMoney\" AS ActualRefundMoney, item.\"LastModifyTime\" AS LastModifyTime " +
        "     FROM songshu_cs_order oo INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = oo.\"Id\" " +
        "     INNER JOIN songshu_cs_payment_record r ON op.\"MergePaymentId\" = r.\"MergePaymentNo\" " +
        "     INNER JOIN songshu_cs_refund_item item ON item.\"PaymentRecordId\" = r.\"Id\" " +
        "     WHERE r.\"PaymentModeType\" = 2 AND item.\"Status\" = 5 AND item.\"MoneyType\" = 1 " +
        "     AND item.\"RefundType\" = 1 AND oo.\"orderType\" IN (0, 1)  " +
        "     AND item.\"LastModifyTime\" BETWEEN ?1 AND ?2 " +
        "    ) comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "        FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "        ON (comt.LastModifyTime < tss.etime AND comt.LastModifyTime >= tss.stime) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getRefundTrendWithAllPlatform(Timestamp beginTime, Timestamp endTime, Integer interval);

    /**
     * 退款金额趋势图（单平台）
     * @param beginTime
     * @param endTime
     * @param interval
     * @param platform
     * @return
     */
    @Query(value = "SELECT tss.stime, tss.etime, COALESCE(SUM(comt.ActualRefundMoney),0) " +
        "FROM(SELECT item.\"ActualRefundMoney\" AS ActualRefundMoney, item.\"LastModifyTime\" AS LastModifyTime " +
        "     FROM songshu_cs_order oo INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = oo.\"Id\" " +
        "     INNER JOIN songshu_cs_payment_record r ON op.\"MergePaymentId\" = r.\"MergePaymentNo\" " +
        "     INNER JOIN songshu_cs_refund_item item ON item.\"PaymentRecordId\" = r.\"Id\" " +
        "     WHERE r.\"PaymentModeType\" = 2 AND item.\"Status\" = 5 AND item.\"MoneyType\" = 1 " +
        "     AND item.\"RefundType\" = 1 AND oo.\"orderType\" IN (0, 1)  AND oo.\"Channel\" =?4 " +
        "     AND item.\"LastModifyTime\" BETWEEN ?1 AND ?2 " +
        "    ) comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "        FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "        ON (comt.LastModifyTime < tss.etime AND comt.LastModifyTime >= tss.stime) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getRefundTrendWithSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer interval, Integer platform);
}
