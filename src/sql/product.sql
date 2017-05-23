SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM (select * from songshu_cs_payment_record
WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
GROUP BY "MergePaymentNo", "PaymentModeType";
-- 商品页面雷达图 包括：销售额、销售数量、包含商品数、商品成本、毛利率   对应 ProductRadarRepository 商品雷达图
-- 销售额：统计时间段内所有已支付金额，不包含已取消/已关闭状态
-- 销售数量：统计时间段内所有已支付商品数量，不包含已取消/已关闭状态
-- 包含商品数：统计时间段内品类包含的商品数量
-- 商品成本：统计时间段内所有已支付订单商品成本，不包含已取消/已关闭状态
-- 毛利率：统计时间段内，（销售额-商品成本)÷销售额


SELECT main.categoryName, COALESCE(main.revenue,0) AS revenue,COALESCE(main.saleNum,0) AS saleNum, COALESCE(main.cost,0) AS cost,
COALESCE((main.revenue-main.cost)/main.revenue,0) as gross ,COALESCE(main.productCount,0) AS productCount
FROM(SELECT base.categoryName, sum(base."AfterFoldingPrice") AS revenue,sum(base."Quantity") as saleNum, sum(base.cost*base."Quantity") AS cost,
          count(DISTINCT base."ProductId") as productCount ,base.categoryId
  FROM(SELECT c."Name" as categoryName,i."AfterFoldingPrice",i."Quantity",i."ProductId",c."Id" AS categoryId
        ,CASE WHEN COALESCE(i."ReferCost",0) =0 THEN g."CostPrice" ELSE i."ReferCost" END  AS cost
        FROM songshu_cs_category c
           INNER JOIN songshu_cs_product p ON p."CategoryId" = c."Id"
           INNER JOIN songshu_cs_goods g ON g."ProductId" = p."Id"
           INNER JOIN songshu_cs_order_item i ON i."GoodsId" = g."Id"
           INNER JOIN (SELECT o."Id" FROM songshu_cs_order o
                       JOIN (SELECT DISTINCT "MergePaymentNo"
                       FROM (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime
                             FROM (select * from songshu_cs_payment_record
                             WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D')
                             AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                             GROUP BY "MergePaymentNo") prr
                       WHERE  prr.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00') r
                       ON o."OrderNumber" = r."MergePaymentNo"
                       JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
                       WHERE p."PaymentStatus" = 1 AND o."OrderStatus"
                       NOT IN (6, 7) AND o."Channel" IN (0, 1, 2, 3, 5)
                       ) oo ON oo."Id" = i."OrderId"
       )base  WHERE base.categoryId != 1 GROUP BY base.categoryName ,base.categoryId ORDER BY revenue DESC
)main;

--  统计时间段内所有已支付品类金额，不包含已取消/已关闭状态。 对应 ProductCategoryRankRepository 商品品类销售额
SELECT tcom.tname,COALESCE(tcom.tamount, 0)FROM
(SELECT c."Name" AS tname,sum(i."AfterFoldingPrice") AS tamount FROM
    (SELECT o.* FROM songshu_cs_order o JOIN
        (SELECT DISTINCT "MergePaymentNo" FROM
            (SELECT pr."MergePaymentNo",MAX(pr."PaidTime") AS paidTime FROM
                (SELECT * FROM songshu_cs_payment_record WHERE "PaymentModeType" = 2 AND "PaidTime"
                 BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D')
                 AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                 GROUP BY "MergePaymentNo") prr
                 WHERE prr.paidTime
                 BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00') r ON o."OrderNumber" = r."MergePaymentNo"
            JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
        WHERE p."PaymentStatus" = 1 AND o."OrderStatus" NOT IN (6, 7) AND o."Channel" IN (0, 1, 2, 3, 5)) oo
    LEFT JOIN songshu_cs_order_item i ON oo."Id" = i."OrderId"
    LEFT JOIN songshu_cs_product p ON i."ProductId" = p."Id"
    INNER JOIN songshu_cs_category c ON p."CategoryId" = c."Id"
WHERE c."Id" != 1 GROUP BY c."Name" ORDER BY tamount DESC) tcom;

-- 商品销售详情 包括：销售额、成本、订单量、毛利率 对应 ProductRevenueRepository 商品的品类、名称、销售额、成本、订单量、毛利率
-- 销售额：统计时间段内所有已支付金额，不包含已取消/已关闭状态
-- 成本：统计时间段内所有已支付订单商品成本，不包含已取消/已关闭状态。
-- 统计时间段内包含该商品所有已支付订单，不包含已取消/已关闭状态。
-- 统计时间段内，（销售额-商品成本)÷销售额

SELECT c."Name" AS categoryName,p."Name" AS productName,main.revenue,main.cost,main.salesCount,
    main.grossMaringRate,p."Id" AS productId,p."Code" AS productCode FROM
    (SELECT calbase."ProductId",calbase.revenue,calbase.cost,calbase.salesCount,
         COALESCE((calbase.revenue - calbase.cost) / calbase.revenue, 0) AS grossMaringRate FROM
         (SELECT base."ProductId",COALESCE(SUM(base."AfterFoldingPrice"), 0) AS revenue,count(DISTINCT base.orderId) AS salesCount,
              COALESCE(SUM(base.cost * base."Quantity"), 0) AS cost FROM
             (SELECT g."ProductId",g."Id" AS goodsId,i."Quantity", CASE WHEN COALESCE(i."ReferCost", 0) = 0 THEN g."CostPrice" ELSE i."ReferCost" END AS cost,
                    o."Id" AS orderId,i."AfterFoldingPrice" FROM songshu_cs_order_item i
                    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
                    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
                    INNER JOIN (SELECT DISTINCT t."MergePaymentNo" FROM
                    (SELECT pr."MergePaymentNo",MAX(pr."PaidTime") AS paidTime FROM
                        (SELECT * FROM songshu_cs_payment_record WHERE "PaymentModeType" = 2 AND "PaidTime"
                         BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D')
                         AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr GROUP BY "MergePaymentNo") t
                         WHERE t.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00') cpr
                         ON cpr."MergePaymentNo" = op."MergePaymentId"
                    INNER JOIN songshu_cs_goods g ON g."Id" = i."GoodsId"
                WHERE op."PaymentStatus" = 1 AND o."orderType" IN (0, 1) AND o."OrderStatus" NOT IN (6, 7)
               ) base
          GROUP BY base."ProductId" ORDER BY revenue DESC LIMIT 10
         ) calbase
    ) main
    INNER JOIN songshu_cs_product p ON p."Id" = main."ProductId"
    INNER JOIN songshu_cs_category c ON c."Id" = p."CategoryId"
ORDER BY main.revenue DESC
LIMIT 10;

-- 统计时间段内，商品加入购物车的次数 对应 ProductRevenueRepository 加入购物车次数
SELECT COUNT(1) AS totalCount FROM songshu_shence_events e WHERE e.event ='AddCardEvent' AND e.productcode = ?3
AND e.times BETWEEN ?1 AND ?2 ;

-- 统计时间段内，商品收藏次数 对应 ProductRevenueRepository 收藏数
SELECT COUNT(1) AS totalCount FROM songshu_shence_events WHERE event ='CollectProductEvent' AND productcode = ?3
AND times BETWEEN ?1 AND ?2 ;

-- 付费率:统计时间段内，消费用户数占比（去重） 消费用户数÷访客数*100%
-- 商品消费用户数:时间段内，购买（支付成功，不包含已取消/已关闭状态）对应商品的用户数（去重） 对应 ProductRevenueRepository 商品消费用户数
SELECT count(DISTINCT (o."MemberId")) AS memberCount
FROM songshu_cs_order_item i
    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
    INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime
                FROM (SELECT * FROM songshu_cs_payment_record WHERE "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D')
                AND  (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                GROUP BY "MergePaymentNo") prr ON prr."MergePaymentNo" = op."MergePaymentId"
WHERE op."PaymentStatus" = 1 AND o."orderType" IN (0, 1) AND o."OrderStatus" NOT IN (6, 7) AND i."ProductId" = 100100590
AND prr.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00';

-- 退出率：统计时间段内，从该页退出的访客数÷进入该页的访客数的百分比
-- 退出商品页面访客数： 指进入到该商品页面后退出应用的用户数 对应 ProductRevenueRepository 退出商品页面访客数
--TODO 需要前端埋点后添加

-- 统计时间段内，所有访问过该商品页面的的人数（去重）ProductRevenueRepository 商品页面访客数
SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e
WHERE e.event = '$pageview'
AND (e.url like '#/tabs/categories/productInfo?productId=100100436%'
    OR e.url like '#/tabs/cart/productInfo?productId=100100436%'
    OR e.url like '#/tabs/index/productInfo?productId=100100436%'
    OR e.url like '#/tabs/user/productInfo?productId=100100436%'
    OR e.url like '#/tabs/integral/integralInfo?id=100100436&type=0%')
AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00';

-- 统计时间段内所有已支付订单（不包含已取消/已关闭状态），销售数量最多的商品 对应 ProductLinkedSalesRepository 商品销售量TOP
SELECT p."Id" AS productId, p."Name" AS productName, base.salesCount AS salesCount FROM
    (SELECT i."ProductId"  AS productId, SUM(i."Quantity") AS salesCount
     FROM songshu_cs_order_item i
         INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
         INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
         INNER JOIN (SELECT DISTINCT t."MergePaymentNo" FROM
             (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime
                           FROM (SELECT * FROM songshu_cs_payment_record
                                 WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D')
                                 AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                           GROUP BY "MergePaymentNo") t
                     WHERE  t.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00') cpr
             ON cpr."MergePaymentNo" = op."MergePaymentId"
     WHERE op."PaymentStatus" = 1 AND o."orderType" IN (0, 1) AND o."OrderStatus" NOT IN (6, 7)
     GROUP BY i."ProductId"
     ORDER BY salesCount DESC
     LIMIT 3) base
    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
ORDER BY base.salesCount DESC
LIMIT 3;

-- 统计时间段内所有已支付订单（不包含已取消/已关闭状态），销售数量最多的商品搭配 对应 ProductLinkedSalesRepository 商品销售量搭配TOP
SELECT p."Id" AS productId, p."Name" AS productName, base.salesCount AS salesCount FROM
    (SELECT i."ProductId" AS productId, SUM(i."Quantity") AS salesCount FROM songshu_cs_order_item i
         INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
         INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
         INNER JOIN (SELECT DISTINCT t."MergePaymentNo" FROM
            (SELECT pr."MergePaymentNo",  MAX(pr."PaidTime") AS paidTime FROM
                (SELECT * FROM songshu_cs_payment_record WHERE "PaymentModeType" = 2 AND "PaidTime"
                 BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D')
                 AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr GROUP BY "MergePaymentNo") t
                 WHERE t.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00') cpr
                 ON cpr."MergePaymentNo" = op."MergePaymentId"
                 WHERE op."PaymentStatus" = 1 AND o."orderType" IN (0, 1) AND o."OrderStatus" NOT IN (6, 7)
                 AND o."Id" IN (SELECT DISTINCT (scoi."OrderId") FROM songshu_cs_order_item scoi
                              INNER JOIN songshu_cs_order co ON co."Id" = scoi."OrderId"
                              INNER JOIN songshu_cs_order_payable cop ON cop."OrderId" = co."Id"
                              INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM
             (SELECT * FROM songshu_cs_payment_record WHERE "PaymentModeType" = 2 AND "PaidTime"
              BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D')
              AND ( CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
              GROUP BY "MergePaymentNo") cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
              WHERE cop."PaymentStatus" = 1 AND co."orderType" IN (0, 1) AND co."OrderStatus" NOT IN (6, 7) AND cpr.paidTime
              BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND scoi."ProductId" = 100100436)
              AND i."ProductId" != 100100436
     GROUP BY i."ProductId"
     ORDER BY salesCount DESC
     LIMIT 3) base
    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
ORDER BY base.salesCount DESC
LIMIT 3;

-- 获取商品图片地址 对应 ProductLinkedSalesRepository 商品图片地址
SELECT  CONCAT('/',pic."Location",'/',pic."Digest",pic."Extension") AS picUrl FROM  songshu_cs_product_picture pp
INNER JOIN songshu_cs_picture pic ON pic."Id" = pp."pictureId" WHERE pp."productId" = ?1 ORDER BY pic."Id" ASC  LIMIT 1;
