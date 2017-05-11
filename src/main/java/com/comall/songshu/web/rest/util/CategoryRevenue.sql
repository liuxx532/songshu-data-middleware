
-- 商品销售报表 全渠道
SELECT  p."Id" AS productId ,p."Code" ,c."Name" as categoryName, p."Name" AS  productName, main.revenue,main.cost,main.salesCount,main.grossMaringRate FROM(
SELECT base.productId,base.revenue,base.cost,base.salesCount, COALESCE((base.revenue-base.cost)/base.revenue,0) AS grossMaringRate FROM
(SELECT i."ProductId" AS productId, COALESCE(SUM(i."AfterFoldingPrice"),0) AS revenue,count(DISTINCT o."Id") AS  salesCount
,COALESCE(SUM(i."ReferCost" * i."Quantity"),0) AS cost
FROM songshu_cs_order_item i
INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-01-01 00:00:00' AND '2017-01-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
GROUP BY i."ProductId" ORDER BY revenue DESC LIMIT 20 ) base) main
INNER JOIN songshu_cs_product p ON p."Id" = main.productId
INNER JOIN songshu_cs_category c ON c."Id" = p."CategoryId"
ORDER BY main.revenue DESC LIMIT 20;


-- 商品销售报表 单渠道

SELECT  p."Id" AS productId ,g."Id" AS goodsId,p."Code" , c."Name" as categoryName, p."Name" AS  productName, main.revenue,main.cost,main.salesCount,main.grossMaringRate FROM(
SELECT base.productId,base.revenue,base.cost, base.salesCount,COALESCE((base.revenue-base.cost)/base.revenue,0) AS grossMaringRate FROM
 (SELECT i."ProductId" AS productId, COALESCE(SUM(i."AfterFoldingPrice"),0) AS revenue,count(DISTINCT o."Id") AS  salesCount
,COALESCE(SUM(i."ReferCost" * i."Quantity"),0) AS cost
FROM songshu_cs_order_item i
INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND o."Channel" = 1
GROUP BY i."ProductId" ORDER BY revenue DESC LIMIT 20 ) base) main
INNER JOIN songshu_cs_product p ON p."Id" = main.productId
INNER JOIN songshu_cs_goods g ON p."Id" = g."ProductId"
INNER JOIN songshu_cs_category c ON c."Id" = p."CategoryId"
ORDER BY main.revenue DESC LIMIT 20;




-- 商品关联图
-- 查出销量最多的TOP3的商品信息
SELECT  p."Id" as productId,p."Name" AS  productName, base.salesCount AS salesCount,CONCAT('/',pic."Location",'/',pic."Digest",pic."Extension") AS picUrl FROM
    (SELECT i."ProductId" AS productId,count(DISTINCT o."Id") AS  salesCount FROM songshu_cs_order_item i
        INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
        INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
        INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND o."Channel" = 1
    GROUP BY i."ProductId" ORDER BY salesCount DESC LIMIT 3) base
    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
    INNER JOIN songshu_cs_product_picture pp ON pp."productId" = p."Id"
    INNER JOIN songshu_cs_picture pic ON pic."Id" = pp."pictureId"
ORDER BY base.salesCount DESC LIMIT 3;

-- 无图片测试
SELECT  p."Id" as productId,p."Name" AS  productName, base.salesCount AS salesCount FROM
    (SELECT i."ProductId" AS productId,count(DISTINCT o."Id") AS  salesCount FROM songshu_cs_order_item i
        INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
        INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
        INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND o."Channel" = 1
    GROUP BY i."ProductId" ORDER BY salesCount DESC LIMIT 3) base
    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
ORDER BY base.salesCount DESC LIMIT 3;




-- 查出与某商品相关联的销量最多的TOP3的商品信息
SELECT  p."Id" as productId,p."Name" AS  productName, base.salesCount AS salesCount,CONCAT('/',pic."Location",'/',pic."Digest",pic."Extension") AS picUrl FROM
    (SELECT i."ProductId" AS productId,count(DISTINCT o."Id") AS  salesCount FROM songshu_cs_order_item i
        INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
        INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
        INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND o."Channel" = 1
           AND o."Id" IN (SELECT DISTINCT (scoi."OrderId") FROM songshu_cs_order_item scoi
                          INNER JOIN songshu_cs_order co ON co."Id" = scoi."OrderId"
                          INNER JOIN songshu_cs_order_payable cop ON cop."OrderId" = co."Id"
                          INNER JOIN songshu_cs_payment_record cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
                          WHERE cop."PaymentStatus" =1 AND co."orderType" IN (0,1) AND co."OrderStatus" NOT IN (6,7) AND cpr."PaidTime"
                          BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND scoi."ProductId" = 100100316 AND co."Channel" = 1
                        )
           AND i."ProductId" != 100100316
    GROUP BY i."ProductId" ORDER BY salesCount DESC LIMIT 3) base
    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
    INNER JOIN songshu_cs_product_picture pp ON pp."productId" = p."Id"
    INNER JOIN songshu_cs_picture pic ON pic."Id" = pp."pictureId"
ORDER BY base.salesCount DESC LIMIT 3;

-- 无图片测试
SELECT  p."Id" as productId,p."Name" AS  productName, base.salesCount AS salesCount FROM
    (SELECT i."ProductId" AS productId,count(DISTINCT o."Id") AS  salesCount FROM songshu_cs_order_item i
        INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
        INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
        INNER JOIN ( SELECT DISTINCT t."MergePaymentNo" FROM songshu_cs_payment_record t
        WHERE "PaymentModeType"= 2 AND "PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00')cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
    WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND o."Channel" = 1
           AND o."Id" IN (SELECT DISTINCT (scoi."OrderId") FROM songshu_cs_order_item scoi
        INNER JOIN songshu_cs_order co ON co."Id" = scoi."OrderId"
        INNER JOIN songshu_cs_order_payable cop ON cop."OrderId" = co."Id"
        INNER JOIN songshu_cs_payment_record cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
    WHERE cop."PaymentStatus" =1 AND co."orderType" IN (0,1) AND co."OrderStatus" NOT IN (6,7) and cpr."PaidTime"
    BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND scoi."ProductId" = 100100316
    )
           AND i."ProductId" != 100100316
    GROUP BY i."ProductId" ORDER BY salesCount DESC LIMIT 3) base
    INNER JOIN songshu_cs_product p ON p."Id" = base.productId
ORDER BY base.salesCount DESC LIMIT 3;

-- 注册用户数
SELECT count(DISTINCT(m.id)) AS memberCount FROM songshu_cs_member m
WHERE m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND m."multipleChannelsId" = 1;

-- 消费用户数

SELECT count(DISTINCT o."MemberId") AS  memberCount FROM  songshu_cs_order o
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
AND pr."PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND o."Channel" = 1;

-- 注册消费转化率
SELECT COALESCE(sum(main.pay)/sum(main.register),0) as consumeTransferRate FROM
(SELECT
MAX(CASE type WHEN 'pay' THEN memberCount ELSE 0 END ) pay,
MAX(CASE type WHEN 'register' THEN memberCount ELSE 0 END ) register
FROM
(SELECT count(DISTINCT o."MemberId") AS  memberCount,'pay' AS type ,'1' as date FROM  songshu_cs_order o
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
INNER JOIN songshu_cs_member m ON m.id = o."MemberId"
WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
AND m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND m."multipleChannelsId" = 1
AND pr."PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'  AND o."Channel" = 1
UNION ALL
SELECT count(DISTINCT(m.id)) AS memberCount , 'register' AS type ,'2' as date FROM songshu_cs_member m
WHERE m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND m."multipleChannelsId" = 1) base
GROUP BY date) main;



-- 复购率
SELECT count(DISTINCT(n."MemberId")) AS rebuyMemberCount from
(SELECT DISTINCT(o."MemberId") FROM  songshu_cs_order o
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
AND pr."PaidTime" BETWEEN '2016-07-01 00:00:00' AND '2016-08-01 00:00:00' AND o."Channel" = 1) n
WHERE  n."MemberId" IN
(SELECT DISTINCT (o."MemberId") FROM  songshu_cs_order o
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
AND pr."PaidTime" < '2016-07-01 00:00:00' AND o."Channel" = 1);




-- 商品消费用户数

SELECT count(DISTINCT(o."MemberId")) AS  memberCount   FROM songshu_cs_order_item i
INNER JOIN songshu_cs_order o ON o."Id" = i."OrderId"
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
INNER JOIN songshu_cs_payment_record pr ON pr."MergePaymentNo" = op."MergePaymentId"
WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7) AND i."ProductId" = 100100452
AND pr."PaidTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00' AND o."Channel" = 1;



-- 性别比例图
SELECT CASE WHEN sexInfo.gender >0  THEN '男' ELSE '女' END AS sexGroup, sexInfo.memberCount FROM
(SELECT info."gender" as gender, count(DISTINCT(m.id)) as memberCount FROM  songshu_cs_member m
INNER JOIN songshu_cs_member_info info ON info."memberId" = m.id
WHERE m."regTime" BETWEEN '2016-07-01 00:00:00' AND '2016-08-01 00:00:00' AND m."multipleChannelsId" = 1
group BY info."gender" ORDER BY info."gender" desc) sexInfo;

-- 年龄分布图
SELECT ageGroupInfo.ageGroup AS ageGroup , SUM(ageGroupInfo.memberCount) AS memberCount FROM
(SELECT
     CASE
     WHEN birthInfo.age<=20 OR birthInfo.age IS NULL  THEN '0-20岁'
     WHEN birthInfo.age>=21 AND birthInfo.age<=25 THEN '21-25岁'
     WHEN birthInfo.age>=26 AND birthInfo.age<=30 THEN '26-30岁'
     WHEN birthInfo.age>=31 AND birthInfo.age<=35 THEN '31-35岁'
     WHEN birthInfo.age>=36 THEN '36岁+'
     END AS ageGroup,
     birthInfo.memberCount AS memberCount FROM
     (SELECT (EXTRACT(YEAR FROM NOW())-EXTRACT(YEAR FROM info."birthday")-1) +
         CASE
            WHEN ( DATE_PART('month',info."birthday") < DATE_PART('month',NOW())) THEN 0
            WHEN ( DATE_PART('month',info."birthday") = DATE_PART('month',NOW())) AND (DATE_PART('day',info."birthday") < DATE_PART('day',NOW())) THEN 0
            ELSE 1
         END AS age
      ,count(DISTINCT(m.id)) as memberCount
      FROM  songshu_cs_member m
          INNER JOIN songshu_cs_member_info info ON info."memberId" = m.id
      WHERE m."regTime" BETWEEN '2016-07-01 00:00:00' AND '2016-08-01 00:00:00'
            AND m."multipleChannelsId" = 1
      GROUP BY age ORDER BY age ASC
     ) birthInfo
)ageGroupInfo
GROUP BY ageGroupInfo.ageGroup ORDER BY ageGroupInfo.ageGroup ASC;


-- 加购次数
SELECT COUNT(1) AS totalCount FROM songshu_shence_events e WHERE e.event ='AddCardEvent' AND e.productcode ='6956511900046'
AND e.times BETWEEN '2016-11-01 00:00:00' AND '2016-12-01 00:00:00' AND e.platform ='ios';


-- 收藏数
SELECT COUNT(1) AS totalCount FROM songshu_shence_events e WHERE e.event ='CollectProductEvent' AND e.productcode ='6956511900046'
AND e.times BETWEEN '2016-11-01 00:00:00' AND '2016-12-01 00:00:00' AND e.platform ='ios';

-- 商品访客数
SELECT COUNT(DISTINCT e.distinct_id) AS totaldiscount FROM songshu_shence_events e WHERE e.event ='$pageview' AND e.url like '%/productInfo%'
AND (e.url like '%productId=100100436%' OR  e.url like '%goodsId=100100436%')
AND e.times BETWEEN '2016-11-01 00:00:00' AND '2016-12-01 00:00:00' AND e.platform ='ios';

-- 退出访客数
SELECT COUNT(DISTINCT e.distinct_id) AS totaldiscount FROM songshu_shence_events e WHERE e.event ='$pageview' AND e.referrer like '%/productInfo%'
AND (e.referrer like '%productId=100100436%' OR  e.referrer like '%goodsId=100100436%')
AND e.times BETWEEN '2016-11-01 00:00:00' AND '2016-12-01 00:00:00' AND e.platform ='ios'


SELECT COUNT(DISTINCT e.distinct_id)  FROM songshu_shence_events e
WHERE e.event = '$pageview'
AND (e.referrer like '#/tabs/categories/productInfo?productId=100100436%'
OR e.referrer like '#/tabs/cart/productInfo?productId=100100436%'
OR e.referrer like '#/tabs/index/productInfo?productId=100100436%'
OR e.referrer like '#/tabs/user/productInfo?productId=100100436%'
OR e.referrer like '#/tabs/integral/productInfo?productId=100100436%')
AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00';


-- 渠道注册用户数据
SELECT u.utm_source,COUNT(u.second_id) as userCount FROM songshu_shence_users u
INNER JOIN songshu_shence_events e ON e.distinct_id = u.second_id
WHERE  e.times BETWEEN '2017-01-01 00:00:00' AND '2017-12-01 00:00:00'
AND u.second_id is not null AND u.utm_source is NOT NULL
AND u.utm_source NOT IN('test','preproduction','production','newtest','channel_10')
AND e.platform ='ios'  GROUP BY u.utm_source  ORDER BY userCount DESC LIMIT 10;


SELECT u.utm_source,COUNT(u.second_id) as userCount FROM songshu_shence_users u
INNER JOIN songshu_cs_member mem ON u.second_id = mem."id"
WHERE  mem."regTime" BETWEEN '2017-01-01 00:00:00' AND '2017-12-01 00:00:00'
AND u.second_id is not null AND u.utm_source is NOT NULL
AND u.utm_source NOT IN('test','preproduction','production','newtest','channel_10')
AND mem."multipleChannelsId" = 1   GROUP BY u.utm_source  ORDER BY userCount DESC LIMIT 10;
