package com.comall.songshu.repository.product;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 商品雷达Repository
 * Created by huanghaizhou on 2017/5/3.
 */
public interface ProductCategoryRankRepository extends JpaRepository<Author,Long> {
    //SQL
    //SELECT tcom.tname, COALESCE(tcom.tamount, 0)
    // FROM( SELECT c."Name" AS tname, sum(i."AfterFoldingPrice") AS tamount
    // FROM (SELECT o.* FROM songshu_cs_order o JOIN (SELECT DISTINCT "MergePaymentNo"
    // FROM songshu_cs_payment_record
    // WHERE "PaymentModeType" = 2 AND "PaidTime"
    // BETWEEN '2015-03-20 00:00:00' AND '2017-04-30 00:00:00') r ON o."OrderNumber" = r."MergePaymentNo"
    // JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId" WHERE p."PaymentStatus" = 1 AND o."OrderStatus" NOT IN (6, 7)
    // AND o."Channel" IN (0, 1, 2, 3, 5)) oo LEFT JOIN songshu_cs_order_item i ON oo."Id" = i."OrderId"
    // LEFT JOIN songshu_cs_product p ON i."ProductId" = p."Id"
    // INNER JOIN songshu_cs_category c ON p."CategoryId" = c."Id" WHERE c."Id" != 1
    // GROUP BY c."Name" ORDER BY tamount DESC) tcom;

    @Query(value = "SELECT tcom.tname, COALESCE(tcom.tamount, 0) " +
        "FROM( SELECT c.\"Name\" AS tname, sum(i.\"AfterFoldingPrice\") AS tamount " +
        "FROM (SELECT o.* FROM songshu_cs_order o JOIN (SELECT DISTINCT \"MergePaymentNo\" " +
        "FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" " +
        "BETWEEN ?1 AND ?2) r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" " +
        "JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6, 7) AND o.\"Channel\" IN (0, 1, 2, 3, 5)) oo " +
        "LEFT JOIN songshu_cs_order_item i ON oo.\"Id\" = i.\"OrderId\" LEFT JOIN songshu_cs_product p ON i.\"ProductId\" = p.\"Id\" " +
        "INNER JOIN songshu_cs_category c ON p.\"CategoryId\" = c.\"Id\" WHERE c.\"Id\" != 1 GROUP BY c.\"Name\" ORDER BY tamount DESC) tcom;", nativeQuery = true)
    List<Object[]> getProductCategoryRankWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "SELECT tcom.tname, COALESCE(tcom.tamount, 0) " +
        "FROM( SELECT c.\"Name\" AS tname, sum(i.\"AfterFoldingPrice\") AS tamount " +
        "FROM (SELECT o.* FROM songshu_cs_order o JOIN (SELECT DISTINCT \"MergePaymentNo\" " +
        "FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" " +
        "BETWEEN ?1 AND ?2) r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" " +
        "JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6, 7) AND o.\"Channel\" = ?3) oo " +
        "LEFT JOIN songshu_cs_order_item i ON oo.\"Id\" = i.\"OrderId\" LEFT JOIN songshu_cs_product p ON i.\"ProductId\" = p.\"Id\" " +
        "INNER JOIN songshu_cs_category c ON p.\"CategoryId\" = c.\"Id\" WHERE c.\"Id\" != 1 GROUP BY c.\"Name\" ORDER BY tamount DESC) tcom;", nativeQuery = true)
    List<Object[]> getProductCategoryRankWithSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer platform);
}
