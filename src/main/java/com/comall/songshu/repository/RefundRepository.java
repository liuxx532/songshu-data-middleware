package com.comall.songshu.repository;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * Created by liugaoyu on 2017/4/20.
 */
public interface RefundRepository extends JpaRepository<Author,Long> {

// SQL
//    SELECT sum(i."ActualRefundMoney") -- 推荐使用实际退款金额
//    FROM songshu_cs_refund_item i JOIN songshu_cs_payment_record r ON i."PaymentRecordId" = r."Id"
//    JOIN songshu_cs_order o ON r."MergePaymentNo" = o."OrderNumber"
//    WHERE i."MoneyType" = 1 AND i."RefundType" = 1 AND i."Status" = 5
//    AND o."OrderCreateTime" BETWEEN '2013-01-06 18:22:50' AND '2017-01-06 18:22:50'
//    AND o."Channel" IN (0, 1, 2, 3, 5)


    @Query(value = "SELECT sum(i.\"ActualRefundMoney\")\n" +
        "FROM songshu_cs_refund_item i JOIN songshu_cs_payment_record r ON i.\"PaymentRecordId\" = r.\"Id\"\n" +
        "  JOIN songshu_cs_order o ON r.\"MergePaymentNo\" = o.\"OrderNumber\"\n" +
        "WHERE i.\"MoneyType\" = 1 AND i.\"RefundType\" = 1 AND i.\"Status\" = 5\n" +
        "      AND o.\"OrderCreateTime\" BETWEEN ?1 AND ?2\n" +
        "      AND o.\"Channel\" IN (0, 1, 2, 3, 5)", nativeQuery = true)
    Double getRefundWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    @Query(value = "SELECT sum(i.\"ActualRefundMoney\")\n" +
        "FROM songshu_cs_refund_item i JOIN songshu_cs_payment_record r ON i.\"PaymentRecordId\" = r.\"Id\"\n" +
        "  JOIN songshu_cs_order o ON r.\"MergePaymentNo\" = o.\"OrderNumber\"\n" +
        "WHERE i.\"MoneyType\" = 1 AND i.\"RefundType\" = 1 AND i.\"Status\" = 5\n" +
        "      AND o.\"OrderCreateTime\" BETWEEN ?2 AND ?3\n" +
        "      AND o.\"Channel\" = ?1", nativeQuery = true)
    Double getRefundWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);

    // TODO and trend
}
