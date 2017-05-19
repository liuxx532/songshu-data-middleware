(select "MergePaymentNo",MAX("PaidTime"),SUM("Amount") AS amount from songshu_cs_payment_record WHERE "PaymentModeType" =2
AND "PaidTime" BETWEEN ('2016-06-01 00:00:00'::TIMESTAMP - INTERVAL '1 day') AND  ('2016-08-01 00:00:00'::TIMESTAMP + INTERVAL '1 day')
GROUP BY "MergePaymentNo","PaymentModeType");

-- 指定时间内平均客单价计算  对应 AvgOrderRevenueRepository  平均客单价
SELECT COALESCE(SUM(r.amount) / COUNT(DISTINCT o."Id"),0) as result
FROM songshu_cs_order o
INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
INNER JOIN (SELECT "MergePaymentNo",MAX("PaidTime") AS paidTime,SUM("Amount") AS amount FROM songshu_cs_payment_record WHERE "PaymentModeType" =2
            AND  AGE("PaidTime") < (AGE(TIMESTAMP '2016-06-01 00:00:00') + INTERVAL '1 D') AND  AGE("PaidTime") >  (AGE(TIMESTAMP '2016-08-01 00:00:00') - INTERVAL '1 D')
            GROUP BY "MergePaymentNo","PaymentModeType") r ON p."MergePaymentId" = r."MergePaymentNo"
WHERE o."OrderStatus" NOT IN (6,7) AND p."PaymentStatus" = 1
AND (r.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00') AND o."Channel" = 1;


-- 指定时间内总平均客单价趋势图计算  对应 AvgOrderRevenueRepository  平均客单价趋势图
SELECT tss.stime AS stime, tss.etime AS etime, COALESCE((SUM(comt.Amount) / COUNT(DISTINCT comt.Id)),0) AS result
FROM(SELECT r.amount AS Amount ,r.paidTime AS PaidTime,o."Id" AS Id
     FROM songshu_cs_order o
     INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
     INNER JOIN (select "MergePaymentNo",MAX("PaidTime") AS paidTime,SUM("Amount") AS amount from songshu_cs_payment_record WHERE "PaymentModeType" =2
                 AND "PaidTime" BETWEEN ('2016-06-01 00:00:00'::TIMESTAMP - INTERVAL '1 day') AND  ('2016-08-01 00:00:00'::TIMESTAMP + INTERVAL '1 day')
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
INNER JOIN (select "MergePaymentNo",MAX("PaidTime") AS paidTime from songshu_cs_payment_record WHERE "PaymentModeType" =2
            AND "PaidTime" BETWEEN ('2016-06-01 00:00:00'::TIMESTAMP - INTERVAL '1 day') AND  ('2017-08-01 00:00:00'::TIMESTAMP + INTERVAL '1 day')
            GROUP BY "MergePaymentNo","PaymentModeType") r ON op."MergePaymentId" = r."MergePaymentNo"
WHERE c."Id" != 1 AND op."PaymentStatus" = 1 AND o."OrderStatus" NOT IN (6,7) AND o."Channel" = 1 AND r.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'
GROUP BY c."Name" ORDER BY tmount DESC;
