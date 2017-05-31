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

    /**
     * 商品品类销售额(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT base.tname,base.tamount FROM " +
        "(SELECT c.\"Name\" AS tname,COALESCE(SUM(oo.\"AfterFoldingPrice\"),0) AS tamount,COUNT(DISTINCT \"ProductId\") AS  productCount " +
        "FROM songshu_cs_category c " +
        "LEFT JOIN songshu_cs_product p ON c.\"Id\" = p.\"CategoryId\" " +
        "LEFT JOIN (SELECT o.\"Id\",i.\"ProductId\",i.\"AfterFoldingPrice\" FROM songshu_cs_order o " +
        "            INNER JOIN songshu_cs_order_item i ON i.\"OrderId\" = o.\"Id\" " +
        "            INNER JOIN (SELECT DISTINCT \"MergePaymentNo\" " +
        "                        FROM (SELECT pr.\"MergePaymentNo\",MAX(pr.\"PaidTime\") AS paidTime " +
        "                              FROM (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" " +
        "                                    BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') " +
        "                                    AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D') " +
        "                                   ) pr GROUP BY \"MergePaymentNo\" " +
        "                             ) prr " +
        "                        WHERE prr.paidTime  BETWEEN ?1 AND ?2 ) r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" " +
        "            INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "            WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6, 7) AND o.\"Channel\" IN (0, 1, 2, 3, 5) " +
        "          ) oo ON oo.\"ProductId\" = p.\"Id\" " +
        "WHERE c.\"Id\" != 1  GROUP BY c.\"Name\" ORDER BY tamount DESC " +
        ")base WHERE base.productCount >0", nativeQuery = true)
    List<Object[]> getProductCategoryRankWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 商品品类销售额(单平台)
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT base.tname,base.tamount FROM " +
        "(SELECT c.\"Name\" AS tname,COALESCE(SUM(oo.\"AfterFoldingPrice\"),0) AS tamount,COUNT(DISTINCT \"ProductId\") AS  productCount " +
        "FROM songshu_cs_category c " +
        "LEFT JOIN songshu_cs_product p ON c.\"Id\" = p.\"CategoryId\" " +
        "LEFT JOIN (SELECT o.\"Id\",i.\"ProductId\",i.\"AfterFoldingPrice\" FROM songshu_cs_order o " +
        "            INNER JOIN songshu_cs_order_item i ON i.\"OrderId\" = o.\"Id\" " +
        "            INNER JOIN (SELECT DISTINCT \"MergePaymentNo\" " +
        "                        FROM (SELECT pr.\"MergePaymentNo\",MAX(pr.\"PaidTime\") AS paidTime " +
        "                              FROM (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" " +
        "                                    BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') " +
        "                                    AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D') " +
        "                                   ) pr GROUP BY \"MergePaymentNo\" " +
        "                             ) prr " +
        "                        WHERE prr.paidTime  BETWEEN ?1 AND ?2) r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" " +
        "            INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "            WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6, 7) AND o.\"Channel\" = ?3  " +
        "          ) oo ON oo.\"ProductId\" = p.\"Id\" " +
        "WHERE c.\"Id\" != 1  GROUP BY c.\"Name\" ORDER BY tamount DESC " +
        ")base WHERE base.productCount >0", nativeQuery = true)
    List<Object[]> getProductCategoryRankWithSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer platform);
}
