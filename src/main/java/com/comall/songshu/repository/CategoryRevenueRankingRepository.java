package com.comall.songshu.repository;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 销售额品类排行
 * Created by huanghaizhou on 2017/4/25.
 */
public interface CategoryRevenueRankingRepository extends JpaRepository<Author,Long> {

// SQL:
// SELECT c."Name",sum(i."AfterFoldingPrice") AS tmount
// FROM(SELECT o.* FROM songshu_cs_order o
// JOIN( SELECT DISTINCT "MergePaymentNo" FROM songshu_cs_payment_record
// WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN ? AND ?)r ON o."OrderNumber" = r."MergePaymentNo"
// JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId" WHERE p."PaymentStatus" = 1 AND o."OrderStatus" NOT IN (6,7) AND o."Channel" IN (0, 1, 2, 3, 5) ) oo
// LEFT JOIN songshu_cs_order_item i ON oo."Id" = i."OrderId" LEFT JOIN songshu_cs_product p ON i."ProductId" = p."Id"
// INNER JOIN songshu_cs_category c ON p."CategoryId" = c."Id" WHERE c."Id" != 1 GROUP BY c."Name" ORDER BY tmount DESC

    @Query(value = "SELECT c.\"Name\",sum(i.\"AfterFoldingPrice\") AS tmount " +
        "FROM(SELECT o.* FROM songshu_cs_order o JOIN( SELECT DISTINCT \"MergePaymentNo\" FROM songshu_cs_payment_record " +
        "WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN ?1 AND ?2)r " +
        "ON o.\"OrderNumber\" = r.\"MergePaymentNo\" JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6,7) AND o.\"Channel\" IN (0, 1, 2, 3, 5) ) oo " +
        "LEFT JOIN songshu_cs_order_item i ON oo.\"Id\" = i.\"OrderId\" LEFT JOIN songshu_cs_product p ON i.\"ProductId\" = p.\"Id\" " +
        "INNER JOIN songshu_cs_category c ON p.\"CategoryId\" = c.\"Id\" " +
        "WHERE c.\"Id\" != 1 GROUP BY c.\"Name\" ORDER BY tmount DESC", nativeQuery = true)
    List<Object[]> getCategoryRevenueRankingWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    @Query(value = "SELECT c.\"Name\",sum(i.\"AfterFoldingPrice\") AS tmount " +
        "FROM(SELECT o.* FROM songshu_cs_order o JOIN( SELECT DISTINCT \"MergePaymentNo\" FROM songshu_cs_payment_record " +
        "WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN ?2 AND ?3)r " +
        "ON o.\"OrderNumber\" = r.\"MergePaymentNo\" JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6,7) AND o.\"Channel\" = ?1 ) oo " +
        "LEFT JOIN songshu_cs_order_item i ON oo.\"Id\" = i.\"OrderId\" LEFT JOIN songshu_cs_product p ON i.\"ProductId\" = p.\"Id\" " +
        "INNER JOIN songshu_cs_category c ON p.\"CategoryId\" = c.\"Id\" " +
        "WHERE c.\"Id\" != 1 GROUP BY c.\"Name\" ORDER BY tmount DESC", nativeQuery = true)
    List<Object[]> getCategoryRevenueRankingWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);
}
