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
    @Query(value = "SELECT c.\"Name\",COALESCE(SUM(i.\"AfterFoldingPrice\"),0) AS tmount\n" +
        "FROM  songshu_cs_category c\n" +
        "INNER JOIN songshu_cs_product p ON c.\"Id\" = p.\"CategoryId\"\n" +
        "INNER JOIN songshu_cs_order_item i ON i.\"ProductId\" = p.\"Id\"\n" +
        "INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"\n" +
        "INNER JOIN songshu_cs_order_payable op ON o.\"Id\" = op.\"OrderId\"\n" +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record\n" +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr\n" +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") r ON op.\"MergePaymentId\" = r.\"MergePaymentNo\"\n" +
        "WHERE c.\"Id\" != 1 AND op.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6,7) AND r.paidTime BETWEEN ?1 AND ?2 \n" +
        "GROUP BY c.\"Name\" ORDER BY tmount DESC;", nativeQuery = true)
    List<Object[]> getCategoryRevenueRankingWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 销售额品类排行（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT c.\"Name\",COALESCE(SUM(i.\"AfterFoldingPrice\"),0) AS tmount\n" +
        "FROM  songshu_cs_category c\n" +
        "INNER JOIN songshu_cs_product p ON c.\"Id\" = p.\"CategoryId\"\n" +
        "INNER JOIN songshu_cs_order_item i ON i.\"ProductId\" = p.\"Id\"\n" +
        "INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"\n" +
        "INNER JOIN songshu_cs_order_payable op ON o.\"Id\" = op.\"OrderId\"\n" +
        "INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record\n" +
        "            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?2 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?3 AS TIMESTAMP) + INTERVAL '1 D')) pr\n" +
        "            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") r ON op.\"MergePaymentId\" = r.\"MergePaymentNo\"\n" +
        "WHERE c.\"Id\" != 1 AND op.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6,7) AND o.\"Channel\" = ?1 AND r.paidTime BETWEEN ?2 AND ?3 \n" +
        "GROUP BY c.\"Name\" ORDER BY tmount DESC;", nativeQuery = true)
    List<Object[]> getCategoryRevenueRankingWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);
}
