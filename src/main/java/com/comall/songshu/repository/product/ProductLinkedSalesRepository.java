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


    /**
     * 商品销售量TOP（全平台）
     * @param beginTime
     * @param endTime
     * @param topCount
     * @return
     */
    @Query(value = "SELECT p.\"Id\" AS productId, p.\"Name\" AS productName, base.salesCount AS salesCount FROM " +
        "    (SELECT i.\"ProductId\"  AS productId, SUM(i.\"Quantity\") AS salesCount " +
        "     FROM songshu_cs_order_item i " +
        "         INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\" " +
        "         INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "         INNER JOIN (SELECT DISTINCT t.\"MergePaymentNo\" FROM  " +
        "             (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime " +
        "                           FROM (SELECT * FROM songshu_cs_payment_record " +
        "                                 WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D') " +
        "                                 AND (CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                           GROUP BY \"MergePaymentNo\") t " +
        "                     WHERE  t.paidTime BETWEEN ?1 AND ?2 ) cpr " +
        "             ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "     WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) " +
        "     GROUP BY i.\"ProductId\" " +
        "     ORDER BY salesCount DESC " +
        "     LIMIT ?3 ) base " +
        "    INNER JOIN songshu_cs_product p ON p.\"Id\" = base.productId " +
        "ORDER BY base.salesCount DESC " +
        "LIMIT ?3 ", nativeQuery = true)
    List<Object[]> getProductSalesAllPlatform(Timestamp beginTime, Timestamp endTime, Integer topCount);

    /**
     * 商品销售量TOP（单平台）
     * @param beginTime
     * @param endTime
     * @param plateForm
     * @param topCount
     * @return
     */
    @Query(value = "SELECT p.\"Id\" AS productId, p.\"Name\" AS productName, base.salesCount AS salesCount FROM " +
        "    (SELECT i.\"ProductId\"  AS productId, SUM(i.\"Quantity\") AS salesCount " +
        "     FROM songshu_cs_order_item i " +
        "         INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\" " +
        "         INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "         INNER JOIN (SELECT DISTINCT t.\"MergePaymentNo\" FROM  " +
        "             (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime " +
        "                           FROM (SELECT * FROM songshu_cs_payment_record " +
        "                                 WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D') " +
        "                                 AND (CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                           GROUP BY \"MergePaymentNo\") t " +
        "                     WHERE  t.paidTime BETWEEN ?1 AND ?2 ) cpr " +
        "             ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "     WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) AND o.\"Channel\" = ?3 " +
        "     GROUP BY i.\"ProductId\" " +
        "     ORDER BY salesCount DESC " +
        "     LIMIT ?4 ) base " +
        "    INNER JOIN songshu_cs_product p ON p.\"Id\" = base.productId " +
        "ORDER BY base.salesCount DESC " +
        "LIMIT ?4 ", nativeQuery = true)
    List<Object[]> getProductSalesSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm, Integer topCount);


    /**
     * 商品销售量搭配TOP（全平台）
     * @param beginTime
     * @param endTime
     * @param topCount
     * @param productId
     * @return
     */
    @Query(value = "SELECT p.\"Id\" AS productId, p.\"Name\" AS productName, base.salesCount AS salesCount FROM  " +
        "    (SELECT i.\"ProductId\" AS productId, SUM(i.\"Quantity\") AS salesCount FROM songshu_cs_order_item i  " +
        "         INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"  " +
        "         INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\"  " +
        "         INNER JOIN (SELECT DISTINCT t.\"MergePaymentNo\" FROM  " +
        "            (SELECT pr.\"MergePaymentNo\",  MAX(pr.\"PaidTime\") AS paidTime FROM  " +
        "                (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\"  " +
        "                 BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D')  " +
        "                 AND (CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr GROUP BY \"MergePaymentNo\") t  " +
        "                 WHERE t.paidTime BETWEEN ?1 AND ?2 ) cpr  " +
        "                 ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\"  " +
        "                 WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7)  " +
        "                 AND o.\"Id\" IN (SELECT DISTINCT (scoi.\"OrderId\") FROM songshu_cs_order_item scoi  " +
        "                              INNER JOIN songshu_cs_order co ON co.\"Id\" = scoi.\"OrderId\"  " +
        "                              INNER JOIN songshu_cs_order_payable cop ON cop.\"OrderId\" = co.\"Id\"  " +
        "                              INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM  " +
        "             (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\"  " +
        "              BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D')  " +
        "              AND ( CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr  " +
        "              GROUP BY \"MergePaymentNo\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\"  " +
        "              WHERE cop.\"PaymentStatus\" = 1 AND co.\"orderType\" IN (0, 1) AND co.\"OrderStatus\" NOT IN (6, 7) AND cpr.paidTime  " +
        "              BETWEEN ?1 AND ?2 AND scoi.\"ProductId\" = ?4)  " +
        "              AND i.\"ProductId\" != ?4  " +
        "     GROUP BY i.\"ProductId\"  " +
        "     ORDER BY salesCount DESC  " +
        "     LIMIT ?3 ) base  " +
        "    INNER JOIN songshu_cs_product p ON p.\"Id\" = base.productId  " +
        "ORDER BY base.salesCount DESC  " +
        "LIMIT ?3 ", nativeQuery = true)
    List<Object[]> getProductLinkedSalesAllPlatform(Timestamp beginTime, Timestamp endTime, Integer topCount,Integer productId);


    /**
     * 商品销售量搭配TOP（单平台）
     * @param beginTime
     * @param endTime
     * @param plateForm
     * @param topCount
     * @param productId
     * @return
     */
    @Query(value = "SELECT p.\"Id\" AS productId, p.\"Name\" AS productName, base.salesCount AS salesCount FROM  " +
        "    (SELECT i.\"ProductId\" AS productId, SUM(i.\"Quantity\") AS salesCount FROM songshu_cs_order_item i  " +
        "         INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"  " +
        "         INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\"  " +
        "         INNER JOIN (SELECT DISTINCT t.\"MergePaymentNo\" FROM  " +
        "            (SELECT pr.\"MergePaymentNo\",  MAX(pr.\"PaidTime\") AS paidTime FROM  " +
        "                (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\"  " +
        "                 BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D')  " +
        "                 AND (CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr GROUP BY \"MergePaymentNo\") t  " +
        "                 WHERE t.paidTime BETWEEN ?1 AND ?2 ) cpr  " +
        "                 ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\"  " +
        "                 WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7)  " +
        "                 AND o.\"Id\" IN (SELECT DISTINCT (scoi.\"OrderId\") FROM songshu_cs_order_item scoi  " +
        "                              INNER JOIN songshu_cs_order co ON co.\"Id\" = scoi.\"OrderId\"  " +
        "                              INNER JOIN songshu_cs_order_payable cop ON cop.\"OrderId\" = co.\"Id\"  " +
        "                              INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM  " +
        "             (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\"  " +
        "              BETWEEN (CAST( ?1 AS TIMESTAMP) - INTERVAL '1 D')  " +
        "              AND ( CAST( ?2 AS TIMESTAMP) + INTERVAL '1 D')) pr  " +
        "              GROUP BY \"MergePaymentNo\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\"  " +
        "              WHERE cop.\"PaymentStatus\" = 1 AND co.\"orderType\" IN (0, 1) AND co.\"OrderStatus\" NOT IN (6, 7) AND cpr.paidTime  " +
        "              BETWEEN ?1 AND ?2 AND scoi.\"ProductId\" = ?5 AND co.\"Channel\" = ?3 )  " +
        "              AND i.\"ProductId\" != ?5  " +
        "     GROUP BY i.\"ProductId\"  " +
        "     ORDER BY salesCount DESC  " +
        "     LIMIT ?4 ) base  " +
        "    INNER JOIN songshu_cs_product p ON p.\"Id\" = base.productId  " +
        "ORDER BY base.salesCount DESC  " +
        "LIMIT ?4 ", nativeQuery = true)
    List<Object[]> getProductLinkedSalesSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm, Integer topCount,Integer productId);


    /**
     * 商品图片地址
     * @param productId
     * @return
     */
    @Query(value = "SELECT  CONCAT('/',pic.\"Location\",'/',pic.\"Digest\",pic.\"Extension\") AS picUrl FROM  songshu_cs_product_picture pp " +
        "INNER JOIN songshu_cs_picture pic ON pic.\"Id\" = pp.\"pictureId\" WHERE pp.\"productId\" = ?1 ORDER BY pic.\"Id\" ASC  LIMIT 1 ", nativeQuery = true)
    String getProductPicUrl(Integer productId);
}
