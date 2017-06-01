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
                FROM (SELECT generate_series('2016-06-01 01:00:00', '2016-08-01 01:00:00', 86400 * INTERVAL '1 second')) ts
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
     WHERE p."PaymentStatus" = 1  AND o."Channel" =1
     GROUP BY o."MemberId"
     HAVING min(o."OrderCreateTime") BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00') temp;

-- 之前有过下单行为的用户，在统计时间段内再次下单的用户数（去重）需要支付成功 对应 NotFirstOrderedConsumerCountRepository 非首单用户数
SELECT count(DISTINCT o."MemberId") as tc
FROM songshu_cs_order o LEFT JOIN songshu_cs_order_payable p on o."Id" = p."OrderId"
WHERE  p."PaymentStatus" = 1 AND o."Channel" = 1   AND o."OrderCreateTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'
AND o."MemberId" IN (
                    SELECT o."MemberId" AS id FROM songshu_cs_order o
                    LEFT JOIN songshu_cs_order_payable p on o."Id" = p."OrderId"
                    WHERE p."PaymentStatus" = 1 AND o."Channel" =1
                    GROUP BY o."MemberId"
                    HAVING min(o."OrderCreateTime") < '2016-06-01 00:00:00');

-- 时间段内 毛利率 =（销售额-商品成本)÷销售额*100% 对应 GrossMarginRateRepository 毛利率
SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END),0) AS goodsGrossMargin FROM
(SELECT SUM(base."Quantity" * base.cost)AS referCost, SUM(base."AfterFoldingPrice")AS AfterFoldingPrice  FROM
    (SELECT coi."Quantity",CASE WHEN COALESCE(coi."ReferCost",0) =0 THEN g."CostPrice" ELSE coi."ReferCost" END AS cost,coi."AfterFoldingPrice"
     FROM  songshu_cs_order o
     INNER JOIN songshu_cs_order_payable cop ON o."Id" = cop."OrderId"
     INNER JOIN (SELECT pr."MergePaymentNo",pr."PaymentModeType", MAX(pr."PaidTime") AS paidTime FROM (select * from songshu_cs_payment_record
                WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
     INNER JOIN songshu_cs_order_item coi ON o."Id" = coi."OrderId"
     INNER JOIN songshu_cs_goods g ON  g."Id" = coi."GoodsId"
     WHERE cop."PaymentStatus" = 1 AND o."OrderStatus" NOT IN (6,7) AND o."orderType" IN(0, 1)
     AND cpr.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-07-01 00:00:00' AND o."Channel" = 1
    )base
)grossmargin;

-- 时间段内 毛利率 =（销售额-商品成本)÷销售额*100% 对应 GrossMarginRateRepository 毛利率趋势
SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END)),0) AS goodsGrossMargin FROM
( SELECT SUM(base."Quantity" * base.cost)AS referCost, SUM(base."AfterFoldingPrice")AS AfterFoldingPrice, base.stime, base.etime FROM
    (SELECT  coi."Quantity",coi."AfterFoldingPrice",CASE WHEN coi."ReferCost" = 0 THEN g."CostPrice" ELSE coi."ReferCost" END  AS cost,tss.stime,tss.etime
     FROM (SELECT DISTINCT oo."Id", cpr.paidTime
                FROM songshu_cs_order oo
                INNER JOIN songshu_cs_order_payable cop ON oo."Id" = cop."OrderId"
                INNER JOIN (SELECT pr."MergePaymentNo",pr."PaymentModeType", MAX(pr."PaidTime") AS paidTime FROM (select * from songshu_cs_payment_record
                            WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                            GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
                WHERE cop."PaymentStatus" = 1 AND oo."OrderStatus" NOT IN (6,7) AND oo."orderType" IN(0, 1)
                AND cpr.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-07-01 00:00:00' AND oo."Channel" = 1
            ) coo
     INNER JOIN songshu_cs_order_item coi ON coo."Id" = coi."OrderId"
     INNER JOIN songshu_cs_goods g ON g."Id" = coi."GoodsId"
     RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + 86400 * INTERVAL '1 second' AS etime
                FROM (SELECT generate_series('2016-06-01 00:00:00', '2016-08-01 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
                ON (coo.paidTime < tss.etime AND coo.paidTime >= tss.stime)
    )base GROUP BY base.stime, base.etime ORDER BY base.stime
)grossmargin;


-- 时间段内单渠道注册会员数(首页注册用户饼图使用) 对应 NewRegisterCountRepository 新注册用户数
SELECT COUNT(id) AS tc FROM songshu_cs_member
WHERE "regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'  AND "multipleChannelsId" = 1;
SELECT COUNT(id) AS tc FROM songshu_cs_member
WHERE "regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'  AND "multipleChannelsId" NOT IN (1,2,3,5);


-- 时间段内支付订单量统计 对应 OrderCountRepository 订单量
SELECT COUNT(DISTINCT o."Id") FROM songshu_cs_order o
INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
INNER JOIN (SELECT pr."MergePaymentNo",pr."PaymentModeType", MAX(pr."PaidTime") AS paidTime FROM (select * from songshu_cs_payment_record
            WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
            GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = p."MergePaymentId"
WHERE o."OrderStatus" NOT IN (6, 7) AND p."PaymentStatus" = 1 AND o."Channel" = 1
AND cpr.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00';


-- 时间段内支付订单量趋势图统计 对应 OrderCountRepository 支付订单量趋势图统计
SELECT tss.stime AS stime, tss.etime AS etime, COUNT(DISTINCT comt.Id) AS result
FROM(SELECT cpr.paidTime,o."Id" AS Id FROM songshu_cs_order o
     INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
     INNER JOIN (SELECT pr."MergePaymentNo",pr."PaymentModeType", MAX(pr."PaidTime") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record
                 WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                 GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = p."MergePaymentId"
     WHERE o."OrderStatus" NOT IN (6, 7) AND p."PaymentStatus" = 1 AND o."Channel" = 1
     AND cpr.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'
    )comt
RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + 86400 * INTERVAL '1 second' AS etime
            FROM (SELECT generate_series('2016-06-01 00:00:00', '2016-08-01 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
            ON (comt.paidTime < tss.etime AND comt.paidTime >= tss.stime)
GROUP BY tss.stime, tss.etime ORDER BY tss.stime;


-- 时间段内退款金额统计 对应 RefundRepository 退款金额
SELECT COALESCE(SUM(item."ActualRefundMoney"),0) FROM songshu_cs_order oo
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = oo."Id"
INNER JOIN songshu_cs_payment_record r ON op."MergePaymentId" = r."MergePaymentNo"
INNER JOIN songshu_cs_refund_item item ON item."PaymentRecordId" = r."Id"
WHERE r."PaymentModeType" = 2 AND item. "Status" = 5 AND item."MoneyType" = 1
AND item."RefundType" = 1 AND oo."orderType" IN(0, 1) AND oo."Channel" = 0
AND item."LastModifyTime" BETWEEN '2016-01-01 00:00:00' AND '2017-08-01 00:00:00';

-- 时间段内退款金额趋势图统计 对应 RefundRepository 退款金额趋势图
SELECT tss.stime, tss.etime, COALESCE(SUM(comt.ActualRefundMoney),0)
FROM(SELECT item."ActualRefundMoney" AS ActualRefundMoney, item."LastModifyTime" AS LastModifyTime
     FROM songshu_cs_order oo INNER JOIN songshu_cs_order_payable op ON op."OrderId" = oo."Id"
     INNER JOIN songshu_cs_payment_record r ON op."MergePaymentId" = r."MergePaymentNo"
     INNER JOIN songshu_cs_refund_item item ON item."PaymentRecordId" = r."Id"
     WHERE r."PaymentModeType" = 2 AND item."Status" = 5 AND item."MoneyType" = 1
     AND item."RefundType" = 1 AND oo."orderType" IN (0, 1)  AND oo."Channel" =0
     AND item."LastModifyTime" BETWEEN '2016-01-01 00:00:00' AND '2017-08-01 00:00:00'
    ) comt
RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + 86400 * INTERVAL '1 second' AS etime
        FROM (SELECT generate_series('2016-01-01 00:00:00', '2017-08-01 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
        ON (comt.LastModifyTime < tss.etime AND comt.LastModifyTime >= tss.stime)
GROUP BY tss.stime, tss.etime ORDER BY tss.stime;

-- 时间段内销售额统计 对应 RevenueRepository 销售额
SELECT COALESCE(SUM(cpr.amount),0) FROM songshu_cs_order o
INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime,SUM("Amount") AS amount FROM (SELECT * FROM songshu_cs_payment_record
            WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
            GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = p."MergePaymentId"
WHERE o."OrderStatus" NOT IN (6, 7) AND p."PaymentStatus" = 1 AND o."Channel" = 0
AND cpr.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00';

-- 时间段内销售额趋势图统计 对应 RevenueRepository 销售额趋势图
SELECT tss.stime AS stime, tss.etime AS etime, COALESCE(SUM(comt.Amount),0)
FROM(SELECT cpr.amount, cpr.paidTime
     FROM songshu_cs_order o
     INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
     INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime,SUM("Amount") AS amount FROM (SELECT * FROM songshu_cs_payment_record
                WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = p."MergePaymentId"
     WHERE o."OrderStatus" NOT IN (6, 7) AND p."PaymentStatus" = 1 AND o."Channel" = 1
     AND cpr.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'
    ) comt
RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + 86400 * INTERVAL '1 second' AS etime
        FROM (SELECT generate_series('2016-06-01 00:00:00','2016-08-01 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
        ON (comt.PaidTime < tss.etime AND comt.PaidTime >= tss.stime)
GROUP BY tss.stime, tss.etime ORDER BY tss.stime;

-- 访客数现在有2种计算方式
-- 1。从神策pageview事件种获取
-- 时间段内访客数趋势图统计 对应 UniqueVisitorsRepository 访客数趋势图
SELECT COUNT(DISTINCT se.distinct_id) FROM songshu_shence_events se
WHERE se.event = '$pageview' AND se.times BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND se.platform ='ios';

-- 时间段内访客数趋势图统计 对应 UniqueVisitorsRepository 访客数趋势图
SELECT tss.stime, tss.etime, COUNT(DISTINCT comt.distinct_id)
FROM(SELECT se.times, se.distinct_id FROM songshu_shence_events se
    WHERE se.event = '$pageview' AND se.times BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND se.platform = 'ios'
    ) comt
RIGHT JOIN(SELECT ts.generate_series AS stime, ts.generate_series + 86400 * INTERVAL '1 second' AS etime
          FROM (SELECT generate_series('2016-06-01 00:00:00','2016-08-01 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
          ON (comt.times < tss.etime AND comt.times >= tss.stime)
GROUP BY tss.stime, tss.etime ORDER BY tss.stime;

-- 2。从nginx日志中获取
-- 时间段内访客数趋势图统计 对应 UniqueVisitorsRepository 访客数趋势图
SELECT COUNT(DISTINCT CASE WHEN userid <> '' AND userid <> '-' THEN userid END )
+ COUNT(DISTINCT CASE WHEN userid = '' OR userid = '-' THEN ss."unique" END)
FROM songshu_log ss
WHERE os = 'ios' AND logTime BETWEEN '2016-01-01 00:00:00' AND '2017-08-01 00:00:00';

-- 时间段内访客数趋势图统计 对应 UniqueVisitorsRepository 访客数趋势图
SELECT tss.stime, tss.etime, COUNT(DISTINCT CASE WHEN comt.userid <> '' AND comt.userid <> '-' THEN comt.userid END)
+ COUNT(DISTINCT CASE WHEN comt.userid = '' OR comt.userid = '-' THEN comt.unique END) AS uv
FROM (select ss.logtime,ss."unique",ss.userid
      FROM songshu_log ss
      WHERE ss.os = 'ios'  AND  ss.logTime BETWEEN '2016-01-01 00:00:00' AND '2017-08-01 00:00:00'
     ) comt
RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + 86400 * INTERVAL '1 second' AS etime
            FROM (SELECT generate_series('2016-01-01 00:00:00', '2017-08-01 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
            ON (comt.logTime < tss.etime AND comt.logTime >= tss.stime)
GROUP BY tss.stime, tss.etime ORDER BY tss.stime;
