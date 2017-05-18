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
    @Query(value = "SELECT c.\"Name\",COALESCE(SUM(i.\"AfterFoldingPrice\"),0) AS tmount " +
        "FROM  songshu_cs_category c " +
        "INNER JOIN songshu_cs_product p ON c.\"Id\" = p.\"CategoryId\" " +
        "INNER JOIN songshu_cs_order_item i ON i.\"ProductId\" = p.\"Id\" " +
        "INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\" " +
        "INNER JOIN songshu_cs_order_payable op ON o.\"Id\" = op.\"OrderId\" " +
        "INNER JOIN (SELECT \"MergePaymentNo\",MAX(\"PaidTime\") AS paidTime FROM songshu_cs_payment_record WHERE \"PaymentModeType\" =2 " +
        "            GROUP BY \"MergePaymentNo\",\"PaymentModeType\") r ON op.\"MergePaymentId\" = r.\"MergePaymentNo\" " +
        "WHERE c.\"Id\" != 1 AND op.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6,7) AND r.paidTime BETWEEN ?1 AND ?2 " +
        "GROUP BY c.\"Name\" ORDER BY tmount DESC;", nativeQuery = true)
    List<Object[]> getCategoryRevenueRankingWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 销售额品类排行（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT c.\"Name\",COALESCE(SUM(i.\"AfterFoldingPrice\"),0) AS tmount " +
        "FROM  songshu_cs_category c " +
        "INNER JOIN songshu_cs_product p ON c.\"Id\" = p.\"CategoryId\" " +
        "INNER JOIN songshu_cs_order_item i ON i.\"ProductId\" = p.\"Id\" " +
        "INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\" " +
        "INNER JOIN songshu_cs_order_payable op ON o.\"Id\" = op.\"OrderId\" " +
        "INNER JOIN (select \"MergePaymentNo\",MAX(\"PaidTime\") AS paidTime from songshu_cs_payment_record WHERE \"PaymentModeType\" =2 " +
        "            GROUP BY \"MergePaymentNo\",\"PaymentModeType\") r ON op.\"MergePaymentId\" = r.\"MergePaymentNo\" " +
        "WHERE c.\"Id\" != 1 AND op.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6,7) AND o.\"Channel\" = ?1 AND r.paidTime BETWEEN ?2 AND ?3  " +
        "GROUP BY c.\"Name\" ORDER BY tmount DESC;", nativeQuery = true)
    List<Object[]> getCategoryRevenueRankingWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);
}
