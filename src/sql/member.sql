/*INNER JOIN (SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e
WHERE e.event ='RegisterEvent' AND e.platform = 'ios'
AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' ) register
ON e.distinct_id = register.memberId;*/

SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM (select * from songshu_cs_payment_record
WHERE "PaymentModeType" = 2 AND "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
GROUP BY "MergePaymentNo", "PaymentModeType";

-- 会员漏斗图 (访客) 单平台 对应 MemberFunnelRepository 会员漏斗（访客）(单平台)
SELECT
    count(a.distinct_id) AS acount,
    count(b.distinct_id) AS bcount,
    count(c.distinct_id) AS ccount,
    count(d.distinct_id) AS dcount,
    count(e.distinct_id) AS ecount
FROM
    -- 统计时间段内启动应用的访客数
    (SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = 'AppLaunch'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND
           (e.os = 'iOS' OR e.platform = 'ios')) a
    LEFT JOIN

    -- 统计时间段内浏览商品详情页的访客数
    (SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = '$pageview'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00'
           AND (e.url LIKE '#/tabs/categories/productInfo%'
                OR e.url LIKE '#/tabs/cart/productInfo%'
                OR e.url LIKE '#/tabs/index/productInfo%'
                OR e.url LIKE '#/tabs/user/productInfo%'
                OR e.url LIKE '#/tabs/integral/integralInfo%' AND e.platform = 'ios')) b
        ON a.distinct_id = b.distinct_id
    LEFT JOIN

    -- 统计时间段内 在商品详情页加入购物车的访客数
    (SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = 'AddCardEvent'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform = 'ios' /*AND
           e.isFromProductInfo = TRUE*/) c
        ON b.distinct_id = c.distinct_id
    LEFT JOIN

    -- 统计时间段内 提交订单的访客数
    (SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = 'CreateOrderEvent'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform = 'ios') d
        ON c.distinct_id = d.distinct_id
    LEFT JOIN

    -- 统计时间段内 支付订单的访客数
    (SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = 'OrderPaymentEvent'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform = 'ios') e
        ON d.distinct_id = e.distinct_id;

-- 会员漏斗图 (访客) 全平台 对应 MemberFunnelRepository 会员漏斗（访客）(全平台)
SELECT
    count(a.distinct_id) AS acount,
    count(b.distinct_id) AS bcount,
    count(c.distinct_id) AS ccount,
    count(d.distinct_id) AS dcount,
    count(e.distinct_id) AS ecount
FROM
    -- 统计时间段内启动应用的访客数
    (SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = 'AppLaunch'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' ) a
    LEFT JOIN

    -- 统计时间段内浏览商品详情页的访客数
    (SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = '$pageview'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00'
           AND (e.url LIKE '#/tabs/categories/productInfo%'
                OR e.url LIKE '#/tabs/cart/productInfo%'
                OR e.url LIKE '#/tabs/index/productInfo%'
                OR e.url LIKE '#/tabs/user/productInfo%'
                OR e.url LIKE '#/tabs/integral/integralInfo%' )) b
        ON a.distinct_id = b.distinct_id
    LEFT JOIN

    -- 统计时间段内 在商品详情页加入购物车的访客数
    (SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = 'AddCardEvent'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00'  /*AND
           e.isFromProductInfo = TRUE*/) c
        ON b.distinct_id = c.distinct_id
    LEFT JOIN

    -- 统计时间段内 提交订单的访客数
    (SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = 'CreateOrderEvent'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' ) d
        ON c.distinct_id = d.distinct_id
    LEFT JOIN

    -- 统计时间段内 支付订单的访客数
    (SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = 'OrderPaymentEvent'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' ) e
        ON d.distinct_id = e.distinct_id;

-- 会员漏斗图 (注册用户) 单平台 对应 MemberFunnelRepository 会员漏斗（注册会员）(单平台)
SELECT
    count(a.distinct_id) AS acount,
    count(b.distinct_id) AS bcount,
    count(c.distinct_id) AS ccount,
    count(d.distinct_id) AS dcount,
    count(e.distinct_id) AS ecount
FROM
    -- 统计时间段内启动应用的访客数
    (SELECT DISTINCT i.distinct_id
     FROM (SELECT DISTINCT e.distinct_id
           FROM songshu_shence_events e
           WHERE e.event = 'AppLaunch'
                 AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND
                 (e.os = 'iOS' OR e.platform = 'ios')
          ) i INNER JOIN (SELECT DISTINCT e.distinct_id AS memberId
                          FROM songshu_shence_events e
                          WHERE e.event = 'RegisterEvent' AND e.platform = 'ios'
                                AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
             ON i.distinct_id = register.memberId) a
    LEFT JOIN

    -- 统计时间段内浏览商品详情页的访客数
    (SELECT DISTINCT i2.distinct_id
     FROM (SELECT DISTINCT e.distinct_id
           FROM songshu_shence_events e
           WHERE e.event = '$pageview'
                 AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00'
                 AND (e.url LIKE '#/tabs/categories/productInfo%'
                      OR e.url LIKE '#/tabs/cart/productInfo%'
                      OR e.url LIKE '#/tabs/index/productInfo%'
                      OR e.url LIKE '#/tabs/user/productInfo%'
                      OR e.url LIKE '#/tabs/integral/integralInfo%' AND e.platform = 'ios')) i2 INNER JOIN
         (SELECT DISTINCT e.distinct_id AS memberId
          FROM songshu_shence_events e
          WHERE e.event = 'RegisterEvent' AND e.platform = 'ios'
                AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
             ON i2.distinct_id = register.memberId) b
        ON a.distinct_id = b.distinct_id
    LEFT JOIN

    -- 统计时间段内 在商品详情页加入购物车的访客数
    (SELECT DISTINCT i3.distinct_id
     FROM (SELECT DISTINCT e.distinct_id
           FROM songshu_shence_events e
           WHERE e.event = 'AddCardEvent'
                 AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform = 'ios' /*AND
           e.isFromProductInfo = TRUE*/) i3 INNER JOIN
         (SELECT DISTINCT e.distinct_id AS memberId
          FROM songshu_shence_events e
          WHERE e.event = 'RegisterEvent' AND e.platform = 'ios'
                AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
             ON i3.distinct_id = register.memberId) c
        ON b.distinct_id = c.distinct_id
    LEFT JOIN

    -- 统计时间段内 提交订单的访客数
    (SELECT DISTINCT i4.distinct_id
     FROM (SELECT DISTINCT e.distinct_id
           FROM songshu_shence_events e
           WHERE e.event = 'CreateOrderEvent'
                 AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform = 'ios') i4 INNER JOIN
         (SELECT DISTINCT e.distinct_id AS memberId
          FROM songshu_shence_events e
          WHERE e.event = 'RegisterEvent' AND e.platform = 'ios'
                AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
             ON i4.distinct_id = register.memberId) d
        ON c.distinct_id = d.distinct_id
    LEFT JOIN

    -- 统计时间段内 支付订单的访客数
    (SELECT DISTINCT i5.distinct_id
     FROM(SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = 'OrderPaymentEvent'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' AND e.platform = 'ios')i5 INNER JOIN
         (SELECT DISTINCT e.distinct_id AS memberId
          FROM songshu_shence_events e
          WHERE e.event = 'RegisterEvent' AND e.platform = 'ios'
                AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
             ON i5.distinct_id = register.memberId) e
        ON d.distinct_id = e.distinct_id;
-- 会员漏斗图 (注册用户) 全平台 对应 MemberFunnelRepository 会员漏斗（注册会员）(全平台)
SELECT
    count(a.distinct_id) AS acount,
    count(b.distinct_id) AS bcount,
    count(c.distinct_id) AS ccount,
    count(d.distinct_id) AS dcount,
    count(e.distinct_id) AS ecount
FROM
    -- 统计时间段内启动应用的访客数
    (SELECT DISTINCT i.distinct_id
     FROM (SELECT DISTINCT e.distinct_id
           FROM songshu_shence_events e
           WHERE e.event = 'AppLaunch'
                 AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00'
          ) i INNER JOIN (SELECT DISTINCT e.distinct_id AS memberId
                          FROM songshu_shence_events e
                          WHERE e.event = 'RegisterEvent'
                                AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
             ON i.distinct_id = register.memberId) a
    LEFT JOIN

    -- 统计时间段内浏览商品详情页的访客数
    (SELECT DISTINCT i2.distinct_id
     FROM (SELECT DISTINCT e.distinct_id
           FROM songshu_shence_events e
           WHERE e.event = '$pageview'
                 AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00'
                 AND (e.url LIKE '#/tabs/categories/productInfo%'
                      OR e.url LIKE '#/tabs/cart/productInfo%'
                      OR e.url LIKE '#/tabs/index/productInfo%'
                      OR e.url LIKE '#/tabs/user/productInfo%'
                      OR e.url LIKE '#/tabs/integral/integralInfo%' )) i2 INNER JOIN
         (SELECT DISTINCT e.distinct_id AS memberId
          FROM songshu_shence_events e
          WHERE e.event = 'RegisterEvent'
                AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
             ON i2.distinct_id = register.memberId) b
        ON a.distinct_id = b.distinct_id
    LEFT JOIN

    -- 统计时间段内 在商品详情页加入购物车的访客数
    (SELECT DISTINCT i3.distinct_id
     FROM (SELECT DISTINCT e.distinct_id
           FROM songshu_shence_events e
           WHERE e.event = 'AddCardEvent'
                 AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00'  /*AND
           e.isFromProductInfo = TRUE*/) i3 INNER JOIN
         (SELECT DISTINCT e.distinct_id AS memberId
          FROM songshu_shence_events e
          WHERE e.event = 'RegisterEvent'
                AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
             ON i3.distinct_id = register.memberId) c
        ON b.distinct_id = c.distinct_id
    LEFT JOIN

    -- 统计时间段内 提交订单的访客数
    (SELECT DISTINCT i4.distinct_id
     FROM (SELECT DISTINCT e.distinct_id
           FROM songshu_shence_events e
           WHERE e.event = 'CreateOrderEvent'
                 AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' ) i4 INNER JOIN
         (SELECT DISTINCT e.distinct_id AS memberId
          FROM songshu_shence_events e
          WHERE e.event = 'RegisterEvent'
                AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
             ON i4.distinct_id = register.memberId) d
        ON c.distinct_id = d.distinct_id
    LEFT JOIN

    -- 统计时间段内 支付订单的访客数
    (SELECT DISTINCT i5.distinct_id
     FROM(SELECT DISTINCT e.distinct_id
     FROM songshu_shence_events e
     WHERE e.event = 'OrderPaymentEvent'
           AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' )i5 INNER JOIN
         (SELECT DISTINCT e.distinct_id AS memberId
          FROM songshu_shence_events e
          WHERE e.event = 'RegisterEvent'
                AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00') register
             ON i5.distinct_id = register.memberId) e
        ON d.distinct_id = e.distinct_id;

-- 统计时间段内，注册成功的用户数 对应 MemberDetailRepository 注册用户数
SELECT count(DISTINCT(m.id)) AS memberCount FROM songshu_cs_member m
WHERE m."regTime" BETWEEN ?1 AND ?2;

-- 统计时间段内所有已支付用户，不包含已取消/已关闭状态（去重）对应 MemberDetailRepository 消费用户数
SELECT count(DISTINCT o."MemberId") AS  memberCount FROM  songshu_cs_order o
INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM (select * from songshu_cs_payment_record
WHERE "PaymentModeType" = 2 AND "PaidTime"
BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D') AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
GROUP BY "MergePaymentNo") pr ON pr. "MergePaymentNo"  = op. "MergePaymentId"
WHERE  op. "PaymentStatus" = 1 AND  o. "orderType" in(0,1) AND  o. "OrderStatus" NOT IN (6,7)
AND pr.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00';

-- 统计时间段内，注册并消费的用户数占比（去重）公式：注册并消费的用户数÷注册用户数*100% 对应 MemberDetailRepository 注册消费转化率
SELECT COALESCE(sum(main.pay) / sum(main.register), 0) AS consumeTransferRate FROM
    (SELECT MAX(CASE type WHEN 'pay' THEN memberCount ELSE 0 END) pay,
            MAX(CASE type WHEN 'register' THEN memberCount ELSE 0 END) register FROM
         (SELECT count(DISTINCT o."MemberId") AS memberCount, 'pay' AS type, '1' AS date FROM songshu_cs_order o
              INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
              INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM
                 (SELECT * FROM songshu_cs_payment_record WHERE "PaymentModeType" = 2 AND
                  "PaidTime" BETWEEN (CAST('2016-06-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D')
                  AND (CAST('2016-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
                  GROUP BY "MergePaymentNo") prr ON prr."MergePaymentNo" = op."MergePaymentId"
                  INNER JOIN songshu_cs_member m ON m.id = o."MemberId"
          WHERE op."PaymentStatus" = 1 AND o."orderType" IN (0, 1) AND o."OrderStatus" NOT IN (6, 7)
                AND m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'
                AND prr.paidTime BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00'
          UNION ALL
          SELECT count(DISTINCT (m.id)) AS memberCount, 'register' AS type, '2' AS date
          FROM songshu_cs_member m
          WHERE m."regTime" BETWEEN '2016-06-01 00:00:00' AND '2016-08-01 00:00:00') base
     GROUP BY date) main;

-- 统计时间段内有复购行为的用户占比（去重）。 PS:复购用户概念:统计时间段之前有过成功购买行为的用户在统计时间段内再次消费。公式：（复购用户数÷消费用户数）*100% 对应 MemberDetailRepository 复购率
SELECT count(DISTINCT (n."MemberId")) AS rebuyMemberCount FROM
    (SELECT DISTINCT (o."MemberId") FROM songshu_cs_order o
         INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
         INNER JOIN (SELECT pr."MergePaymentNo", MAX(pr."PaidTime") AS paidTime FROM
            (SELECT * FROM songshu_cs_payment_record WHERE "PaymentModeType" = 2
             AND "PaidTime" BETWEEN (CAST('2016-08-01 00:00:00' AS TIMESTAMP) - INTERVAL '1 D')
             AND ( CAST('2017-08-01 00:00:00' AS TIMESTAMP) + INTERVAL '1 D')) pr
             GROUP BY "MergePaymentNo") prr ON prr."MergePaymentNo" = op."MergePaymentId"
         WHERE op."PaymentStatus" = 1 AND o."orderType" IN (0, 1) AND o."OrderStatus" NOT IN (6, 7)
         AND prr.paidTime BETWEEN '2016-08-01 00:00:00' AND '2017-08-01 00:00:00' AND o."Channel" = 1) n
WHERE n."MemberId" IN
      (SELECT DISTINCT (o."MemberId") FROM songshu_cs_order o
           INNER JOIN songshu_cs_order_payable op ON op."OrderId" = o."Id"
           INNER JOIN (SELECT "MergePaymentNo",  MAX("PaidTime") AS paidTime FROM songshu_cs_payment_record
           WHERE "PaymentModeType" = 2 AND "PaidTime" < '2016-08-01 00:00:00'
           GROUP BY "MergePaymentNo") prr ON prr."MergePaymentNo" = op."MergePaymentId"
        WHERE op."PaymentStatus" = 1 AND o."orderType" IN (0, 1) AND o."OrderStatus" NOT IN (6, 7)
        AND o."Channel" = 1);

-- 统计时间段内，启动应用次数 对应 MemberDetailRepository 启动次数
SELECT count(1) FROM songshu_shence_events e WHERE e.event ='AppLaunch'
AND e.times BETWEEN ?1 AND ?2;

-- 平均访问深度：每个访客的页面浏览量的均值 公式 页面浏览量÷访客数
-- 页面浏览量: 时间段内 页面浏览的总数 对应 MemberDetailRepository 页面浏览量
SELECT count(1) FROM songshu_shence_events e WHERE e.event ='$pageview'
AND e.times BETWEEN ?1 AND ?2 AND e.platform =?3 ;

-- 统计时间段内，注册成功的用户数 对应 ChannelRegisterMemberRepository 渠道注册用户

SELECT COUNT(DISTINCT base.memberId) AS memberCount , upper(base.utm_source) FROM
(SELECT mem. "id " AS memberId,
    CASE
    WHEN mem. "multipleChannelsId " = 1 AND u.utm_source IS NOT NULL THEN u.utm_source
    WHEN mem. "multipleChannelsId " = 1 AND u.utm_source IS NULL THEN 'yingyongbao'
    WHEN mem. "multipleChannelsId " = 2 THEN 'appstore'
    WHEN mem. "multipleChannelsId " = 3 THEN 'weixin'
    WHEN mem. "multipleChannelsId " = 5 THEN 'wap'
    ELSE 'wap'
    END  AS utm_source
FROM songshu_cs_member mem
LEFT JOIN songshu_shence_users u  ON u.second_id = mem. "id "
WHERE  mem. "regTime " BETWEEN ?1 AND ?2 AND mem. "multipleChannelsId " = ?3)base
GROUP BY base.utm_source ORDER BY memberCount DESC LIMIT  ?4;

-- 根据时间获取事件次数 对应 MemberShareRepository 获取用户分享
SELECT count(1) FROM songshu_shence_events se WHERE se.event= ?1
AND se.times BETWEEN ?2 AND ?3 AND se.platform= ?4 ;

-- 根据时间获取事件次数趋势 对应 MemberShareRepository 获取用户分享趋势
SELECT tss.stime, tss.etime, count(comt.event)
FROM(SELECT se.* FROM songshu_shence_events se
WHERE se.event = ?1 AND se.times BETWEEN ?2 AND ?3 AND se.platform = ?4 ) comt
RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?5 * INTERVAL '1 second' AS etime
FROM (SELECT generate_series( ?2 , ?3 , ?5 * INTERVAL '1 second')) ts) tss
ON (comt.times < tss.etime AND comt.times >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime ;
