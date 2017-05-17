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



//    SELECT SELECT p."Id" AS productId ,g."Id" AS goodsId ,p."Code" as productCode, c."Name" as categoryName, p."Name" AS  productName, main.revenue,main.cost,main.salesCount,main.grossMaringRate FROM(
//        SELECT base.productId,base.revenue,base.cost, base.salesCount,COALESCE((base.revenue-base.cost)/base.revenue,0) AS grossMaringRate FROM
//        (SELECT i."ProductId" AS productId, COALESCE(SUM(i."AfterFoldingPrice"),0) AS revenue,count(DISTINCT o."Id") AS  salesCount
//,COALESCE(SUM(i."ReferCost" * i."Quantity"),0) AS cost
//    FROM songshu_cs_order_item i
//    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
//        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
//    GROUP BY i."ProductId" ORDER BY revenue DESC LIMIT 20 ) base) main
//    INNER JOIN songshu_cs_product p ON p."Id" = main.productId
//    INNER JOIN songshu_cs_goods g ON p."Id" = g."ProductId"
//    INNER JOIN songshu_cs_category c ON c."Id" = p."CategoryId"
//    ORDER BY main.revenue DESC LIMIT 20;
    @Query(value = "SELECT p.\"Id\" AS productId ,g.\"Id\" AS goodsId,p.\"Code\" AS productCode,  c.\"Name\" AS categoryName, p.\"Name\" AS  productName, main.revenue,main.cost,main.salesCount, main.grossMaringRate FROM( " +
        "SELECT base.productId,base.revenue,base.cost, base.salesCount,COALESCE((base.revenue-base.cost)/base.revenue,0) AS grossMaringRate FROM " +
        "(SELECT i.\"ProductId\" AS productId,COALESCE(SUM(i.\"AfterFoldingPrice\"),0) AS revenue,count(DISTINCT o.\"Id\") AS  salesCount " +
        ",COALESCE(SUM(i.\"ReferCost\" * i.\"Quantity\"),0) AS cost " +
        "FROM songshu_cs_order_item i " +
        "INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\" " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN ( SELECT DISTINCT t.\"MergePaymentNo\" FROM songshu_cs_payment_record t " +
        "        WHERE \"PaymentModeType\"= 2 AND \"PaidTime\" BETWEEN ?1 AND ?2)cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" IN(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) " +
        "GROUP BY i.\"ProductId\" ORDER BY revenue DESC LIMIT ?3 ) base) main " +
        "INNER JOIN songshu_cs_product p ON p.\"Id\" = main.productId " +
        "INNER JOIN songshu_cs_goods g ON p.\"Id\" = g.\"ProductId\" " +
        "INNER JOIN songshu_cs_category c ON c.\"Id\" = p.\"CategoryId\" " +
        "ORDER BY main.revenue DESC LIMIT ?3 ", nativeQuery = true)
    List<Object[]> getProductRevenueAllPlatform(Timestamp beginTime, Timestamp endTime,Integer topCount);



//    SELECT SELECT p."Id" AS productId ,g."Id" AS goodsId,p."Code" as productCode, c."Name" as categoryName, p."Name" AS  productName, main.revenue,main.cost,main.salesCount,main.grossMaringRate FROM(
//        SELECT base.productId,base.revenue,base.cost, base.salesCount,COALESCE((base.revenue-base.cost)/base.revenue,0) AS grossMaringRate FROM
//        (SELECT i."ProductId" AS productId, COALESCE(SUM(i."AfterFoldingPrice"),0) AS revenue,count(DISTINCT o."Id") AS  salesCount
//,COALESCE(SUM(i."ReferCost" * i."Quantity"),0) AS cost
//    FROM songshu_cs_order_item i
//    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
//        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND o."Channel" = 1
//    GROUP BY i."ProductId" ORDER BY revenue DESC LIMIT 20 ) base) main
//    INNER JOIN songshu_cs_product p ON p."Id" = main.productId
//    INNER JOIN songshu_cs_goods g ON p."Id" = g."ProductId"
//    INNER JOIN songshu_cs_category c ON c."Id" = p."CategoryId"
//    ORDER BY main.revenue DESC LIMIT 20;
    @Query(value = "SELECT p.\"Id\" AS productId ,g.\"Id\" AS goodsId,p.\"Code\" AS productCode, c.\"Name\" as categoryName, p.\"Name\" AS  productName, main.revenue,main.cost,main.salesCount,main.grossMaringRate FROM( " +
        "SELECT base.productId,base.revenue,base.cost,base.salesCount, COALESCE((base.revenue-base.cost)/base.revenue,0) AS grossMaringRate FROM " +
        " (SELECT i.\"ProductId\" AS productId, COALESCE(SUM(i.\"AfterFoldingPrice\"),0) AS revenue,count(DISTINCT o.\"Id\") AS  salesCount " +
        ",COALESCE(SUM(i.\"ReferCost\" * i.\"Quantity\"),0) AS cost " +
        "FROM songshu_cs_order_item i " +
        "INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\" " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN ( SELECT DISTINCT t.\"MergePaymentNo\" FROM songshu_cs_payment_record t " +
        "WHERE \"PaymentModeType\"= 2 AND \"PaidTime\" BETWEEN ?1 AND ?2)cpr ON cpr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" in(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) AND o.\"Channel\" = ?3 " +
        "GROUP BY i.\"ProductId\" ORDER BY revenue DESC LIMIT ?4 ) base) main " +
        "INNER JOIN songshu_cs_product p ON p.\"Id\" = main.productId " +
        "INNER JOIN songshu_cs_goods g ON p.\"Id\" = g.\"ProductId\" " +
        "INNER JOIN songshu_cs_category c ON c.\"Id\" = p.\"CategoryId\" " +
        "ORDER BY main.revenue DESC LIMIT ?4 ", nativeQuery = true)
    List<Object[]> getProductRevenueSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer plateForm,Integer topCount);




    /**
     * 加入购物车次数（全平台）
     * @param beginTime
     * @param endTime
     * @param productCode
     * @return
     */
//    SELECT COUNT(1) AS totalCount FROM songshu_shence_events WHERE event ='AddCardEvent' AND productcode ='6956511900046'
//    AND times BETWEEN '2016-11-01 00:00:00' AND '2016-12-01 00:00:00' ;
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
//    SELECT COUNT(1) AS totalCount FROM songshu_shence_events WHERE event ='AddCardEvent' AND productcode ='6956511900046'
//    AND times BETWEEN '2016-11-01 00:00:00' AND '2016-12-01 00:00:00' AND platform ='ios';
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
//    SELECT COUNT(1) AS totalCount FROM songshu_shence_events e WHERE e.event ='CollectProductEvent' AND e.productcode ='6956511900046'
//    AND e.times BETWEEN '2016-11-01 00:00:00' AND '2016-12-01 00:00:00' ;
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
//    SELECT COUNT(1) AS totalCount FROM songshu_shence_events e WHERE e.event ='CollectProductEvent' AND e.productcode ='6956511900046'
//    AND e.times BETWEEN '2016-11-01 00:00:00' AND '2016-12-01 00:00:00' AND e.platform ='ios';
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
//    SELECT count(DISTINCT(o."MemberId")) AS  memberCount   FROM songshu_cs_order_item i
//    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND i."ProductId" = 100100452
//    AND pr."PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00';
    @Query(value = "SELECT count(DISTINCT(o.\"MemberId\")) AS  memberCount   FROM songshu_cs_order_item i " +
        "INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\" " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record pr ON pr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" IN(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) AND i.\"ProductId\" = ?3 " +
        "AND pr.\"PaidTime\" BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Integer getProductConsumerCountAllPlatform(Timestamp beginTime, Timestamp endTime,Integer productId);

    /**
     * 商品消费用户数（单平台）
     * @param beginTime
     * @param endTime
     * @param productId
     * @param plateForm
     * @return
     */
//    SELECT count(DISTINCT(o."MemberId")) AS  memberCount   FROM songshu_cs_order_item i
//    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
//    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
//    INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
//    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND i."ProductId" = 100100452
//    AND pr."PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND o."Channel" = 1;
    @Query(value = "SELECT count(DISTINCT(o.\"MemberId\")) AS  memberCount   FROM songshu_cs_order_item i " +
        "INNER JOIN songshu_cs_order o ON o.\"Id\" = i.\"OrderId\" " +
        "INNER JOIN songshu_cs_order_payable op ON op.\"OrderId\" = o.\"Id\" " +
        "INNER JOIN songshu_cs_payment_record pr ON pr.\"MergePaymentNo\" = op.\"MergePaymentId\" " +
        "WHERE  op.\"PaymentStatus\" = 1 AND  o.\"orderType\" IN(0,1) AND  o.\"OrderStatus\" NOT IN (6,7) AND i.\"ProductId\" = ?3 " +
        "AND pr.\"PaidTime\" BETWEEN ?1 AND ?2 AND o.\"Channel\" = ?4", nativeQuery = true)
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
