package com.comall.songshu.repository.member;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户数据
 *
 * @author liushengling
 * @create 2017-05-04-16:39
 **/
public interface MemberDetailRepository extends JpaRepository<Author,Long> {


    /**
     * 注册用户数（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT count(DISTINCT(m.id)) AS memberCount FROM songshu_cs_member m " +
        "WHERE m.\"regTime\" BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getMemberRegisterCountAllPlatform(Timestamp beginTime, Timestamp endTime);


    /**
     * 注册用户数（单平台）
     * @param beginTime
     * @param endTime
     * @param plateForm
     * @return
     */
    @Query(value = "SELECT count(DISTINCT(m.id)) AS memberCount FROM songshu_cs_member m " +
        "WHERE m.\"regTime\" BETWEEN ?1 AND ?2 AND m.\"multipleChannelsId\" = ?3 ", nativeQuery = true)
    Integer getMemberRegisterCountSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm);


    /**
     * 消费用户数(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT count(DISTINCT o.\"MemberId\") AS  memberCount FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record " +
        "WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" " +
        "BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "GROUP BY \"MergePaymentNo\") pr ON pr. \"MergePaymentNo\"  = op. \"MergePaymentId\" " +
        "WHERE  op. \"PaymentStatus\" = 1 AND  o. \"orderType\" in(0,1) AND  o. \"OrderStatus\" NOT IN (6,7) " +
        "AND pr.paidTime BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getConsumerCountAllPlatform(Timestamp beginTime, Timestamp endTime);


    /**
     * 消费用户数(单平台)
     * @param beginTime
     * @param endTime
     * @param plateForm
     * @return
     */
    @Query(value = "SELECT count(DISTINCT o.\"MemberId\") AS  memberCount FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record " +
        "WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" " +
        "BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "GROUP BY \"MergePaymentNo\") pr ON pr. \"MergePaymentNo\"  = op. \"MergePaymentId\" " +
        "WHERE  op. \"PaymentStatus\" = 1 AND  o. \"orderType\" in(0,1) AND  o. \"OrderStatus\" NOT IN (6,7) " +
        "AND pr.paidTime BETWEEN ?1 AND ?2 AND o.\"Channel\" = ?3 ", nativeQuery = true)
    Integer getConsumerCountSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm);

    /**
     * 注册消费转化率（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COALESCE(sum(main.pay) / sum(main.register), 0) AS consumeTransferRate FROM " +
        "    (SELECT MAX(CASE type WHEN 'pay' THEN memberCount ELSE 0 END) pay, " +
        "            MAX(CASE type WHEN 'register' THEN memberCount ELSE 0 END) register FROM " +
        "         (SELECT count(DISTINCT o.\"MemberId\") AS memberCount, 'pay' AS type, '1' AS date FROM songshu_cs_order o " +
        "              INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "              INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM  " +
        "                 (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND " +
        "                  \"PaidTime\" BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D')  " +
        "                  AND (CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                  GROUP BY \"MergePaymentNo\") prr ON prr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "                  INNER JOIN songshu_cs_member m ON m.id = o.\"MemberId\" " +
        "          WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) " +
        "                AND m.\"regTime\" BETWEEN ?1 AND ?2  " +
        "                AND prr.paidTime BETWEEN ?1 AND ?2  " +
        "          UNION ALL " +
        "          SELECT count(DISTINCT (m.id)) AS memberCount, 'register' AS type, '2' AS date " +
        "          FROM songshu_cs_member m " +
        "          WHERE m.\"regTime\" BETWEEN ?1 AND ?2 ) base " +
        "     GROUP BY date) main ", nativeQuery = true)
    Double getLogonConsumeRateAllPlatform(Timestamp beginTime, Timestamp endTime);


    /**
     * 注册消费转化率（单平台）
     * @param beginTime
     * @param endTime
     * @param plateForm
     * @return
     */
    @Query(value = "SELECT COALESCE(sum(main.pay) / sum(main.register), 0) AS consumeTransferRate FROM " +
        "    (SELECT MAX(CASE type WHEN 'pay' THEN memberCount ELSE 0 END) pay, " +
        "            MAX(CASE type WHEN 'register' THEN memberCount ELSE 0 END) register FROM " +
        "         (SELECT count(DISTINCT o.\"MemberId\") AS memberCount, 'pay' AS type, '1' AS date FROM songshu_cs_order o " +
        "              INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "              INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM  " +
        "                 (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND " +
        "                  \"PaidTime\" BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D')  " +
        "                  AND (CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                  GROUP BY \"MergePaymentNo\") prr ON prr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "                  INNER JOIN songshu_cs_member m ON m.id = o.\"MemberId\" " +
        "          WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) " +
        "                AND m.\"regTime\" BETWEEN ?1 AND ?2  " +
        "                AND prr.paidTime BETWEEN ?1 AND ?2  " +
        "          UNION ALL " +
        "          SELECT count(DISTINCT (m.id)) AS memberCount, 'register' AS type, '2' AS date " +
        "          FROM songshu_cs_member m " +
        "          WHERE m.\"regTime\" BETWEEN ?1 AND ?2 AND m.\"multipleChannelsId\" = ?3 ) base " +
        "     GROUP BY date) main ", nativeQuery = true)
    Double getLogonConsumeRateSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm);

    /**
     * 复购率(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT count(DISTINCT (n.memberId)) AS rebuyMemberCount FROM " +
        "    (SELECT DISTINCT (o.\"MemberId\") AS memberId FROM songshu_cs_order o " +
        "         INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "         INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM " +
        "            (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 " +
        "             AND \"PaidTime\" BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D') " +
        "             AND ( CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "             GROUP BY \"MergePaymentNo\") prr ON prr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "         WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) " +
        "         AND prr.paidTime BETWEEN ?1 AND ?2  ) n " +
        "WHERE n.memberId IN " +
        "      (SELECT DISTINCT (o.\"MemberId\") FROM songshu_cs_order o " +
        "           INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "           INNER JOIN (SELECT \"MergePaymentNo\",  MAX(\"PaidTime\") AS paidTime FROM songshu_cs_payment_record  " +
        "           WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" < ?1  " +
        "           GROUP BY \"MergePaymentNo\") prr ON prr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "        WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) " +
        "        ) ", nativeQuery = true)
    Integer getRepeatPurchaseRateAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 复购率(单平台)
     * @param beginTime
     * @param endTime
     * @param plateForm
     * @return
     */
    @Query(value = "SELECT count(DISTINCT (n.memberId)) AS rebuyMemberCount FROM " +
        "    (SELECT DISTINCT (o.\"MemberId\") AS memberId FROM songshu_cs_order o " +
        "         INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "         INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM " +
        "            (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 " +
        "             AND \"PaidTime\" BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D') " +
        "             AND ( CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "             GROUP BY \"MergePaymentNo\") prr ON prr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "         WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) " +
        "         AND prr.paidTime BETWEEN ?1 AND ?2 AND o.\"Channel\" = ?3 ) n " +
        "WHERE n.memberId IN " +
        "      (SELECT DISTINCT (o.\"MemberId\") FROM songshu_cs_order o " +
        "           INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "           INNER JOIN (SELECT \"MergePaymentNo\",  MAX(\"PaidTime\") AS paidTime FROM songshu_cs_payment_record  " +
        "           WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" < ?1  " +
        "           GROUP BY \"MergePaymentNo\") prr ON prr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "        WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) " +
        "        AND o.\"Channel\" = ?3 ) ", nativeQuery = true)
    Integer getRepeatPurchaseRateSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm);


    /**
     * 启动次数（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT count(1) FROM songshu_shence_events e WHERE e.event ='AppLaunch'  " +
        "AND e.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getOpenTimesAllPlatform(Timestamp beginTime, Timestamp endTime);


    /**
     * 启动次数
     * @param beginTime
     * @param endTime
     * @param os
     * @param plateFormName
     * @return
     */
    @Query(value = "SELECT count(1) FROM songshu_shence_events e WHERE e.event ='AppLaunch' AND (e.os =?3 OR e.platform =?4) " +
        "AND e.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getOpenTimesSinglePlatform(Timestamp beginTime, Timestamp endTime,String os, String plateFormName);


    /**
     * 访问时长（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getVisitTimeAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 访问时长（单平台）
     * @param beginTime
     * @param endTime
     * @param plateForm
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getVisitTimeSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm);

    /**
     * 页面浏览量（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT count(1) FROM songshu_shence_events e WHERE e.event ='$pageview' " +
        "AND e.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getVisitDepthAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 页面浏览量（单平台）
     * @param beginTime
     * @param endTime
     * @param plateFormName
     * @return
     */
    @Query(value = "SELECT count(1) FROM songshu_shence_events e WHERE e.event ='$pageview' " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform =?3 ", nativeQuery = true)
    Integer getVisitDepthSinglePlatform(Timestamp beginTime, Timestamp endTime, String plateFormName);

}
