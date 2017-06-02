package com.comall.songshu.repository.index;

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


    /**
     * 销售额品类排行（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT c.\"Name\",COALESCE(SUM(oo.\"AfterFoldingPrice\"),0) AS tmount " +
        "FROM songshu_cs_category c " +
        "LEFT JOIN songshu_cs_product p ON c.\"Id\" = p.\"CategoryId\" " +
        "LEFT JOIN (SELECT i.\"ProductId\",i.\"AfterFoldingPrice\" FROM songshu_cs_order o " +
        "            INNER JOIN songshu_cs_order_item i ON i.\"OrderId\" = o.\"Id\" " +
        "            INNER JOIN (SELECT DISTINCT \"MergePaymentNo\" " +
        "                        FROM (SELECT pr.\"MergePaymentNo\",MAX(pr.\"PaidTime\") AS paidTime " +
        "                              FROM (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" " +
        "                              BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') " +
        "                              AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D') " +
        "                                   ) pr GROUP BY \"MergePaymentNo\" " +
        "                             ) prr " +
        "                        WHERE prr.paidTime  BETWEEN ?1 AND ?2) r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" " +
        "            INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "            WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6, 7) " +
        "            ) oo ON oo.\"ProductId\" = p.\"Id\" " +
        "WHERE c.\"Id\" != 1 AND c.\"Name\" != '投食卡' GROUP BY c.\"Name\" ORDER BY tmount DESC", nativeQuery = true)
    List<Object[]> getCategoryRevenueRankingWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 销售额品类排行（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT c.\"Name\",COALESCE(SUM(oo.\"AfterFoldingPrice\"),0) AS tmount " +
        "FROM songshu_cs_category c " +
        "LEFT JOIN songshu_cs_product p ON c.\"Id\" = p.\"CategoryId\" " +
        "LEFT JOIN (SELECT i.\"ProductId\",i.\"AfterFoldingPrice\" FROM songshu_cs_order o " +
        "            INNER JOIN songshu_cs_order_item i ON i.\"OrderId\" = o.\"Id\" " +
        "            INNER JOIN (SELECT DISTINCT \"MergePaymentNo\" " +
        "                        FROM (SELECT pr.\"MergePaymentNo\",MAX(pr.\"PaidTime\") AS paidTime " +
        "                              FROM (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" " +
        "                              BETWEEN (CAST(?2 AS TIMESTAMP) - INTERVAL '1 D') " +
        "                              AND (CAST(?3 AS TIMESTAMP) + INTERVAL '1 D') " +
        "                                   ) pr GROUP BY \"MergePaymentNo\" " +
        "                             ) prr " +
        "                        WHERE prr.paidTime  BETWEEN ?2 AND ?3) r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" " +
        "            INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "            WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6, 7) AND o.\"Channel\" = ?1 " +
        "            ) oo ON oo.\"ProductId\" = p.\"Id\" " +
        "WHERE c.\"Id\" != 1 AND c.\"Name\" != '投食卡' GROUP BY c.\"Name\" ORDER BY tmount DESC", nativeQuery = true)
    List<Object[]> getCategoryRevenueRankingWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);
}
