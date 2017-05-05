package com.comall.songshu.repository.member;

import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户数据
 *
 * @author liushengling
 * @create 2017-05-04-16:39
 **/
public interface MemberDetailRepository {


//    SELECT count(DISTINCT(m.id)) AS memberCount FROM songshu_cs_member m
//    WHERE m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND m."multipleChannelsId" = 1
    @Query(value = "SELECT count(DISTINCT(m.id)) AS memberCount FROM songshu_cs_member m " +
        "WHERE m.\"regTime\" BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getMemberRegisterCountAllPlatform(Timestamp beginTime, Timestamp endTime);


//    SELECT count(DISTINCT(m.id)) AS memberCount FROM songshu_cs_member m
//    WHERE m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND m."multipleChannelsId" = 1
    @Query(value = "SELECT count(DISTINCT(m.id)) AS memberCount FROM songshu_cs_member m " +
        "WHERE m.\"regTime\" BETWEEN ?1 AND ?2 AND m.\"multipleChannelsId\" = ?3 ", nativeQuery = true)
    Integer getMemberRegisterCountSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm);


//    SELECT count(DISTINCT o."MemberId") AS  memberCount FROM  songshu_cs_order o
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    AND pr."PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'
    @Query(value = "SELECT count(DISTINCT o.\"MemberId\") AS  memberCount FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record pr ON pr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "AND pr.\"PaidTime\" BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getConsumerCountAllPlatform(Timestamp beginTime, Timestamp endTime);


//    SELECT count(DISTINCT o."MemberId") AS  memberCount FROM  songshu_cs_order o
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    AND pr."PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND o."Channel" = 1
    @Query(value = "SELECT count(DISTINCT o.\"MemberId\") AS  memberCount FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record pr ON pr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" IN(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "AND pr.\"PaidTime\" BETWEEN ?1 AND ?2 AND o.\"Channel\" = ?3 ", nativeQuery = true)
    Integer getConsumerCountSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm);


//    SELECT COALESCE(sum(main.pay)/sum(main.register),0) as consumeTransferRate FROM
//        (SELECT
//             MAX(CASE type WHEN 'pay' THEN memberCount ELSE 0 END ) pay,
//    MAX(CASE type WHEN 'register' THEN memberCount ELSE 0 END ) register
//    FROM
//        (SELECT count(DISTINCT o."MemberId") AS  memberCount,'pay' AS type ,'1' as date FROM  songshu_cs_order o
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
//    INNER JOIN songshu_cs_member m ON m.id = o."MemberId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    AND m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'
//    AND pr."PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'
//    UNION ALL
//    SELECT count(DISTINCT(m.id)) AS memberCount , 'register' AS type ,'2' as date FROM songshu_cs_member m
//    WHERE m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' ) base
//    GROUP BY date) main
    @Query(value = "SELECT count(DISTINCT o.\"MemberId\") AS  memberCount FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record pr ON pr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "AND pr.\"PaidTime\" BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Double getLogonConsumeRateAllPlatform(Timestamp beginTime, Timestamp endTime);


//    SELECT COALESCE(sum(main.pay)/sum(main.register),0) as consumeTransferRate FROM
//        (SELECT
//             MAX(CASE type WHEN 'pay' THEN memberCount ELSE 0 END ) pay,
//    MAX(CASE type WHEN 'register' THEN memberCount ELSE 0 END ) register
//    FROM
//        (SELECT count(DISTINCT o."MemberId") AS  memberCount,'pay' AS type ,'1' as date FROM  songshu_cs_order o
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
//    INNER JOIN songshu_cs_member m ON m.id = o."MemberId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    AND m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND m."multipleChannelsId" = 1
//    AND pr."PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'  AND o."Channel" = 1
//    UNION ALL
//    SELECT count(DISTINCT(m.id)) AS memberCount , 'register' AS type ,'2' as date FROM songshu_cs_member m
//    WHERE m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND m."multipleChannelsId" = 1) base
//    GROUP BY date) main
    @Query(value = "SELECT COALESCE(sum(main.pay)/sum(main.register),0) as consumeTransferRate FROM " +
        "(SELECT " +
        "MAX(CASE type WHEN 'pay' THEN memberCount ELSE 0 END ) pay, " +
        "MAX(CASE type WHEN 'register' THEN memberCount ELSE 0 END ) register " +
        "FROM " +
        "(SELECT count(DISTINCT o.\"MemberId\") AS  memberCount,'pay' AS type ,'1' as date FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record pr ON pr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "INNER JOIN songshu_cs_member m ON m.id = o.\"MemberId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "AND m.\"regTime\" BETWEEN ?1 AND ?2 AND m.\"multipleChannelsId\" = ?3 " +
        "AND pr.\"PaidTime\" BETWEEN ?1 AND ?2  AND o.\"Channel\" = ?3 " +
        "UNION ALL " +
        "SELECT count(DISTINCT(m.id)) AS memberCount , 'register' AS type ,'2' as date FROM songshu_cs_member m " +
        "WHERE m.\"regTime\" BETWEEN ?1 AND ?2 AND m.\"multipleChannelsId\" = ?3) base " +
        "GROUP BY date) main", nativeQuery = true)
    Double getLogonConsumeRateSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm);


//    SELECT count(DISTINCT(n."MemberId")) AS rebuyMemberCount from
//        (SELECT DISTINCT(o."MemberId") FROM  songshu_cs_order o
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    AND pr."PaidTime" BETWEEN '2016-07-01 00:00:00' AND '2016-08-01 00:00:00' AND o."Channel" = 1) n
//    WHERE  n."MemberId" IN
//        (SELECT DISTINCT (o."MemberId") FROM  songshu_cs_order o
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    AND pr."PaidTime" < '2016-07-01 00:00:00' AND o."Channel" = 1)
    @Query(value = "SELECT count(DISTINCT(n.\"MemberId\")) AS rebuyMemberCount from " +
        "(SELECT DISTINCT(o.\"MemberId\") FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record pr ON pr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "AND pr.\"PaidTime\" BETWEEN ?1 AND ?2  AND o.\"Channel\" = 1) n " +
        "WHERE  n.\"MemberId\" IN " +
        "(SELECT DISTINCT (o.\"MemberId\") FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record pr ON pr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "AND pr.\"PaidTime\" < ?1 )", nativeQuery = true)
    Double getRepeatPurchaseRateAllPlatform(Timestamp beginTime, Timestamp endTime);


//    SELECT count(DISTINCT(n."MemberId")) AS rebuyMemberCount from
//        (SELECT DISTINCT(o."MemberId") FROM  songshu_cs_order o
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    AND pr."PaidTime" BETWEEN '2016-07-01 00:00:00' AND '2016-08-01 00:00:00' AND o."Channel" = 1) n
//    WHERE  n."MemberId" IN
//        (SELECT DISTINCT (o."MemberId") FROM  songshu_cs_order o
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    AND pr."PaidTime" < '2016-07-01 00:00:00' AND o."Channel" = 1)
    @Query(value = "SELECT count(DISTINCT(n.\"MemberId\")) AS rebuyMemberCount from " +
        "(SELECT DISTINCT(o.\"MemberId\") FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record pr ON pr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "AND pr.\"PaidTime\" BETWEEN ?1 AND ?2  AND o.\"Channel\" = ?3) n " +
        "WHERE  n.\"MemberId\" IN " +
        "(SELECT DISTINCT (o.\"MemberId\") FROM  songshu_cs_order o " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record pr ON pr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "AND pr.\"PaidTime\" < ?1 AND o.\"Channel\" = ?3)", nativeQuery = true)
    Double getRepeatPurchaseRateSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm);
}
