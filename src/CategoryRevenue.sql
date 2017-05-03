
-- 商品销售报表 全渠道
SELECT c."Name" as categoryName, p."Name" AS  productName, base.* FROM
(SELECT i."ProductId" AS productId, sum(i."AfterFoldingPrice") AS revenue,count(DISTINCT o."Id") AS  salesCount ,sum(i."ReferCost" * i."Quantity") AS cost,
    (sum(i."AfterFoldingPrice")-sum(i."ReferCost" * i."Quantity"))/sum(i."AfterFoldingPrice") as grossMaringRate FROM songshu_cs_order_item i
    INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
    INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
    INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
                WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
    GROUP BY i."ProductId" ) base
INNER JOIN songshu_cs_product p ON p."Id" = base.productId
INNER JOIN songshu_cs_category c ON c."Id" = p."CategoryId"
ORDER BY base.revenue DESC LIMIT 20;


-- 商品销售报表 单渠道
SELECT c."Name" as categoryName, p."Name" AS  productName, base.* FROM
    (SELECT i."ProductId" AS productId, sum(i."AfterFoldingPrice") AS revenue,count(DISTINCT o."Id") AS  salesCount ,sum(i."ReferCost" * i."Quantity") AS cost,
            (sum(i."AfterFoldingPrice")-sum(i."ReferCost" * i."Quantity"))/sum(i."AfterFoldingPrice") as grossMaringRate FROM songshu_cs_order_item i
        INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
        INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
        INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
     WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND o."Channel" = 4
     GROUP BY i."ProductId") base
    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
    INNER JOIN songshu_cs_category c ON c."Id" = p."CategoryId";



SELECT count(m.id) as tc ,1
FROM songshu_cs_member m WHERE m."regTime" BETWEEN '2015-01-06 18:22:50' AND '2017-01-06 18:22:50' AND m."multipleChannelsId" = 1;
