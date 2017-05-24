

-- 指定时间内各渠道下载量及注册用户数统计（app平台）  对应 ChannelPageInfoRepository  渠道下载量及注册用户数
SELECT UPPER(install.utm_source) AS advSource ,COALESCE(install.memberCount,0) AS installCount,COALESCE(reg.memberCount,0) AS regCount
FROM (SELECT COUNT(DISTINCT base.distinctId) AS memberCount , base.utm_source FROM
                (SELECT
                     e.distinct_id AS distinctId,
                     CASE
                     WHEN e.os = 'Android' AND e.utm_source IS NOT NULL  THEN e.utm_source
                     WHEN e.os = 'Android' AND e.utm_source IS NULL THEN 'yingyongbao'
                     WHEN e.os = 'iOS'     THEN 'appstore'
                     WHEN e.os = 'weixin'  THEN 'weixin'
                     WHEN e.os = 'wap'     THEN 'wap'
                     ELSE 'yingyongbao'
                     END  AS utm_source,e.os
                 FROM songshu_shence_events e WHERE e.event ='AppInstall' AND e.times BETWEEN  '2016-01-01 00:00:00' AND '2017-08-01 00:00:00'
                 AND e.os = 'iOS')base
            GROUP BY base.utm_source
     )install
LEFT JOIN (SELECT COUNT(DISTINCT base.memberId) AS memberCount , base.utm_source FROM
                (SELECT mem."id" AS memberId,
                        CASE
                        WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source
                        WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'yingyongbao'
                        WHEN mem."multipleChannelsId" = 2 THEN 'appstore'
                        WHEN mem."multipleChannelsId" = 3 THEN 'weixin'
                        WHEN mem."multipleChannelsId" = 5 THEN 'wap'
                        ELSE 'wap'
                        END  AS utm_source
                 FROM songshu_cs_member mem
                     LEFT JOIN songshu_shence_users u  ON u.second_id = mem."id"
                 WHERE  mem."regTime" BETWEEN '2016-01-01 00:00:00' AND '2017-08-01 00:00:00' AND mem."multipleChannelsId" = 2)base
            GROUP BY base.utm_source
          )reg ON reg.utm_source = install.utm_source
ORDER BY install.memberCount DESC,reg.memberCount DESC;

-- 指定时间内各渠道注册用户数统计(非app平台)  对应 ChannelPageInfoRepository  渠道下载量及注册用户数
SELECT UPPER(reg.utm_source) AS advSource ,0 AS installCount,COALESCE(reg.memberCount,0) AS regCount
FROM (SELECT COUNT(DISTINCT base.memberId) AS memberCount , base.utm_source
      FROM (SELECT  mem."id" AS memberId,
                    CASE
                    WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source
                    WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'yingyongbao'
                    WHEN mem."multipleChannelsId" = 2 THEN 'appstore'
                    WHEN mem."multipleChannelsId" = 3 THEN 'weixin'
                    WHEN mem."multipleChannelsId" = 5 THEN 'wap'
                    ELSE 'wap'
                    END  AS utm_source
            FROM songshu_cs_member mem
            LEFT JOIN songshu_shence_users u  ON u.second_id = mem."id"
            WHERE  mem."regTime" BETWEEN '2016-01-01 00:00:00' AND '2017-08-01 00:00:00' AND mem."multipleChannelsId" = 1 AND mem."multipleChannelsId" NOT IN(1,2)
           )base
           GROUP BY base.utm_source
     )reg
ORDER BY reg.memberCount DESC;


-- 指定时间内渠道销售额（全渠道）  对应 ChannelRevenueRepository  渠道销售额
SELECT COALESCE(SUM(cpr.amount),0)
FROM songshu_cs_order o
INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime,SUM("Amount") AS amount FROM (SELECT * FROM songshu_cs_payment_record
            WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
            GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = p."MergePaymentId"
WHERE o."OrderStatus" not IN (6,7) AND p."PaymentStatus" = 1
AND cpr.paidTime BETWEEN '2016-01-01 00:00:00' AND '2017-08-01 00:00:00' AND  o."Channel" = 1;

-- 指定时间内单渠道销售额（单渠道）  对应 ChannelRevenueRepository  单渠道销售额
SELECT COALESCE(SUM(cpr.amount),0)
FROM songshu_cs_order o
INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime,SUM("Amount") AS amount FROM (SELECT * FROM songshu_cs_payment_record
            WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2017-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
            GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = p."MergePaymentId"
LEFT JOIN(SELECT mem."id" AS memberId,
                 CASE WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source)
                 WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO'
                 WHEN mem."multipleChannelsId" = 2 THEN 'APPLESTORE'
                 WHEN mem."multipleChannelsId" = 3 THEN 'WEIXIN'
                 WHEN mem."multipleChannelsId" = 5 THEN 'WAP'
                 ELSE 'WAP' END AS utm_source
                 FROM songshu_cs_member mem
                 LEFT JOIN songshu_shence_users u ON u.second_id = mem."id"
         ) tsource ON o."MemberId"=tsource.memberId
WHERE o."OrderStatus" NOT IN (6, 7) AND p."PaymentStatus" = 1
AND cpr.paidTime BETWEEN '2016-01-01 00:00:00' AND '2017-08-01 00:00:00' AND o."Channel" = 2 AND tsource.utm_source= 'APPLESTORE';

-- 指定时间内渠道订单量（全渠道）  对应 ChannelOrderCountRepository  全渠道订单量
SELECT COUNT(DISTINCT o."Id")
FROM songshu_cs_order o
INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record
            WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
            GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = p."MergePaymentId"
WHERE o."OrderStatus" not IN (6,7) AND p."PaymentStatus" = 1
AND cpr.paidTime BETWEEN '2016-01-01 00:00:00' AND '2017-08-01 00:00:00' AND o."Channel" = 2;

-- 指定时间内渠道订单量（单渠道）  对应 ChannelOrderCountRepository  单渠道订单量
SELECT COUNT(DISTINCT o."Id")
FROM songshu_cs_order o
    INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
    INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime,SUM("Amount") AS amount FROM (SELECT * FROM songshu_cs_payment_record
                WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2017-07-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2017-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = p."MergePaymentId"
    LEFT JOIN(SELECT mem."id" AS memberId,
                     CASE WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source)
                     WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO'
                     WHEN mem."multipleChannelsId" = 2 THEN 'APPLESTORE'
                     WHEN mem."multipleChannelsId" = 3 THEN 'WEIXIN'
                     WHEN mem."multipleChannelsId" = 5 THEN 'WAP'
                     ELSE 'WAP' END AS utm_source
              FROM songshu_cs_member mem
                  LEFT JOIN songshu_shence_users u ON u.second_id = mem."id"
             ) tsource ON o."MemberId"=tsource.memberId
WHERE o."OrderStatus" NOT IN (6, 7) AND p."PaymentStatus" = 1
AND  cpr.paidTime BETWEEN '2017-07-01 00:00:00' AND '2017-08-01 00:00:00' AND o."Channel" = 2 AND tsource.utm_source= 'APPLESTORE';


-- 指定时间内渠道客单价（全渠道）  对应 ChannelAvgOrderRevenueRepository  全渠道客单价
SELECT COALESCE(SUM(cpr.amount) / COUNT(DISTINCT o."Id"),0) as result
FROM songshu_cs_order o
    INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
    INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime,SUM("Amount") AS amount FROM (SELECT * FROM songshu_cs_payment_record
                WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = p."MergePaymentId"
WHERE o."OrderStatus" not IN (6,7) AND p."PaymentStatus" = 1
AND cpr.paidTime BETWEEN '2016-01-01 00:00:00' AND '2017-08-01 00:00:00' AND o."Channel" = 2;

-- 指定时间内渠道客单价（单渠道）  对应 ChannelAvgOrderRevenueRepository  单渠道客单价
SELECT COALESCE(SUM(cpr.amount) / COUNT(DISTINCT o."Id"), 0) AS result
FROM songshu_cs_order o
    INNER JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
    INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime,SUM("Amount") AS amount FROM (SELECT * FROM songshu_cs_payment_record
                WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-07-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2017-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = p."MergePaymentId"
    LEFT JOIN(SELECT mem."id" AS memberId,
                     CASE WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source)
                     WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO'
                     WHEN mem."multipleChannelsId" = 2 THEN 'APPLESTORE'
                     WHEN mem."multipleChannelsId" = 3 THEN 'WEIXIN'
                     WHEN mem."multipleChannelsId" = 5 THEN 'WAP'
                     ELSE 'WAP' END AS utm_source
              FROM songshu_cs_member mem
                  LEFT JOIN songshu_shence_users u ON u.second_id = mem."id"
             ) tsource ON o."MemberId"=tsource.memberId
WHERE o."OrderStatus" NOT IN (6, 7) AND p."PaymentStatus" = 1
AND  cpr.paidTime BETWEEN '2016-07-01 00:00:00' AND '2017-08-01 00:00:00' AND o."Channel" = 2 AND tsource.utm_source= 'APPLESTORE';

-- 指定时间内渠道页面访客数（全渠道）  对应 ChannelUniqueVisitorsRepository  全渠道页面访客数
SELECT COUNT(DISTINCT se.distinct_id)
FROM songshu_shence_events se
WHERE se.event = '$pageview' AND se.times BETWEEN '2017-02-01 00:00:00' AND '2017-08-01 00:00:00' AND se.platform = 'ios' ;

-- 指定时间内渠道页面访客数（单渠道）  对应 ChannelUniqueVisitorsRepository  单渠道页面访客数
SELECT COUNT(distinct tcom.distinct_id)
FROM(SELECT se.distinct_id AS distinct_id,
     CASE WHEN se.platform = 'android' AND se.utm_source IS NOT NULL THEN upper(se.utm_source)
     WHEN se.platform = 'android' AND se.utm_source IS NULL THEN 'YINGYONGBAO'
     WHEN se.platform = 'ios' THEN 'APPLESTORE'
     WHEN se.platform = 'weixin' THEN 'WEIXIN'
     WHEN se.platform = 'wap' THEN 'WAP'
     ELSE 'WAP' END AS utm_source, se.platform AS platform
     FROM songshu_shence_events se
     WHERE se.event = '$pageview' AND se.times BETWEEN '2017-02-01 00:00:00' AND '2017-03-01 00:00:00' AND se.platform = 'ios'
    ) AS tcom
WHERE tcom.utm_source = 'APPLESTORE';


-- 指定时间内渠道消费用户数（全渠道）  对应 ChannelConsumerCountRepository  全渠道消费用户数
SELECT COUNT(DISTINCT o."MemberId") AS  memberCount
FROM  songshu_cs_order o
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record
            WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-07-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2017-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
            GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
WHERE  op."PaymentStatus" = 1 AND  o."orderType" in(0,1) AND  o."OrderStatus" NOT IN (6,7)
AND cpr.paidTime BETWEEN '2016-07-01 00:00:00' AND '2017-08-01 00:00:00' AND o."Channel" = 1;

-- 指定时间内渠道消费用户数（单渠道）  对应 ChannelConsumerCountRepository  单渠道消费用户数
SELECT COUNT(DISTINCT o."MemberId") AS memberCount FROM songshu_cs_order o
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record
            WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2017-02-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2017-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
            GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = op."MergePaymentId"
LEFT JOIN(SELECT mem."id" AS memberId,
             CASE WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source)
             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO'
             WHEN mem."multipleChannelsId" = 2 THEN 'APPLESTORE'
             WHEN mem."multipleChannelsId" = 3 THEN 'WEIXIN'
             WHEN mem."multipleChannelsId" = 5 THEN 'WAP'
             ELSE 'WAP' END AS utm_source
             FROM songshu_cs_member mem
             LEFT JOIN songshu_shence_users u ON u.second_id = mem."id"
         ) tsource ON o."MemberId"=tsource.memberId
WHERE o."OrderStatus" NOT IN (6, 7) AND op."PaymentStatus" = 1
AND  cpr.paidTime BETWEEN '2017-02-01 00:00:00' AND '2017-08-01 00:00:00' AND o."Channel" = 2 AND tsource.utm_source= 'APPLESTORE';

-- 指定时间内渠道毛利率（全渠道）  对应 ChannelGrossMarginRateRepository  全渠道毛利率
SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END),0.0) AS goodsGrossMargin
FROM(SELECT SUM(base."Quantity"* base.cost)AS referCost, SUM(base."AfterFoldingPrice")AS AfterFoldingPrice
     FROM(SELECT  coi."Quantity",CASE WHEN COALESCE(coi."ReferCost",0) =0 THEN g."CostPrice" ELSE coi."ReferCost" END  AS cost,coi."AfterFoldingPrice"
          FROM songshu_cs_order_item coi
          INNER JOIN songshu_cs_goods g ON g."Id" = coi."GoodsId"
          INNER JOIN (SELECT DISTINCT oo."Id" FROM songshu_cs_order oo
                        INNER JOIN songshu_cs_order_payable cop ON oo."Id" = cop. "OrderId"
                        INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record
                                    WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-05-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2017-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                                    GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
                        WHERE cop."PaymentStatus" = 1 AND oo. "OrderStatus" NOT IN (6,7) AND oo. "orderType" IN(0, 1)
                        AND cpr.paidTime BETWEEN '2016-05-01 00:00:00' AND '2017-08-01 00:00:00'  AND oo. "Channel" = 1
                     ) coo ON coi."OrderId" = coo. "Id"
     )base
)grossmargin;

-- 指定时间内渠道毛利率（单渠道）  对应 ChannelGrossMarginRateRepository  单渠道毛利率
SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END),0.0) AS goodsGrossMargin
FROM(SELECT SUM(base."Quantity"* base.cost)AS referCost, SUM(base."AfterFoldingPrice")AS AfterFoldingPrice
     FROM(SELECT  coi."Quantity",CASE WHEN COALESCE(coi."ReferCost",0) =0 THEN g."CostPrice" ELSE coi."ReferCost" END  AS cost,coi."AfterFoldingPrice"
          FROM songshu_cs_order_item coi
          INNER JOIN songshu_cs_goods g ON g."Id" = coi."GoodsId"
          INNER JOIN (SELECT DISTINCT oo."Id"
                      FROM songshu_cs_order oo
                      INNER JOIN songshu_cs_order_payable cop ON oo."Id" = cop."OrderId"
                      INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record
                                  WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-05-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2017-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                                  GROUP BY "MergePaymentNo", "PaymentModeType") cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
                      LEFT JOIN (SELECT mem."id" AS memberId, CASE
                                    WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source)
                                    WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO'
                                    WHEN mem."multipleChannelsId" = 2 THEN 'APPLESTORE'
                                    WHEN mem."multipleChannelsId" = 3 THEN 'WEIXIN'
                                    WHEN mem."multipleChannelsId" = 5 THEN 'WAP' ELSE 'WAP' END AS utm_source
                                    FROM songshu_cs_member mem
                                    LEFT JOIN songshu_shence_users u ON u.second_id = mem."id") tsource ON oo."MemberId" = tsource.memberId
                      WHERE cop."PaymentStatus" = 1 AND oo. "OrderStatus" NOT IN (6,7) AND oo. "orderType" IN(0, 1)
                      AND cpr.paidTime BETWEEN '2016-05-01 00:00:00' AND '2017-08-01 00:00:00'  AND oo."Channel" =1 AND tsource.utm_source = 'APPLESTORE'
                     ) coo  ON coi."OrderId" = coo."Id"
     )base
) grossmargin;

-- 指定时间内性别分布(只统计设置了性别的用户)  对应 AgeAndSexDistributionRepository  性别分布
SELECT CASE WHEN sexInfo.gender =1  THEN '男'
       WHEN sexInfo.gender =0  THEN '女'
       END AS sexGroup, sexInfo.memberCount AS memberCount
FROM (SELECT info."gender" as gender, count(DISTINCT(m.id)) as memberCount
      FROM  songshu_cs_member m
      INNER JOIN songshu_cs_member_info info ON info."memberId" = m.id
      WHERE m."regTime" BETWEEN '2016-05-01 00:00:00' AND '2017-06-01 00:00:00' AND m."multipleChannelsId" = 1 AND info.gender IN (0,1)
      group BY info."gender" ORDER BY info."gender" desc
) sexInfo;

-- 指定时间内用户年龄分布  对应 AgeAndSexDistributionRepository  用户年龄分布
SELECT ageGroupInfo.ageGroup AS ageGroup , COALESCE(SUM(ageGroupInfo.memberCount),0) AS memberCount FROM
(SELECT
     CASE
     WHEN birthInfo.age<=20 OR birthInfo.age IS NULL  THEN '0-20岁'
     WHEN birthInfo.age>=21 AND birthInfo.age<=25 THEN '21-25岁'
     WHEN birthInfo.age>=26 AND birthInfo.age<=30 THEN '26-30岁'
     WHEN birthInfo.age>=31 AND birthInfo.age<=35 THEN '31-35岁'
     WHEN birthInfo.age>=36 THEN '36岁+'
     END AS ageGroup,
     birthInfo.memberCount AS memberCount
     FROM  (SELECT (EXTRACT(YEAR FROM NOW())-EXTRACT(YEAR FROM info."birthday")-1) +
              CASE
              WHEN ( DATE_PART('month',info."birthday") < DATE_PART('month',NOW())) THEN 0
              WHEN ( DATE_PART('month',info."birthday") = DATE_PART('month',NOW())) AND (DATE_PART('day',info."birthday") < DATE_PART('day',NOW())) THEN 0
              ELSE 1
              END AS age
              ,COUNT(DISTINCT(m.id)) as memberCount
              FROM  songshu_cs_member m
              INNER JOIN songshu_cs_member_info info ON info."memberId" = m.id
              WHERE m."regTime"  BETWEEN '2016-05-01 00:00:00' AND '2016-06-01 00:00:00' AND m."multipleChannelsId" = 1 AND info.birthday IS NOT NULL
              GROUP BY age ORDER BY age ASC
            ) birthInfo
)ageGroupInfo
GROUP BY ageGroupInfo.ageGroup ORDER BY ageGroupInfo.ageGroup ASC;


-- 指定时间内机型分布排行  对应 ManufacturerRankRepository  机型分布排行
SELECT UPPER(se.manufacturer) AS tmanufacturer,COUNT(DISTINCT se.distinct_id) AS tcount
FROM songshu_shence_events se
WHERE se.event = '$pageview' AND se.times BETWEEN '2016-01-01 00:00:00' AND '2017-06-01 00:00:00'
AND se.platform = 'ios' AND se.manufacturer IS NOT NULL AND  UPPER(se.manufacturer) NOT LIKE 'ITOOLSAVM%'
GROUP BY UPPER(se.manufacturer) ORDER BY tcount DESC LIMIT 10;

-- 指定时间内地区排行  对应 RegionRankRepository  地区排行
SELECT se.city, COUNT(DISTINCT se.distinct_id) AS tcount
FROM songshu_shence_events se
WHERE se.event = '$pageview' AND se.times BETWEEN '2016-01-01 00:00:00' AND '2016-09-01 00:00:00'
AND se.platform = 'ios' AND se.city IS NOT NULL AND se.city != '未知'
GROUP BY se.city ORDER BY tcount DESC LIMIT 10;

-- 指定时间内用户的访问深度（页面访问数）  对应 ManufacturerRankRepository  访问深度
SELECT tcomm.tdeep,COUNT(tcomm.did)
FROM(SELECT CASE WHEN tcom.tcount = 1 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_ONE+"'
        WHEN tcom.tcount >= 2 AND tcom.tcount <= 5 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_TWO+"'
        WHEN tcom.tcount >= 6 AND tcom.tcount <= 10 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_THREE+"'
        WHEN tcom.tcount >= 11 AND tcom.tcount <= 30 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_FOUR+"'
        WHEN tcom.tcount >= 31 AND tcom.tcount <= 40 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_FIVE+"'
        WHEN tcom.tcount >= 41 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_SIX+"' END AS tdeep, tcom.distinct_id AS did
        FROM (SELECT COUNT(se.url) AS tcount, distinct_id
              FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.platform = 'ios'
              AND se.times BETWEEN '2016-05-01 00:00:00' AND '2017-09-01 00:00:00' GROUP BY se.distinct_id
             ) tcom
) tcomm
where tcomm.tdeep IS NOT NULL GROUP BY tcomm.tdeep;
