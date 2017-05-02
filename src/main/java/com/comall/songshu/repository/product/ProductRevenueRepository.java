package com.comall.songshu.repository.product;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 品类销售统计
 *
 * @author liushengling
 * @create 2017-05-02-16:21
 **/
public interface ProductRevenueRepository extends JpaRepository<Author,Long> {



//    SELECT c."Name" as categoryName, p."Name" AS  productName, base.* FROM
//        (SELECT i."ProductId" AS productId, sum(i."AfterFoldingPrice") AS revenue,count(DISTINCT o."Id") AS  salesCount ,sum(i."ReferCost" * i."Quantity") AS cost,
//    (sum(i."AfterFoldingPrice")-sum(i."ReferCost" * i."Quantity"))/sum(i."AfterFoldingPrice") as grossMaringRate FROM songshu_cs_order_item i
//    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
//        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    GROUP BY i."ProductId") base
//    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
//    INNER JOIN songshu_cs_category c ON c."Id" = p."CategoryId";
    @Query(value = "SELECT c.\"Name\" as categoryName, p.\"Name\" AS  productName, base.* FROM " +
        "(SELECT i.\"ProductId\" AS productId, SUM(i.\"AfterFoldingPrice\") AS revenue,count(DISTINCT o.\"Id\") AS  salesCount ,SUM(i.\"ReferCost\" * i.\"Quantity\") AS cost, " +
        "(SUM(i.\"AfterFoldingPrice\")-SUM(i.\"ReferCost\" * i.\"Quantity\"))/SUM(i.\"AfterFoldingPrice\") as grossMaringRate FROM songshu_cs_order_item i " +
        "INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\" " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN ( SELECT DISTINCT t.\"MergePaymentNo\" FROM songshu_cs_payment_record t " +
        "WHERE t.\"PaymentModeType\"= 2 AND t.\"PaidTime\" BETWEEN ?1 AND ?2 )cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "GROUP BY i.\"ProductId\") base " +
        "INNER JOIN songshu_cs_product p ON p.\"Id\" = base.productId " +
        "INNER JOIN songshu_cs_category c ON c.\"Id\" = p.\"CategoryId\" ", nativeQuery = true)
    List<Object[]> getProductRevenueAllPlatform(Timestamp beginTime, Timestamp endTime);



//    SELECT c."Name" as categoryName, p."Name" AS  productName, base.* FROM
//        (SELECT i."ProductId" AS productId, sum(i."AfterFoldingPrice") AS revenue,count(DISTINCT o."Id") AS  salesCount ,sum(i."ReferCost" * i."Quantity") AS cost,
//    (sum(i."AfterFoldingPrice")-sum(i."ReferCost" * i."Quantity"))/sum(i."AfterFoldingPrice") as grossMaringRate FROM songshu_cs_order_item i
//    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
//        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND o."Channel" = 4
//    GROUP BY i."ProductId") base
//    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
//    INNER JOIN songshu_cs_category c ON c."Id" = p."CategoryId";
    @Query(value = "SELECT c.\"Name\" as categoryName, p.\"Name\" AS  productName, base.* FROM " +
        "(SELECT i.\"ProductId\" AS productId, SUM(i.\"AfterFoldingPrice\") AS revenue,count(DISTINCT o.\"Id\") AS  salesCount ,SUM(i.\"ReferCost\" * i.\"Quantity\") AS cost, " +
        "(SUM(i.\"AfterFoldingPrice\")-SUM(i.\"ReferCost\" * i.\"Quantity\"))/SUM(i.\"AfterFoldingPrice\") as grossMaringRate FROM songshu_cs_order_item i " +
        "INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\" " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN ( SELECT DISTINCT t.\"MergePaymentNo\" FROM songshu_cs_payment_record t " +
        "WHERE t.\"PaymentModeType\"= 2 AND t.\"PaidTime\" BETWEEN ?1 AND ?2 )cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) AND o.\"Channel\" = ?3 " +
        "GROUP BY i.\"ProductId\") base " +
        "INNER JOIN songshu_cs_product p ON p.\"Id\" = base.productId " +
        "INNER JOIN songshu_cs_category c ON c.\"Id\" = p.\"CategoryId\" ", nativeQuery = true)
    List<Object[]> getProductRevenueSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer plateForm);
}
