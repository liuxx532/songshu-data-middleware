package com.comall.songshu.repository.member;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户注册数据
 *
 * @author liushengling
 * @create 2017-05-04-16:39
 **/
public interface ChannelRegisterMemberRepository extends JpaRepository<Author,Long>{



//    SELECT COUNT(base.memberId) AS memberCount , base.utm_source FROM
//        (SELECT mem."id" AS memberId,
//         CASE
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'yingyongbao'
//             WHEN mem."multipleChannelsId" = 2 THEN 'ios'
//             WHEN mem."multipleChannelsId" = 3 THEN 'weixin'
//             WHEN mem."multipleChannelsId" = 5 THEN 'wap'
//             ELSE 'wap'
//             END  AS utm_source
//             FROM songshu_cs_member mem
//             LEFT JOIN songshu_shence_users u  ON u.second_id = mem."id"
//             WHERE  mem."regTime" BETWEEN '2016-01-01 00:00:00' AND '2016-02-01 00:00:00' )base
//    GROUP BY base.utm_source ORDER BY memberCount DESC LIMIT  10
    @Query(value = "    SELECT COUNT(base.memberId) AS memberCount , base.utm_source FROM " +
        "        (SELECT mem.\"id\" AS memberId, " +
        "         CASE " +
        "             WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source " +
        "             WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'yingyongbao' " +
        "             WHEN mem.\"multipleChannelsId\" = 2 THEN 'ios' " +
        "             WHEN mem.\"multipleChannelsId\" = 3 THEN 'weixin' " +
        "             WHEN mem.\"multipleChannelsId\" = 5 THEN 'wap' " +
        "             ELSE 'wap' " +
        "             END  AS utm_source " +
        "             FROM songshu_cs_member mem " +
        "             LEFT JOIN songshu_shence_users u  ON u.second_id = mem.\"id\" " +
        "             WHERE  mem.\"regTime\" BETWEEN ?1 AND ?2 )base " +
        "    GROUP BY base.utm_source ORDER BY memberCount DESC LIMIT  ?3", nativeQuery = true)
    List<Object[]> getChannelMemberRegisterCountAllPlatform(Timestamp beginTime, Timestamp endTime, Integer topCount);


//    SELECT COUNT(base.memberId) AS memberCount , base.utm_source FROM
//        (SELECT mem."id" AS memberId,
//         CASE
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source
//             WHEN mem."multipleChannelsId" = 1 AND u.utm_source IS NULL THEN 'yingyongbao'
//             WHEN mem."multipleChannelsId" = 2 THEN 'ios'
//             WHEN mem."multipleChannelsId" = 3 THEN 'weixin'
//             WHEN mem."multipleChannelsId" = 5 THEN 'wap'
//             ELSE 'wap'
//             END  AS utm_source
//             FROM songshu_cs_member mem
//             LEFT JOIN songshu_shence_users u  ON u.second_id = mem."id"
//             WHERE  mem."regTime" BETWEEN '2016-01-01 00:00:00' AND '2016-02-01 00:00:00' AND mem."multipleChannelsId" = 1)base
//    GROUP BY base.utm_source ORDER BY memberCount DESC LIMIT  10
    @Query(value = "SELECT COUNT(base.memberId) AS memberCount , base.utm_source FROM " +
        " (SELECT mem.\"id\" AS memberId, " +
        "        CASE " +
        "        WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN u.utm_source " +
        "        WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'yingyongbao' " +
        "        WHEN mem.\"multipleChannelsId\" = 2 THEN 'ios' " +
        "        WHEN mem.\"multipleChannelsId\" = 3 THEN 'weixin' " +
        "        WHEN mem.\"multipleChannelsId\" = 5 THEN 'wap' " +
        "        ELSE 'wap' " +
        "        END  AS utm_source " +
        "  FROM songshu_cs_member mem " +
        "     LEFT JOIN songshu_shence_users u  ON u.second_id = mem.\"id\" " +
        " WHERE  mem.\"regTime\" BETWEEN ?1 AND ?2 AND mem.\"multipleChannelsId\" = ?3)base " +
        "GROUP BY base.utm_source ORDER BY memberCount DESC LIMIT  ?4", nativeQuery = true)
    List<Object[]> getChannelMemberRegisterCountSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm, Integer topCount);
}
