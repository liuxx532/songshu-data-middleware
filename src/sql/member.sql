/*INNER JOIN (SELECT DISTINCT e.distinct_id AS memberId FROM songshu_shence_events e
WHERE e.event ='RegisterEvent' AND e.platform = 'ios'
AND e.times BETWEEN '2016-05-11 00:00:00' AND '2017-05-11 00:00:00' ) register
ON e.distinct_id = register.memberId;*/

-- 会员漏斗图 (访客) 单平台
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

-- 会员漏斗图 (访客) 全平台
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

-- 会员漏斗图 (注册用户) 单平台
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
-- 会员漏斗图 (注册用户) 全平台
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
