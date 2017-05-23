package com.comall.songshu.repository.member;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 会员漏斗Repository
 * Created by huanghaizhou on 2017/5/5.
 */
public interface MemberFunnelRepository  extends JpaRepository<Author,Long> {

    /**
     * 会员漏斗（访客）(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT " +
        "    count(a.distinct_id) AS acount, " +
        "    count(b.distinct_id) AS bcount, " +
        "    count(c.distinct_id) AS ccount, " +
        "    count(d.distinct_id) AS dcount, " +
        "    count(e.distinct_id) AS ecount " +
        "FROM " +
        "    (SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = 'AppLaunch' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 ) a " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = '$pageview' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 " +
        "           AND (e.url LIKE '#/tabs/categories/productInfo%' " +
        "                OR e.url LIKE '#/tabs/cart/productInfo%' " +
        "                OR e.url LIKE '#/tabs/index/productInfo%' " +
        "                OR e.url LIKE '#/tabs/user/productInfo%' " +
        "                OR e.url LIKE '#/tabs/integral/integralInfo%' )) b " +
        "        ON a.distinct_id = b.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = 'AddCardEvent' " +
        "           AND e.times BETWEEN  ?1 AND  ?2  AND " +
        "           e.isFromProductInfo = TRUE ) c " +
        "        ON b.distinct_id = c.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = 'CreateOrderEvent' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 ) d " +
        "        ON c.distinct_id = d.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = 'OrderPaymentEvent' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 ) e " +
        "        ON d.distinct_id = e.distinct_id ", nativeQuery = true)
    List<Integer[]> getMemberFunnelWithAllPlatformForVisitor(Timestamp beginTime, Timestamp endTime);
    /**
     * 会员漏斗（访客）(单平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT " +
        "    count(a.distinct_id) AS acount, " +
        "    count(b.distinct_id) AS bcount, " +
        "    count(c.distinct_id) AS ccount, " +
        "    count(d.distinct_id) AS dcount, " +
        "    count(e.distinct_id) AS ecount " +
        "FROM " +
        "    (SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = 'AppLaunch' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 AND " +
        "           (e.os =  ?3 OR e.platform =  ?4)) a " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = '$pageview' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 " +
        "           AND (e.url LIKE '#/tabs/categories/productInfo%' " +
        "                OR e.url LIKE '#/tabs/cart/productInfo%' " +
        "                OR e.url LIKE '#/tabs/index/productInfo%' " +
        "                OR e.url LIKE '#/tabs/user/productInfo%' " +
        "                OR e.url LIKE '#/tabs/integral/integralInfo%' AND e.platform =  ?4)) b " +
        "        ON a.distinct_id = b.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = 'AddCardEvent' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 AND e.platform =  ?4  AND " +
        "           e.isFromProductInfo = TRUE ) c " +
        "        ON b.distinct_id = c.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = 'CreateOrderEvent' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 AND e.platform =  ?4) d " +
        "        ON c.distinct_id = d.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = 'OrderPaymentEvent' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 AND e.platform =  ?4) e " +
        "        ON d.distinct_id = e.distinct_id ", nativeQuery = true)
    List<Integer[]> getMemberFunnelWithSinglePlatformForVisitor(Timestamp beginTime, Timestamp endTime, String os, String plateFormName);
    /**
     * 会员漏斗（注册会员）(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT " +
        "    count(a.distinct_id) AS acount, " +
        "    count(b.distinct_id) AS bcount, " +
        "    count(c.distinct_id) AS ccount, " +
        "    count(d.distinct_id) AS dcount, " +
        "    count(e.distinct_id) AS ecount " +
        "FROM " +
        "    (SELECT DISTINCT i.distinct_id " +
        "     FROM (SELECT DISTINCT e.distinct_id " +
        "           FROM songshu_shence_events e " +
        "           WHERE e.event = 'AppLaunch' " +
        "                 AND e.times BETWEEN  ?1 AND  ?2 " +
        "          ) i INNER JOIN (SELECT DISTINCT e.distinct_id AS memberId " +
        "                          FROM songshu_shence_events e " +
        "                          WHERE e.event = 'RegisterEvent' " +
        "                                AND e.times BETWEEN  ?1 AND  ?2) register " +
        "             ON i.distinct_id = register.memberId) a " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT i2.distinct_id " +
        "     FROM (SELECT DISTINCT e.distinct_id " +
        "           FROM songshu_shence_events e " +
        "           WHERE e.event = '$pageview' " +
        "                 AND e.times BETWEEN  ?1 AND  ?2 " +
        "                 AND (e.url LIKE '#/tabs/categories/productInfo%' " +
        "                      OR e.url LIKE '#/tabs/cart/productInfo%' " +
        "                      OR e.url LIKE '#/tabs/index/productInfo%' " +
        "                      OR e.url LIKE '#/tabs/user/productInfo%' " +
        "                      OR e.url LIKE '#/tabs/integral/integralInfo%' )) i2 INNER JOIN " +
        "         (SELECT DISTINCT e.distinct_id AS memberId " +
        "          FROM songshu_shence_events e " +
        "          WHERE e.event = 'RegisterEvent' " +
        "                AND e.times BETWEEN  ?1 AND  ?2) register " +
        "             ON i2.distinct_id = register.memberId) b " +
        "        ON a.distinct_id = b.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT i3.distinct_id " +
        "     FROM (SELECT DISTINCT e.distinct_id " +
        "           FROM songshu_shence_events e " +
        "           WHERE e.event = 'AddCardEvent' " +
        "                 AND e.times BETWEEN  ?1 AND  ?2 AND " +
        "           e.isFromProductInfo = TRUE ) i3 INNER JOIN " +
        "         (SELECT DISTINCT e.distinct_id AS memberId " +
        "          FROM songshu_shence_events e " +
        "          WHERE e.event = 'RegisterEvent' " +
        "                AND e.times BETWEEN  ?1 AND  ?2) register " +
        "             ON i3.distinct_id = register.memberId) c " +
        "        ON b.distinct_id = c.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT i4.distinct_id " +
        "     FROM (SELECT DISTINCT e.distinct_id " +
        "           FROM songshu_shence_events e " +
        "           WHERE e.event = 'CreateOrderEvent' " +
        "                 AND e.times BETWEEN  ?1 AND  ?2 ) i4 INNER JOIN " +
        "         (SELECT DISTINCT e.distinct_id AS memberId " +
        "          FROM songshu_shence_events e " +
        "          WHERE e.event = 'RegisterEvent' " +
        "                AND e.times BETWEEN  ?1 AND  ?2) register " +
        "             ON i4.distinct_id = register.memberId) d " +
        "        ON c.distinct_id = d.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT i5.distinct_id " +
        "     FROM(SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = 'OrderPaymentEvent' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 )i5 INNER JOIN " +
        "         (SELECT DISTINCT e.distinct_id AS memberId " +
        "          FROM songshu_shence_events e " +
        "          WHERE e.event = 'RegisterEvent' " +
        "                AND e.times BETWEEN  ?1 AND  ?2) register " +
        "             ON i5.distinct_id = register.memberId) e " +
        "        ON d.distinct_id = e.distinct_id ", nativeQuery = true)
    List<Integer[]> getMemberFunnelWithAllPlatformForRegister(Timestamp beginTime, Timestamp endTime);
    /**
     * 会员漏斗（注册会员）(单平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT " +
        "    count(a.distinct_id) AS acount, " +
        "    count(b.distinct_id) AS bcount, " +
        "    count(c.distinct_id) AS ccount, " +
        "    count(d.distinct_id) AS dcount, " +
        "    count(e.distinct_id) AS ecount " +
        "FROM " +
        "    (SELECT DISTINCT i.distinct_id " +
        "     FROM (SELECT DISTINCT e.distinct_id " +
        "           FROM songshu_shence_events e " +
        "           WHERE e.event = 'AppLaunch' " +
        "                 AND e.times BETWEEN  ?1 AND  ?2 AND " +
        "                 (e.os =  ?3 OR e.platform =  ?4) " +
        "          ) i INNER JOIN (SELECT DISTINCT e.distinct_id AS memberId " +
        "                          FROM songshu_shence_events e " +
        "                          WHERE e.event = 'RegisterEvent' AND e.platform =  ?4 " +
        "                                AND e.times BETWEEN  ?1 AND  ?2) register " +
        "             ON i.distinct_id = register.memberId) a " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT i2.distinct_id " +
        "     FROM (SELECT DISTINCT e.distinct_id " +
        "           FROM songshu_shence_events e " +
        "           WHERE e.event = '$pageview' " +
        "                 AND e.times BETWEEN  ?1 AND  ?2 " +
        "                 AND (e.url LIKE '#/tabs/categories/productInfo%' " +
        "                      OR e.url LIKE '#/tabs/cart/productInfo%' " +
        "                      OR e.url LIKE '#/tabs/index/productInfo%' " +
        "                      OR e.url LIKE '#/tabs/user/productInfo%' " +
        "                      OR e.url LIKE '#/tabs/integral/integralInfo%' AND e.platform =  ?4)) i2 INNER JOIN " +
        "         (SELECT DISTINCT e.distinct_id AS memberId " +
        "          FROM songshu_shence_events e " +
        "          WHERE e.event = 'RegisterEvent' AND e.platform =  ?4 " +
        "                AND e.times BETWEEN  ?1 AND  ?2) register " +
        "             ON i2.distinct_id = register.memberId) b " +
        "        ON a.distinct_id = b.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT i3.distinct_id " +
        "     FROM (SELECT DISTINCT e.distinct_id " +
        "           FROM songshu_shence_events e " +
        "           WHERE e.event = 'AddCardEvent' " +
        "                 AND e.times BETWEEN  ?1 AND  ?2 AND e.platform =  ?4 AND " +
        "           e.isFromProductInfo = TRUE ) i3 INNER JOIN " +
        "         (SELECT DISTINCT e.distinct_id AS memberId " +
        "          FROM songshu_shence_events e " +
        "          WHERE e.event = 'RegisterEvent' AND e.platform =  ?4 " +
        "                AND e.times BETWEEN  ?1 AND  ?2) register " +
        "             ON i3.distinct_id = register.memberId) c " +
        "        ON b.distinct_id = c.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT i4.distinct_id " +
        "     FROM (SELECT DISTINCT e.distinct_id " +
        "           FROM songshu_shence_events e " +
        "           WHERE e.event = 'CreateOrderEvent' " +
        "                 AND e.times BETWEEN  ?1 AND  ?2 AND e.platform =  ?4) i4 INNER JOIN " +
        "         (SELECT DISTINCT e.distinct_id AS memberId " +
        "          FROM songshu_shence_events e " +
        "          WHERE e.event = 'RegisterEvent' AND e.platform =  ?4 " +
        "                AND e.times BETWEEN  ?1 AND  ?2) register " +
        "             ON i4.distinct_id = register.memberId) d " +
        "        ON c.distinct_id = d.distinct_id " +
        "    LEFT JOIN " +
        " " +
        "    (SELECT DISTINCT i5.distinct_id " +
        "     FROM(SELECT DISTINCT e.distinct_id " +
        "     FROM songshu_shence_events e " +
        "     WHERE e.event = 'OrderPaymentEvent' " +
        "           AND e.times BETWEEN  ?1 AND  ?2 AND e.platform =  ?4)i5 INNER JOIN " +
        "         (SELECT DISTINCT e.distinct_id AS memberId " +
        "          FROM songshu_shence_events e " +
        "          WHERE e.event = 'RegisterEvent' AND e.platform =  ?4 " +
        "                AND e.times BETWEEN  ?1 AND  ?2) register " +
        "             ON i5.distinct_id = register.memberId) e " +
        "        ON d.distinct_id = e.distinct_id", nativeQuery = true)
    List<Integer[]> getMemberFunnelWithSinglePlatformForRegister(Timestamp beginTime, Timestamp endTime, String os, String plateFormName);
}
