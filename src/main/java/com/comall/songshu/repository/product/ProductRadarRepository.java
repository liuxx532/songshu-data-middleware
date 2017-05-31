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
    @Query(value = "SELECT main.categoryName, main.revenue,main.saleNum, main.cost,COALESCE((main.revenue-main.cost)/(CASE WHEN main.revenue = 0 THEN NULL ELSE main.revenue END),0) AS gross " +
        "      ,COALESCE(main.productCount,0) AS productCount " +
        "FROM (SELECT base.categoryName, COALESCE(SUM(base.\"AfterFoldingPrice\"),0) AS revenue,COALESCE(SUM(base.\"Quantity\"),0) AS saleNum, " +
        "             COALESCE(SUM(base.cost*base.\"Quantity\"),0) AS cost,COUNT(DISTINCT base.productId) AS productCount " +
        "     FROM(SELECT c.\"Name\" as categoryName,oo.\"AfterFoldingPrice\",oo.\"Quantity\",p.\"Id\" AS productId ,c.\"Id\" AS categoryId,c.\"Searchable\" " +
        "          ,CASE WHEN COALESCE(oo.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE oo.\"ReferCost\" END  AS cost " +
        "          FROM songshu_cs_category c " +
        "          LEFT JOIN songshu_cs_product p ON p.\"CategoryId\" = c.\"Id\" " +
        "          LEFT JOIN songshu_cs_goods g ON g.\"ProductId\" = p.\"Id\" " +
        "          LEFT JOIN (SELECT o.\"Id\",i.\"ProductId\",i.\"AfterFoldingPrice\",i.\"Quantity\",i.\"ReferCost\" FROM songshu_cs_order o " +
        "                      INNER JOIN songshu_cs_order_item i ON i.\"OrderId\" = o.\"Id\" " +
        "                      INNER JOIN (SELECT DISTINCT \"MergePaymentNo\" " +
        "                                  FROM (SELECT pr.\"MergePaymentNo\",MAX(pr.\"PaidTime\") AS paidTime " +
        "                                        FROM (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" " +
        "                                        BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') " +
        "                                        AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D') " +
        "                                             ) pr GROUP BY \"MergePaymentNo\" " +
        "                                       ) prr " +
        "                                  WHERE prr.paidTime  BETWEEN ?1 AND ?2) r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" " +
        "                      INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "                      WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6, 7) AND o.\"Channel\" IN (0, 1, 2, 3, 5) " +
        "                    ) oo ON oo.\"ProductId\" = p.\"Id\" " +
        "         )base  WHERE base.categoryId !=1  AND base.categoryName != '投食卡' GROUP BY base.categoryName  ORDER BY revenue DESC " +
        ")main WHERE  main.productCount>0;", nativeQuery = true)
    List<Object[]> getProductRadarWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 商品雷达图（单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT main.categoryName, main.revenue,main.saleNum, main.cost,COALESCE((main.revenue-main.cost)/(CASE WHEN main.revenue = 0 THEN NULL ELSE main.revenue END),0) AS gross " +
        "      ,COALESCE(main.productCount,0) AS productCount " +
        "FROM (SELECT base.categoryName, COALESCE(SUM(base.\"AfterFoldingPrice\"),0) AS revenue,COALESCE(SUM(base.\"Quantity\"),0) AS saleNum, " +
        "             COALESCE(SUM(base.cost*base.\"Quantity\"),0) AS cost,COUNT(DISTINCT base.productId) AS productCount " +
        "     FROM(SELECT c.\"Name\" as categoryName,oo.\"AfterFoldingPrice\",oo.\"Quantity\",p.\"Id\" AS productId ,c.\"Id\" AS categoryId,c.\"Searchable\" " +
        "          ,CASE WHEN COALESCE(oo.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE oo.\"ReferCost\" END  AS cost " +
        "          FROM songshu_cs_category c " +
        "          LEFT JOIN songshu_cs_product p ON p.\"CategoryId\" = c.\"Id\" " +
        "          LEFT JOIN songshu_cs_goods g ON g.\"ProductId\" = p.\"Id\" " +
        "          LEFT JOIN (SELECT o.\"Id\",i.\"ProductId\",i.\"AfterFoldingPrice\",i.\"Quantity\",i.\"ReferCost\" FROM songshu_cs_order o " +
        "                      INNER JOIN songshu_cs_order_item i ON i.\"OrderId\" = o.\"Id\" " +
        "                      INNER JOIN (SELECT DISTINCT \"MergePaymentNo\" " +
        "                                  FROM (SELECT pr.\"MergePaymentNo\",MAX(pr.\"PaidTime\") AS paidTime " +
        "                                        FROM (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" " +
        "                                        BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') " +
        "                                        AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D') " +
        "                                             ) pr GROUP BY \"MergePaymentNo\" " +
        "                                       ) prr " +
        "                                  WHERE prr.paidTime  BETWEEN ?1 AND ?2) r ON o.\"OrderNumber\" = r.\"MergePaymentNo\" " +
        "                      INNER JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\" " +
        "                      WHERE p.\"PaymentStatus\" = 1 AND o.\"OrderStatus\" NOT IN (6, 7) AND o.\"Channel\" =?3  " +
        "                    ) oo ON oo.\"ProductId\" = p.\"Id\" " +
        "         )base  WHERE base.categoryId !=1  AND base.categoryName != '投食卡' GROUP BY base.categoryName  ORDER BY revenue DESC " +
        ")main WHERE  main.productCount>0;", nativeQuery = true)
    List<Object[]> getProductRadarWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);
}
