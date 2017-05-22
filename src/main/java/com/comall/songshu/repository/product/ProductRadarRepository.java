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
public interface ProductRadarRepository  extends JpaRepository<Author,Long> {

    /**
     * 商品雷达图（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT main.categoryName, COALESCE(main.revenue,0) AS revenue,COALESCE(main.saleNum,0) AS saleNum, COALESCE(main.cost,0) AS cost," +
        "COALESCE((main.revenue-main.cost)/main.revenue,0) as gross ,COALESCE(main.productCount,0) AS productCount " +
        "FROM(SELECT base.categoryName, sum(base.\"AfterFoldingPrice\") AS revenue,sum(base.\"Quantity\") as saleNum, sum(base.cost*base.\"Quantity\") AS cost," +
        "          count(DISTINCT base.\"ProductId\") as productCount ,base.categoryId" +
        "  FROM(SELECT c.\"Name\" as categoryName,i.\"AfterFoldingPrice\",i.\"Quantity\",i.\"ProductId\",c.\"Id\" AS categoryId" +
        "        ,CASE WHEN COALESCE(i.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE i.\"ReferCost\" END  AS cost" +
        "        FROM songshu_cs_category c" +
        "           INNER JOIN songshu_cs_product p ON p.\"CategoryId\" = c.\"Id\"" +
        "           INNER JOIN songshu_cs_goods g ON g.\"ProductId\" = p.\"Id\"" +
        "           INNER JOIN songshu_cs_order_item i ON i.\"GoodsId\" = g.\"Id\"" +
        "           INNER JOIN (SELECT o.\"Id\" FROM songshu_cs_order o" +
        "                       JOIN (SELECT DISTINCT \"MergePaymentNo\"" +
        "                       FROM (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime" +
        "                             FROM (select * from songshu_cs_payment_record" +
        "                             WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D')" +
        "                             AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr" +
        "                             GROUP BY \"MergePaymentNo\") prr" +
        "                       WHERE  prr.paidTime BETWEEN ?1 AND ?2) r" +
        "                       ON o.\"OrderNumber\" = r.\"MergePaymentNo\"" +
        "                       JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"" +
        "                       WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\"" +
        "                       NOT IN (6, 7) AND o.\"Channel\" IN (0, 1, 2, 3, 5)" +
        "                       ) oo ON oo.\"Id\" = i.\"OrderId\"" +
        "       )base  WHERE base.categoryId != 1 GROUP BY base.categoryName ,base.categoryId ORDER BY revenue DESC" +
        ")main", nativeQuery = true)
    List<Object[]> getProductRadarWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 商品雷达图（单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT main.categoryName, COALESCE(main.revenue,0) AS revenue,COALESCE(main.saleNum,0) AS saleNum, COALESCE(main.cost,0) AS cost," +
        "COALESCE((main.revenue-main.cost)/main.revenue,0) as gross ,COALESCE(main.productCount,0) AS productCount " +
        "FROM(SELECT base.categoryName, sum(base.\"AfterFoldingPrice\") AS revenue,sum(base.\"Quantity\") as saleNum, sum(base.cost*base.\"Quantity\") AS cost," +
        "          count(DISTINCT base.\"ProductId\") as productCount ,base.categoryId" +
        "  FROM(SELECT c.\"Name\" as categoryName,i.\"AfterFoldingPrice\",i.\"Quantity\",i.\"ProductId\",c.\"Id\" AS categoryId" +
        "        ,CASE WHEN COALESCE(i.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE i.\"ReferCost\" END  AS cost" +
        "        FROM songshu_cs_category c" +
        "           INNER JOIN songshu_cs_product p ON p.\"CategoryId\" = c.\"Id\"" +
        "           INNER JOIN songshu_cs_goods g ON g.\"ProductId\" = p.\"Id\"" +
        "           INNER JOIN songshu_cs_order_item i ON i.\"GoodsId\" = g.\"Id\"" +
        "           INNER JOIN (SELECT o.\"Id\" FROM songshu_cs_order o" +
        "                       JOIN (SELECT DISTINCT \"MergePaymentNo\"" +
        "                       FROM (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime" +
        "                             FROM (select * from songshu_cs_payment_record" +
        "                             WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D')" +
        "                             AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr" +
        "                             GROUP BY \"MergePaymentNo\") prr" +
        "                       WHERE  prr.paidTime BETWEEN ?1 AND ?2) r" +
        "                       ON o.\"OrderNumber\" = r.\"MergePaymentNo\"" +
        "                       JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"" +
        "                       WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\"" +
        "                       NOT IN (6, 7) AND o.\"Channel\" = ?3 " +
        "                       ) oo ON oo.\"Id\" = i.\"OrderId\"" +
        "       )base  WHERE base.categoryId != 1 GROUP BY base.categoryName ,base.categoryId ORDER BY revenue DESC" +
        ")main", nativeQuery = true)
    List<Object[]> getProductRadarWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);
}
