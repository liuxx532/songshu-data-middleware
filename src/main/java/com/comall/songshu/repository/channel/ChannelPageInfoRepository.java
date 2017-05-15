package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 渠道分页信息
 *
 * @author liushengling
 * @create 2017-05-09-09:54
 **/
public interface ChannelPageInfoRepository extends JpaRepository<Author,Long> {


    /**
     * 渠道下载量及注册用户数（全app平台）
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT upper(install.utm_source) AS advSource ,COALESCE(install.memberCount,0) AS installCount,COALESCE(reg.memberCount,0) AS regCount FROM
//        (SELECT COUNT(DISTINCT base.distinctId) AS memberCount , base.utm_source FROM
//        (SELECT
//             e.distinct_id AS distinctId,
//         CASE
//             WHEN e.os = 'Android' AND e.utm_source IS NOT NULL  THEN e.utm_source
//             WHEN e.os = 'Android' AND e.utm_source IS NULL THEN 'yingyongbao'
//             WHEN e.os = 'iOS'      THEN 'ios'
//             WHEN e.os = 'weixin'   THEN 'weixin'
//             WHEN e.os = 'wap'      THEN 'wap'
//             ELSE 'yingyongbao'
//             END  AS utm_source,e.os
//             FROM songshu_shence_events e WHERE e.event ='AppInstall' AND e.times BETWEEN '2016-01-01 00:00:00' AND '2017-02-01 00:00:00'
//             AND  e.os IN ('iOS','Android'))base
//    GROUP BY base.utm_source)install
//    LEFT JOIN
//        (SELECT COUNT(DISTINCT base.memberId) AS memberCount , base.utm_source FROM
//        (SELECT mem."id" AS memberId,
//         CASE
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'yingyongbao'
//             WHEN mem."multipleChannelsId" = 2 THEN 'ios'
//             WHEN mem."multipleChannelsId" = 3 THEN 'weixin'
//             WHEN mem."multipleChannelsId" = 5 THEN 'wap'
//             ELSE 'wap'
//             END  AS utm_source
//             from songshu_cs_member mem
//             LEFT JOIN songshu_shence_users u  ON u.second_id = mem."id"
//             where  mem."regTime" BETWEEN '2016-01-01 00:00:00' AND '2017-02-01 00:00:00' and mem."multipleChannelsId" IN (1,2))base
//    GROUP BY base.utm_source)reg
//    ON reg.utm_source = install.utm_source
//    ORDER BY install.memberCount DESC,reg.memberCount DESC;
    @Query(value = "SELECT upper(install.utm_source) AS advSource ,COALESCE(install.memberCount,0) AS installCount,COALESCE(reg.memberCount,0) AS regCount FROM " +
        "    (SELECT COUNT( DISTINCT base.distinctId) AS memberCount , base.utm_source FROM " +
        "        (SELECT " +
        "             e.distinct_id AS distinctId, " +
        "             CASE " +
        "             WHEN e.os = 'Android' AND e.utm_source IS NOT NULL  THEN e.utm_source " +
        "             WHEN e.os = 'Android' AND e.utm_source IS NULL THEN 'yingyongbao' " +
        "             WHEN e.os = 'iOS'      THEN 'appstore' " +
        "             WHEN e.os = 'weixin'   THEN 'weixin' " +
        "             WHEN e.os = 'wap'      THEN 'wap' " +
        "             ELSE 'yingyongbao' " +
        "             END  AS utm_source,e.os " +
        "         FROM songshu_shence_events e WHERE e.event ='AppInstall' AND e.times BETWEEN ?1 AND ?2 " +
        "         AND  e.os IN ('iOS','Android'))base " +
        "    GROUP BY base.utm_source)install " +
        "    LEFT JOIN " +
        "    (SELECT COUNT(DISTINCT base.memberId) AS memberCount , base.utm_source FROM " +
        "        (SELECT mem.\"id\" AS memberId, " +
        "                CASE " +
        "                WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source " +
        "                WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'yingyongbao' " +
        "                WHEN mem.\"multipleChannelsId\" = 2 THEN 'appstore' " +
        "                WHEN mem.\"multipleChannelsId\" = 3 THEN 'weixin' " +
        "                WHEN mem.\"multipleChannelsId\" = 5 THEN 'wap' " +
        "                ELSE 'wap' " +
        "                END  AS utm_source " +
        "         FROM songshu_cs_member mem " +
        "             LEFT JOIN songshu_shence_users u  ON u.second_id = mem.\"id\" " +
        "         WHERE  mem.\"regTime\" BETWEEN ?1 AND ?2 and mem.\"multipleChannelsId\" IN (1,2))base " +
        "    GROUP BY base.utm_source)reg " +
        "    ON reg.utm_source = install.utm_source " +
        "ORDER BY install.memberCount DESC,reg.memberCount DESC", nativeQuery = true)
    List<Object[]> getChannelPageInfoWithAllAppPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 渠道下载量及注册用户数（单app平台）
     * @param beginTime
     * @param endTime
     * @param os
     * @param platform
     * @return
     */
//    SELECT upper(install.utm_source) AS advSource ,COALESCE(install.memberCount,0) AS installCount,COALESCE(reg.memberCount,0) AS regCount FROM
//        (SELECT COUNT(DISTINCT base.distinctId) AS memberCount , base.utm_source FROM
//        (SELECT
//             e.distinct_id AS distinctId,
//         CASE
//             WHEN e.os = 'Android' AND e.utm_source IS NOT NULL  THEN e.utm_source
//             WHEN e.os = 'Android' AND e.utm_source IS NULL THEN 'yingyongbao'
//             WHEN e.os = 'iOS'      THEN 'appstore'
//             WHEN e.os = 'weixin'   THEN 'weixin'
//             WHEN e.os = 'wap'      THEN 'wap'
//             ELSE 'yingyongbao'
//             END  AS utm_source,e.os
//             FROM songshu_shence_events e WHERE e.event ='AppInstall' AND e.times BETWEEN '2016-01-01 00:00:00' AND '2017-02-01 00:00:00'
//             AND e.os = 'iOS')base
//    GROUP BY base.utm_source)install
//    LEFT JOIN
//        (SELECT COUNT(DISTINCT base.memberId) AS memberCount , base.utm_source FROM
//        (SELECT mem."id" AS memberId,
//         CASE
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'yingyongbao'
//             WHEN mem."multipleChannelsId" = 2 THEN 'appstore'
//             WHEN mem."multipleChannelsId" = 3 THEN 'weixin'
//             WHEN mem."multipleChannelsId" = 5 THEN 'wap'
//             ELSE 'wap'
//             END  AS utm_source
//             from songshu_cs_member mem
//             LEFT JOIN songshu_shence_users u  ON u.second_id = mem."id"
//             where  mem."regTime" BETWEEN '2016-01-01 00:00:00' AND '2017-02-01 00:00:00' and mem."multipleChannelsId" = 2)base
//    GROUP BY base.utm_source)reg
//    ON reg.utm_source = install.utm_source
//    ORDER BY install.memberCount DESC,reg.memberCount DESC;
    @Query(value = "SELECT upper(install.utm_source) AS advSource ,COALESCE(install.memberCount,0) AS installCount,COALESCE(reg.memberCount,0) AS regCount FROM " +
        "    (SELECT COUNT(DISTINCT base.distinctId) AS memberCount , base.utm_source FROM " +
        "        (SELECT " +
        "             e.distinct_id AS distinctId, " +
        "             CASE " +
        "             WHEN e.os = 'Android' AND e.utm_source IS NOT NULL  THEN e.utm_source " +
        "             WHEN e.os = 'Android' AND e.utm_source IS NULL THEN 'yingyongbao' " +
        "             WHEN e.os = 'iOS'      THEN 'appstore' " +
        "             WHEN e.os = 'weixin'   THEN 'weixin' " +
        "             WHEN e.os = 'wap'      THEN 'wap' " +
        "             ELSE 'yingyongbao' " +
        "             END  AS utm_source,e.os " +
        "         FROM songshu_shence_events e WHERE e.event ='AppInstall' AND e.times BETWEEN  ?1 AND ?2  " +
        "         AND e.os = ?3)base " +
        "    GROUP BY base.utm_source)install " +
        "    LEFT JOIN " +
        "    (SELECT COUNT(DISTINCT base.memberId) AS memberCount , base.utm_source FROM " +
        "        (SELECT mem.\"id\" AS memberId, " +
        "                CASE " +
        "                WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source " +
        "                WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'yingyongbao' " +
        "                WHEN mem.\"multipleChannelsId\" = 2 THEN 'appstore' " +
        "                WHEN mem.\"multipleChannelsId\" = 3 THEN 'weixin' " +
        "                WHEN mem.\"multipleChannelsId\" = 5 THEN 'wap' " +
        "                ELSE 'wap' " +
        "                END  AS utm_source " +
        "         FROM songshu_cs_member mem " +
        "             LEFT JOIN songshu_shence_users u  ON u.second_id = mem.\"id\" " +
        "         WHERE  mem.\"regTime\" BETWEEN ?1 AND ?2 AND mem.\"multipleChannelsId\" = ?4)base " +
        "    GROUP BY base.utm_source)reg " +
        "    ON reg.utm_source = install.utm_source " +
        "ORDER BY install.memberCount DESC,reg.memberCount DESC", nativeQuery = true)
    List<Object[]> getChannelPageInfoWithSingleAppPlatform(Timestamp beginTime, Timestamp endTime,String os, Integer platform);


    /**
     * 渠道注册用户数（非app平台（全））
     * @param beginTime
     * @param endTime
     * @return
     */
//    SELECT upper(reg.utm_source) AS advSource ,0 AS installCount,COALESCE(reg.memberCount,0) AS regCount FROM
//        (SELECT COUNT(DISTINCT base.memberId) AS memberCount , base.utm_source FROM
//        (SELECT mem."id" AS memberId,
//         CASE
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'yingyongbao'
//             WHEN mem."multipleChannelsId" = 2 THEN 'appstore'
//             WHEN mem."multipleChannelsId" = 3 THEN 'weixin'
//             WHEN mem."multipleChannelsId" = 5 THEN 'wap'
//             ELSE 'wap'
//             END  AS utm_source
//             from songshu_cs_member mem
//             LEFT JOIN songshu_shence_users u  ON u.second_id = mem."id"
//             where  mem."regTime" BETWEEN '2016-01-01 00:00:00' AND '2017-02-01 00:00:00' and mem."multipleChannelsId" NOT IN (1,2))base
//    GROUP BY base.utm_source)reg ORDER BY reg.memberCount DESC
    @Query(value = "SELECT upper(reg.utm_source) AS advSource ,0 AS installCount,COALESCE(reg.memberCount,0) AS regCount FROM " +
        "    (SELECT COUNT(DISTINCT base.memberId) AS memberCount , base.utm_source FROM " +
        "        (SELECT mem.\"id\" AS memberId, " +
        "                CASE " +
        "                WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source " +
        "                WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'yingyongbao' " +
        "                WHEN mem.\"multipleChannelsId\" = 2 THEN 'appstore' " +
        "                WHEN mem.\"multipleChannelsId\" = 3 THEN 'weixin' " +
        "                WHEN mem.\"multipleChannelsId\" = 5 THEN 'wap' " +
        "                ELSE 'wap' " +
        "                END  AS utm_source " +
        "         from songshu_cs_member mem " +
        "             LEFT JOIN songshu_shence_users u  ON u.second_id = mem.\"id\" " +
        "         where  mem.\"regTime\" BETWEEN ?1 AND ?2 and mem.\"multipleChannelsId\" NOT IN (1,2))base " +
        "    GROUP BY base.utm_source)reg ORDER BY reg.memberCount DESC", nativeQuery = true)
    List<Object[]> getChannelRegisterInfoWithAllWebPlatform(Timestamp beginTime, Timestamp endTime);


    /**
     * 渠道注册用户数（非app平台（单））
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
//    SELECT upper(reg.utm_source) AS advSource ,0 AS installCount,COALESCE(reg.memberCount,0) AS regCount FROM
//        (SELECT COUNT( DISTINCT base.memberId) AS memberCount , base.utm_source FROM
//        (SELECT mem."id" AS memberId,
//         CASE
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'yingyongbao'
//             WHEN mem."multipleChannelsId" = 2 THEN 'appstore'
//             WHEN mem."multipleChannelsId" = 3 THEN 'weixin'
//             WHEN mem."multipleChannelsId" = 5 THEN 'wap'
//             ELSE 'wap'
//             END  AS utm_source
//             from songshu_cs_member mem
//             LEFT JOIN songshu_shence_users u  ON u.second_id = mem."id"
//             where  mem."regTime" BETWEEN '2016-01-01 00:00:00' AND '2017-02-01 00:00:00' and mem."multipleChannelsId" = 1)base
//    GROUP BY base.utm_source)reg ORDER BY reg.memberCount DESC
    @Query(value = "SELECT upper(reg.utm_source) AS advSource ,0 AS installCount,COALESCE(reg.memberCount,0) AS regCount FROM " +
        "    (SELECT COUNT(DISTINCT base.memberId) AS memberCount , base.utm_source FROM " +
        "        (SELECT mem.\"id\" AS memberId, " +
        "                CASE " +
        "                WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source " +
        "                WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'yingyongbao' " +
        "                WHEN mem.\"multipleChannelsId\" = 2 THEN 'appstore' " +
        "                WHEN mem.\"multipleChannelsId\" = 3 THEN 'weixin' " +
        "                WHEN mem.\"multipleChannelsId\" = 5 THEN 'wap' " +
        "                ELSE 'wap' " +
        "                END  AS utm_source " +
        "         from songshu_cs_member mem " +
        "             LEFT JOIN songshu_shence_users u  ON u.second_id = mem.\"id\" " +
        "         where  mem.\"regTime\" BETWEEN ?1 AND ?2 and mem.\"multipleChannelsId\" = ?3)base " +
        "    GROUP BY base.utm_source)reg ORDER BY reg.memberCount DESC", nativeQuery = true)
    List<Object[]> getChannelRegisterInfoWithSingleWebPlatform(Timestamp beginTime, Timestamp endTime,Integer platform);
}
