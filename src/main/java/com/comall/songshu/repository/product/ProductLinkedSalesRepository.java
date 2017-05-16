package com.comall.songshu.repository.product;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 关联商品销售统计
 *
 * @author liushengling
 * @create 2017-05-02-16:21
 **/
public interface ProductLinkedSalesRepository extends JpaRepository<Author,Long> {



//    SELECT  p."Id" as productId,p."Name" AS  productName, base.salesCount AS salesCount FROM
//        (SELECT i."ProductId" AS productId,count(DISTINCT o."Id") AS  salesCount FROM songshu_cs_order_item i
//    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
//        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    GROUP BY i."ProductId" ORDER BY salesCount DESC LIMIT 3) base
//    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
//    ORDER BY base.salesCount DESC LIMIT 3;
    @Query(value = "SELECT  p.\"Id\" as productId,p.\"Name\" AS  productName, base.salesCount AS salesCount FROM\n" +
        "    (SELECT i.\"ProductId\" AS productId,count(DISTINCT o.\"Id\") AS  salesCount FROM songshu_cs_order_item i\n" +
        "        INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"\n" +
        "        INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\"\n" +
        "        INNER JOIN ( SELECT DISTINCT t.\"MergePaymentNo\" FROM songshu_cs_payment_record t\n" +
        "        WHERE \"PaymentModeType\"= 2 AND \"PaidTime\" BETWEEN ?1 AND ?2)cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\"\n" +
        "    WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) \n" +
        "    GROUP BY i.\"ProductId\" ORDER BY salesCount DESC LIMIT 3) base\n" +
        "    INNER JOIN songshu_cs_product p ON p.\"Id\" = base.productId\n" +
        "ORDER BY base.salesCount DESC LIMIT ?3", nativeQuery = true)
    List<Object[]> getProductSalesAllPlatform(Timestamp beginTime, Timestamp endTime, Integer topCount);


//
//    SELECT  p."Id" as productId,p."Name" AS  productName, base.salesCount AS salesCount FROM
//        (SELECT i."ProductId" AS productId,count(DISTINCT o."Id") AS  salesCount FROM songshu_cs_order_item i
//    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
//        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND o."Channel" = 1
//    GROUP BY i."ProductId" ORDER BY salesCount DESC LIMIT 3) base
//    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
//    ORDER BY base.salesCount DESC LIMIT 3;
    @Query(value = "SELECT  p.\"Id\" as productId,p.\"Name\" AS  productName, base.salesCount AS salesCount FROM\n" +
        "    (SELECT i.\"ProductId\" AS productId,count(DISTINCT o.\"Id\") AS  salesCount FROM songshu_cs_order_item i\n" +
        "        INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"\n" +
        "        INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\"\n" +
        "        INNER JOIN ( SELECT DISTINCT t.\"MergePaymentNo\" FROM songshu_cs_payment_record t\n" +
        "        WHERE \"PaymentModeType\"= 2 AND \"PaidTime\" BETWEEN ?1 AND ?2)cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\"\n" +
        "    WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) AND o.\"Channel\" = ?3\n" +
        "    GROUP BY i.\"ProductId\" ORDER BY salesCount DESC LIMIT 3) base\n" +
        "    INNER JOIN songshu_cs_product p ON p.\"Id\" = base.productId\n" +
        "ORDER BY base.salesCount DESC LIMIT ?4", nativeQuery = true)
    List<Object[]> getProductSalesSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm, Integer topCount);


//    SELECT  p."Id" as productId,p."Name" AS  productName, base.salesCount AS salesCount FROM
//        (SELECT i."ProductId" AS productId,count(DISTINCT o."Id") AS  salesCount FROM songshu_cs_order_item i
//    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
//        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    AND o."Id" IN (SELECT DISTINCT (scoi."OrderId") FROM songshu_cs_order_item scoi
//    INNER JOIN songshu_cs_order co ON co."Id" = scoi."OrderId"
//    INNER JOIN songshu_cs_order_payable cop ON cop."OrderId" = co."Id"
//    INNER JOIN songshu_cs_payment_record cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
//    WHERE cop."PaymentStatus" =1 AND co."orderType" IN (0,1) AND co."OrderStatus" NOT IN (6,7) AND cpr."PaidTime"
//    BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND scoi."ProductId" = 100100316)
//    AND i."ProductId" != 100100316
//    GROUP BY i."ProductId" ORDER BY salesCount DESC LIMIT 3) base
//    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
//    ORDER BY base.salesCount DESC LIMIT 3
    @Query(value = "SELECT  p.\"Id\" as productId,p.\"Name\" AS  productName, base.salesCount AS salesCount FROM\n" +
        "    (SELECT i.\"ProductId\" AS productId,count(DISTINCT o.\"Id\") AS  salesCount FROM songshu_cs_order_item i\n" +
        "        INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"\n" +
        "        INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\"\n" +
        "        INNER JOIN ( SELECT DISTINCT t.\"MergePaymentNo\" FROM songshu_cs_payment_record t\n" +
        "        WHERE \"PaymentModeType\"= 2 AND \"PaidTime\" BETWEEN ?1 AND ?2)cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\"\n" +
        "    WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) \n" +
        "    AND o.\"Id\" IN (SELECT DISTINCT (scoi.\"OrderId\") FROM songshu_cs_order_item scoi\n" +
        "        INNER JOIN songshu_cs_order co ON co.\"Id\" = scoi.\"OrderId\"\n" +
        "        INNER JOIN songshu_cs_order_payable cop ON cop.\"OrderId\" = co.\"Id\"\n" +
        "        INNER JOIN songshu_cs_payment_record cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\"\n" +
        "    WHERE cop.\"PaymentStatus\" =1 AND co.\"orderType\" IN (0,1) AND co.\"OrderStatus\" NOT IN (6,7) AND cpr.\"PaidTime\"\n" +
        "    BETWEEN ?1 AND ?2 AND scoi.\"ProductId\" = ?4 )\n" +
        "    AND i.\"ProductId\" != ?4\n" +
        "    GROUP BY i.\"ProductId\" ORDER BY salesCount DESC LIMIT 3) base\n" +
        "    INNER JOIN songshu_cs_product p ON p.\"Id\" = base.productId\n" +
        "ORDER BY base.salesCount DESC LIMIT ?3", nativeQuery = true)
    List<Object[]> getProductLinkedSalesAllPlatform(Timestamp beginTime, Timestamp endTime, Integer topCount,Integer productId);


//    SELECT  p."Id" as productId,p."Name" AS  productName, base.salesCount AS salesCount FROM
//        (SELECT i."ProductId" AS productId,count(DISTINCT o."Id") AS  salesCount FROM songshu_cs_order_item i
//    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
//        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND o."Channel" = 1
//    AND o."Id" IN (SELECT DISTINCT (scoi."OrderId") FROM songshu_cs_order_item scoi
//    INNER JOIN songshu_cs_order co ON co."Id" = scoi."OrderId"
//    INNER JOIN songshu_cs_order_payable cop ON cop."OrderId" = co."Id"
//    INNER JOIN songshu_cs_payment_record cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
//    WHERE cop."PaymentStatus" =1 AND co."orderType" IN (0,1) AND co."OrderStatus" NOT IN (6,7) AND cpr."PaidTime"
//    BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND scoi."ProductId" = 100100316 AND co."Channel" = 1)
//    AND i."ProductId" != 100100316
//    GROUP BY i."ProductId" ORDER BY salesCount DESC LIMIT 3) base
//    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
//    ORDER BY base.salesCount DESC LIMIT 3
    @Query(value = "SELECT  p.\"Id\" as productId,p.\"Name\" AS  productName, base.salesCount AS salesCount FROM\n" +
        "    (SELECT i.\"ProductId\" AS productId,count(DISTINCT o.\"Id\") AS  salesCount FROM songshu_cs_order_item i\n" +
        "        INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"\n" +
        "        INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\"\n" +
        "        INNER JOIN ( SELECT DISTINCT t.\"MergePaymentNo\" FROM songshu_cs_payment_record t\n" +
        "        WHERE \"PaymentModeType\"= 2 AND \"PaidTime\" BETWEEN ?1 AND ?2)cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\"\n" +
        "    WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) AND o.\"Channel\" = ?3\n" +
        "    AND o.\"Id\" IN (SELECT DISTINCT (scoi.\"OrderId\") FROM songshu_cs_order_item scoi\n" +
        "        INNER JOIN songshu_cs_order co ON co.\"Id\" = scoi.\"OrderId\"\n" +
        "        INNER JOIN songshu_cs_order_payable cop ON cop.\"OrderId\" = co.\"Id\"\n" +
        "        INNER JOIN songshu_cs_payment_record cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\"\n" +
        "    WHERE cop.\"PaymentStatus\" =1 AND co.\"orderType\" IN (0,1) AND co.\"OrderStatus\" NOT IN (6,7) AND cpr.\"PaidTime\"\n" +
        "    BETWEEN ?1 AND ?2 AND scoi.\"ProductId\" = ?5 AND co.\"Channel\" = ?3)\n" +
        "    AND i.\"ProductId\" != ?5\n" +
        "    GROUP BY i.\"ProductId\" ORDER BY salesCount DESC LIMIT 3) base\n" +
        "    INNER JOIN songshu_cs_product p ON p.\"Id\" = base.productId\n" +
        "ORDER BY base.salesCount DESC LIMIT ?4", nativeQuery = true)
    List<Object[]> getProductLinkedSalesSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm, Integer topCount,Integer productId);


//    SELECT  CONCAT('/',pic."Location",'/',pic."Digest",pic."Extension") AS picUrl FROM  songshu_cs_product_picture pp
//    INNER JOIN songshu_cs_picture pic ON pic."Id" = pp."pictureId" WHERE pp."productId" = 100100316 ORDER BY pic."Id" ASC  LIMIT 1;
    @Query(value = "SELECT  CONCAT('/',pic.\"Location\",'/',pic.\"Digest\",pic.\"Extension\") AS picUrl FROM  songshu_cs_product_picture pp\n" +
        "INNER JOIN songshu_cs_picture pic ON pic.\"Id\" = pp.\"pictureId\" WHERE pp.\"productId\" = ?1 ORDER BY pic.\"Id\" ASC  LIMIT 1;", nativeQuery = true)
    String getProductPicUrl(Integer productId);
}
