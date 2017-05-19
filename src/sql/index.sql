SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime, SUM(pr."Amount") AS amount FROM (select * from songshu_cs_payment_record
WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
GROUP BY "MergePaymentNo", "PaymentModeType";

-- 指定时间内平均客单价计算  对应 AvgOrderRevenueRepository  平均客单价
SELECT COALESCE(SUM(r.amount) / COUNT(DISTINCT o."Id"), 0) AS result
FROM songshu_cs_order o
INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime, SUM(pr."Amount") AS amount FROM (select * from songshu_cs_payment_record
            WHERE "PaymentModeType" = 2 AND "PaidTime"
            BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
            GROUP BY "MergePaymentNo", "PaymentModeType") r ON p."MergePaymentId" = r."MergePaymentNo"
WHERE o."OrderStatus" NOT IN (6, 7) AND p."PaymentStatus" = 1
AND (r.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00') AND o."Channel" = 1;


-- 指定时间内总平均客单价趋势图计算  对应 AvgOrderRevenueRepository  平均客单价趋势图
SELECT tss.stime AS stime, tss.etime AS etime, COALESCE((SUM(comt.Amount) / COUNT(DISTINCT comt.Id)),0) AS result
FROM(SELECT r.amount AS Amount ,r.paidTime AS PaidTime,o."Id" AS Id
     FROM songshu_cs_order o
         INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
         INNER JOIN (select pr."MergePaymentNo",MAX(pr."PaidTime") AS paidTime,SUM(pr."Amount") AS amount from (select * from songshu_cs_payment_record WHERE "PaymentModeType" =2
                     AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 day') AND  (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 day')) pr
         GROUP BY "MergePaymentNo","PaymentModeType") r ON p."MergePaymentId" = r."MergePaymentNo"
     WHERE o."OrderStatus" NOT IN (6,7) AND p."PaymentStatus" = 1
     AND (r.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00') AND o."Channel" = 1
    )comt
    RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + 86400 * INTERVAL '1 second' AS etime
                FROM (SELECT generate_series('2016-06-01 00:00:00', '2016-08-01 00:00:00', 86400 * INTERVAL '1 second')) ts
               ) tss ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime)
GROUP BY tss.stime, tss.etime ORDER BY tss.stime;

-- 指定时间内销售额品类排行  对应 CategoryRevenueRankingRepository  销售额品类排行
SELECT c."Name",COALESCE(SUM(i."AfterFoldingPrice"),0) AS tmount
FROM  songshu_cs_category c
INNER JOIN songshu_cs_product p ON c."Id" = p."CategoryId"
INNER JOIN songshu_cs_order_item i ON i."ProductId" = p."Id"
INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
INNER JOIN songshu_cs_order_payable op ON o."Id" = op."OrderId"
INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM (select * from songshu_cs_payment_record
            WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
            GROUP BY "MergePaymentNo", "PaymentModeType") r ON op."MergePaymentId" = r."MergePaymentNo"
WHERE c."Id" != 1 AND op."PaymentStatus" = 1 AND o."OrderStatus" NOT IN (6,7) AND o."Channel" = 1 AND r.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'
GROUP BY c."Name" ORDER BY tmount DESC;

-- 之前从未有过下单行为，统计时间段内首次下单成功的用户数（去重）需要支付成功 对应 FirstOrderedConsumerCountRepository 首单用户数
SELECT count(DISTINCT temp.id) AS tc
FROM
    (SELECT o."MemberId" AS id, min(o."OrderCreateTime") AS mt
     FROM songshu_cs_order o JOIN songshu_cs_order_payable p on o."Id" = p."OrderId"
     WHERE p."PaymentStatus" = 1  AND o."Channel" IN (0, 1, 2, 3, 5)
     GROUP BY o."MemberId"
     HAVING min(o."OrderCreateTime") BETWEEN ?1 AND ?2) temp;

-- 之前有过下单行为的用户，在统计时间段内再次下单的用户数（去重）需要支付成功 对应 NotFirstOrderedConsumerCountRepository 非首单用户数
SELECT count(DISTINCT o."MemberId") as tc
FROM songshu_cs_order o LEFT JOIN songshu_cs_order_payable p on o."Id" = p."OrderId"
WHERE o."Channel" IN (0, 1, 2, 3, 5)
      AND p."PaymentStatus" = 1
      AND o."OrderCreateTime" BETWEEN ?1 AND ?2
      AND o."MemberId" in (
                            SELECT o."MemberId" AS id FROM songshu_cs_order o
                            LEFT JOIN songshu_cs_order_payable p on o."Id" = p."OrderId"
                            WHERE o."Channel" IN (0, 1, 2, 3, 5) AND p."PaymentStatus" = 1
                            GROUP BY o."MemberId"
                            HAVING min(o."OrderCreateTime") < ?1);
-- 时间段内 毛利率 =（销售额-商品成本)÷销售额*100% 对应 GrossMarginRateRepository 毛利率
SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ grossmargin.AfterFoldingPrice,0) AS goodsGrossMargin FROM
(SELECT SUM(base."Quantity" * base.cost)AS referCost, SUM(base."AfterFoldingPrice")AS AfterFoldingPrice  FROM
        (SELECT coi."Quantity",CASE WHEN COALESCE(coi."ReferCost",0) =0 THEN g."CostPrice" ELSE coi."ReferCost" END AS cost,coi."AfterFoldingPrice"
         FROM  songshu_cs_order co
         RIGHT JOIN ( SELECT DISTINCT oo."Id" FROM songshu_cs_order oo
                     INNER JOIN songshu_cs_order_payable cop ON oo."Id" = cop."OrderId"
                     INNER JOIN (SELECT pr."MergePaymentNo",pr."PaymentModeType", MAX(pr."PaidTime") AS paidTime FROM (select * from songshu_cs_payment_record
                                 WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                                 GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
                     WHERE cop."PaymentStatus" = 1 AND cpr."PaymentModeType" = 2 AND cpr.paidTime
                     BETWEEN '2016-06-01 00:00:00' AND '2016-07-01 00:00:00' AND oo."OrderStatus" NOT IN (6,7) AND oo."orderType" IN(0, 1)
                     AND oo."Channel" IN(0, 1, 2, 3, 5) ) coo ON co."Id" = coo."Id"
         INNER JOIN songshu_cs_order_item coi ON co."Id" = coi."OrderId"
         INNER JOIN songshu_cs_goods g ON  g."Id" = coi."GoodsId"
        )base
)grossmargin;

-- 时间段内 毛利率 =（销售额-商品成本)÷销售额*100% 对应 GrossMarginRateRepository 毛利率趋势
SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (grossmargin.AfterFoldingPrice)),0) AS goodsGrossMargin FROM
( SELECT SUM(base."Quantity" * base.cost)AS referCost, SUM(base."AfterFoldingPrice")AS AfterFoldingPrice, base.stime, base.etime FROM
    (SELECT  coi."Quantity",coi."AfterFoldingPrice",CASE WHEN coi."ReferCost" = 0 THEN g."CostPrice" ELSE coi."ReferCost" END  AS cost,tss.stime,tss.etime
     FROM songshu_cs_order co
     RIGHT JOIN (SELECT  oo."Id", MAX(cpr.paidTime) AS MPaidTime
                FROM songshu_cs_order oo
                INNER JOIN songshu_cs_order_payable cop ON oo."Id" = cop."OrderId"
                INNER JOIN (SELECT pr."MergePaymentNo",pr."PaymentModeType", MAX(pr."PaidTime") AS paidTime FROM (select * from songshu_cs_payment_record
                            WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                            GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
                WHERE cop."PaymentStatus" = 1 AND cpr."PaymentModeType" = 2 AND cpr.paidTime
                BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND oo."OrderStatus" NOT IN (6, 7) AND oo."orderType" IN (0, 1)
                AND  oo."Channel" IN (0, 1, 2, 3, 5)  GROUP BY oo."Id") coo ON co."Id" = coo."Id"
     INNER JOIN songshu_cs_order_item coi ON co."Id" = coi."OrderId"
     INNER JOIN songshu_cs_goods g ON g."Id" = coi."GoodsId"
     RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + 86400 * INTERVAL '1 second' AS etime
                FROM (SELECT generate_series('2016-06-01 00:00:00', '2016-08-01 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
                ON (coo.MPaidTime < tss.etime AND coo.MPaidTime >= tss.stime)
    )base GROUP BY base.stime, base.etime ORDER BY base.stime
)grossmargin;
