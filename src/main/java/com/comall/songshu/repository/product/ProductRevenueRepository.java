package com.comall.songshu.repository.product;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 品类销售统计
 *
 * @author liushengling
 * @create 2017-05-02-16:21
 **/
public interface ProductRevenueRepository extends JpaRepository<Author,Long> {


    /**
     * 商品的品类、名称、销售额、成本、订单量、毛利率（全平台）
     * @param beginTime
     * @param endTime
     * @param topCount
     * @return
     */
    @Query(value = "SELECT c.\"Name\" AS categoryName,p.\"Name\" AS productName,main.revenue,main.cost,main.salesCount," +
        "    main.grossMaringRate,p.\"Id\" AS productId,p.\"Code\" AS productCode FROM" +
        "    (SELECT calbase.\"ProductId\",calbase.revenue,calbase.cost,calbase.salesCount," +
        "         COALESCE((calbase.revenue - calbase.cost) / calbase.revenue, 0) AS grossMaringRate FROM" +
        "         (SELECT base.\"ProductId\",COALESCE(SUM(base.\"AfterFoldingPrice\"), 0) AS revenue,count(DISTINCT base.orderId) AS salesCount," +
        "              COALESCE(SUM(base.cost * base.\"Quantity\"), 0) AS cost FROM" +
        "             (SELECT g.\"ProductId\",g.\"Id\" AS goodsId,i.\"Quantity\", CASE WHEN COALESCE(i.\"ReferCost\", 0) = 0 THEN g.\"CostPrice\" ELSE i.\"ReferCost\" END AS cost," +
        "                    o.\"Id\" AS orderId,i.\"AfterFoldingPrice\" FROM songshu_cs_order_item i" +
        "                    INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"" +
        "                    INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\"" +
        "                    INNER JOIN (SELECT DISTINCT t.\"MergePaymentNo\" FROM" +
        "                    (SELECT pr.\"MergePaymentNo\",MAX(pr.\"PaidTime\") AS paidTime FROM" +
        "                        (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\"" +
        "                         BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D')" +
        "                         AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr GROUP BY \"MergePaymentNo\") t" +
        "                         WHERE t.paidTime BETWEEN ?1 AND ?2) cpr" +
        "                         ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\"" +
        "                    INNER JOIN songshu_cs_goods g ON g.\"Id\" = i.\"GoodsId\"" +
        "                WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7)" +
        "               ) base" +
        "          GROUP BY base.\"ProductId\" ORDER BY revenue DESC LIMIT ?3" +
        "         ) calbase" +
        "    ) main" +
        "    INNER JOIN songshu_cs_product p ON p.\"Id\" = main.\"ProductId\"" +
        "    INNER JOIN songshu_cs_category c ON c.\"Id\" = p.\"CategoryId\"" +
        "ORDER BY main.revenue DESC " +
        "LIMIT ?3", nativeQuery = true)
    List<Object[]> getProductRevenueAllPlatform(Timestamp beginTime, Timestamp endTime,Integer topCount);


    /**
     * 商品的品类、名称、销售额、成本、订单量、毛利率（单平台）
     * @param beginTime
     * @param endTime
     * @param plateForm
     * @param topCount
     * @return
     */
    @Query(value = "SELECT c.\"Name\" AS categoryName,p.\"Name\" AS productName,main.revenue,main.cost,main.salesCount," +
        "    main.grossMaringRate,p.\"Id\" AS productId,p.\"Code\" AS productCode FROM" +
        "    (SELECT calbase.\"ProductId\",calbase.revenue,calbase.cost,calbase.salesCount," +
        "         COALESCE((calbase.revenue - calbase.cost) / calbase.revenue, 0) AS grossMaringRate FROM" +
        "         (SELECT base.\"ProductId\",COALESCE(SUM(base.\"AfterFoldingPrice\"), 0) AS revenue,count(DISTINCT base.orderId) AS salesCount," +
        "              COALESCE(SUM(base.cost * base.\"Quantity\"), 0) AS cost FROM" +
        "             (SELECT g.\"ProductId\",g.\"Id\" AS goodsId,i.\"Quantity\", CASE WHEN COALESCE(i.\"ReferCost\", 0) = 0 THEN g.\"CostPrice\" ELSE i.\"ReferCost\" END AS cost," +
        "                    o.\"Id\" AS orderId,i.\"AfterFoldingPrice\" FROM songshu_cs_order_item i" +
        "                    INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"" +
        "                    INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\"" +
        "                    INNER JOIN (SELECT DISTINCT t.\"MergePaymentNo\" FROM" +
        "                    (SELECT pr.\"MergePaymentNo\",MAX(pr.\"PaidTime\") AS paidTime FROM" +
        "                        (SELECT * FROM songshu_cs_payment_record WHERE \"PaymentModeType\" = 2 AND \"PaidTime\"" +
        "                         BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D')" +
        "                         AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr GROUP BY \"MergePaymentNo\") t" +
        "                         WHERE t.paidTime BETWEEN ?1 AND ?2) cpr" +
        "                         ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\"" +
        "                    INNER JOIN songshu_cs_goods g ON g.\"Id\" = i.\"GoodsId\"" +
        "                WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) AND o.\"Channel\" = ?3 " +
        "               ) base" +
        "          GROUP BY base.\"ProductId\" ORDER BY revenue DESC LIMIT ?4" +
        "         ) calbase" +
        "    ) main" +
        "    INNER JOIN songshu_cs_product p ON p.\"Id\" = main.\"ProductId\"" +
        "    INNER JOIN songshu_cs_category c ON c.\"Id\" = p.\"CategoryId\"" +
        "ORDER BY main.revenue DESC " +
        "LIMIT ?4", nativeQuery = true)
    List<Object[]> getProductRevenueSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer plateForm,Integer topCount);




    /**
     * 加入购物车次数（全平台）
     * @param beginTime
     * @param endTime
     * @param productCode
     * @return
     */
    @Query(value = "SELECT COUNT(1) AS totalCount FROM songshu_shence_events e WHERE e.event ='AddCardEvent' AND e.productcode = ?3  " +
        "AND e.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getAddCartTimesAllPlatform(Timestamp beginTime, Timestamp endTime,String productCode);

    /**
     * 加入购物车次数（单平台）
     * @param beginTime
     * @param endTime
     * @param productCode
     * @param plateFormName
     * @return
     */
    @Query(value = "SELECT COUNT(1) AS totalCount FROM songshu_shence_events e WHERE e.event ='AddCardEvent' AND e.productcode = ?3  " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform =?4 ", nativeQuery = true)
    Integer getAddCartTimesSinglePlatform(Timestamp beginTime, Timestamp endTime,String productCode, String plateFormName);

    /**
     * 收藏数（全平台）
     * @param beginTime
     * @param endTime
     * @param productCode
     * @return
     */
    @Query(value = "SELECT COUNT(1) AS totalCount FROM songshu_shence_events WHERE event ='CollectProductEvent' AND productcode = ?3  " +
        "AND times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getCollectionCountAllPlatform(Timestamp beginTime, Timestamp endTime,String productCode);

    /**
     * 收藏数（单平台）
     * @param beginTime
     * @param endTime
     * @param productCode
     * @param plateFormName
     * @return
     */
    @Query(value = "SELECT COUNT(1) AS totalCount FROM songshu_shence_events e WHERE e.event ='CollectProductEvent' AND e.productcode =?3  " +
        "AND e.times BETWEEN ?1 AND ?2 AND e.platform = ?4", nativeQuery = true)
    Integer getCollectionCountSinglePlatform(Timestamp beginTime, Timestamp endTime,String productCode, String plateFormName);



    /**
     * 商品消费用户数（全平台）
     * @param beginTime
     * @param endTime
     * @param productId
     * @return
     */
    @Query(value = "SELECT count(DISTINCT (o.\"MemberId\")) AS memberCount " +
        "FROM songshu_cs_order_item i" +
        "    INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"" +
        "    INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\"" +
        "    INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime" +
        "                FROM (SELECT * FROM songshu_cs_payment_record WHERE \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D')" +
        "                AND  (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr" +
        "                GROUP BY \"MergePaymentNo\") prr ON prr.\"MergePaymentNo\" = op.\"MergePaymentId\"" +
        "WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) AND i.\"ProductId\" = ?3 " +
        "AND prr.paidTime BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getProductConsumerCountAllPlatform(Timestamp beginTime, Timestamp endTime,Integer productId);

    /**
     * 商品消费用户数（单平台）
     * @param beginTime
     * @param endTime
     * @param productId
     * @param plateForm
     * @return
     */
    @Query(value = "SELECT count(DISTINCT (o.\"MemberId\")) AS memberCount " +
        "FROM songshu_cs_order_item i" +
        "    INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\"" +
        "    INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\"" +
        "    INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime" +
        "                FROM (SELECT * FROM songshu_cs_payment_record WHERE \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D')" +
        "                AND  (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr" +
        "                GROUP BY \"MergePaymentNo\") prr ON prr.\"MergePaymentNo\" = op.\"MergePaymentId\"" +
        "WHERE op.\"PaymentStatus\" = 1 AND o.\"orderType\" IN (0, 1) AND o.\"OrderStatus\" NOT IN (6, 7) AND i.\"ProductId\" = ?3 " +
        "AND prr.paidTime BETWEEN ?1 AND ?2 AND o.\"Channel\" = ?4 ", nativeQuery = true)
    Integer getProductConsumerCountSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer productId, Integer plateForm);

    /**
     * 退出商品页面访客数（全平台）
     * @param beginTime
     * @param endTime
     * @param categoriesProductLike
     * @param indexProductLike
     * @param cartLike
     * @param userProductLike
     * @param integralProductLike
     * @return
     */
//    SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e
//    WHERE e.event = '$pageview'
//    AND (e.referrer like '#/tabs/categories/productInfo?productId=100100436%'
//        OR e.referrer like '#/tabs/cart/productInfo?productId=100100436%'
//        OR e.referrer like '#/tabs/index/productInfo?productId=100100436%'
//        OR e.referrer like '#/tabs/user/productInfo?productId=100100436%'
//        OR e.referrer like '#/tabs/integral/integralInfo?id=100100436&type=0%')
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00';
    @Query(value = "SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e " +
        "WHERE e.event = '$pageview' " +
        "AND (e.referrer like ?3 OR e.referrer like ?4 OR e.referrer like ?5 OR e.referrer like ?6 OR e.referrer like ?7 )" +
        "AND e.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getProductPageLeaveVisitorsAllPlatform(Timestamp beginTime, Timestamp endTime
        ,String categoriesProductLike,String indexProductLike,String cartLike,String userProductLike,String integralProductLike);

    /**
     * 退出商品页面访客数（单平台）
     * @param beginTime
     * @param endTime
     * @param plateFormName
     * @param categoriesProductLike
     * @param indexProductLike
     * @param cartLike
     * @param userProductLike
     * @param integralProductLike
     * @return
     */
//    SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e
//    WHERE e.event = '$pageview'
//    AND (e.referrer like '#/tabs/categories/productInfo?productId=100100436%'
//        OR e.referrer like '#/tabs/cart/productInfo?productId=100100436%'
//        OR e.referrer like '#/tabs/index/productInfo?productId=100100436%'
//        OR e.referrer like '#/tabs/user/productInfo?productId=100100436%'
//        OR e.referrer like '#/tabs/integral/integralInfo?id=100100436&type=0%')
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform ='ios';
    @Query(value = "SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e " +
        "WHERE e.event = '$pageview' " +
        "AND (e.referrer like ?4 OR e.referrer like ?5 OR e.referrer like ?6 OR e.referrer like ?7 OR e.referrer like ?8 )" +
        "AND e.times BETWEEN ?1 AND ?2 AND  e.platform =?3 ", nativeQuery = true)
    Integer getProductPageLeaveVisitorsSinglePlatform(Timestamp beginTime, Timestamp endTime,String plateFormName
        ,String categoriesProductLike,String indexProductLike,String cartLike,String userProductLike,String integralProductLike);

    /**
     * 商品页面访客数（全平台）
     * @param beginTime
     * @param endTime
     * @param categoriesProductLike
     * @param indexProductLike
     * @param cartLike
     * @param userProductLike
     * @param integralProductLike
     * @return
     */
//    SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e
//    WHERE e.event = '$pageview'
//    AND (e.url like '#/tabs/categories/productInfo?productId=100100436%'
//        OR e.url like '#/tabs/cart/productInfo?productId=100100436%'
//        OR e.url like '#/tabs/index/productInfo?productId=100100436%'
//        OR e.url like '#/tabs/user/productInfo?productId=100100436%'
//        OR e.url like '#/tabs/integral/integralInfo?id=100100436&type=0%')
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00';
    @Query(value = "SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e " +
        "WHERE e.event = '$pageview' " +
        "AND (e.url like ?3 OR e.url like ?4 OR e.url like ?5 OR e.url like ?6 OR e.url like ?7 )" +
        "AND e.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getProductPageVisitorsAllPlatform(Timestamp beginTime, Timestamp endTime
        ,String categoriesProductLike,String indexProductLike,String cartLike,String userProductLike,String integralProductLike);

    /**
     * 商品页面访客数（单平台）
     * @param beginTime
     * @param endTime
     * @param plateFormName
     * @param categoriesProductLike
     * @param indexProductLike
     * @param cartLike
     * @param userProductLike
     * @param integralProductLike
     * @return
     */
//    SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e
//    WHERE e.event = '$pageview'
//    AND (e.url like '#/tabs/categories/productInfo?productId=100100436%'
//        OR e.url like '#/tabs/cart/productInfo?productId=100100436%'
//        OR e.url like '#/tabs/index/productInfo?productId=100100436%'
//        OR e.url like '#/tabs/user/productInfo?productId=100100436%'
//        OR e.url like '#/tabs/integral/integralInfo?id=100100436&type=0%')
//    AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform ='ios';
    @Query(value = "SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e " +
        "WHERE e.event = '$pageview' " +
        "AND (e.url like ?4 OR e.url like ?5 OR e.url like ?6 OR e.url like ?7 OR e.url like ?8 )" +
        "AND e.times BETWEEN ?1 AND ?2 AND  e.platform =?3", nativeQuery = true)
    Integer getProductPageVisitorsSinglePlatform(Timestamp beginTime, Timestamp endTime,String plateFormName
        ,String categoriesProductLike,String indexProductLike,String cartLike,String userProductLike,String integralProductLike);
}
