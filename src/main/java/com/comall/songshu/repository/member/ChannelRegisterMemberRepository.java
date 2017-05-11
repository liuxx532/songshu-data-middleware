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



//    SELECT u.utm_source,COUNT(u.second_id) as userCount FROM songshu_shence_users u
//    INNER JOIN songshu_cs_member mem ON u.second_id = mem."id"
//    WHERE  mem."regTime" BETWEEN '2017-01-01 00:00:00' AND '2017-12-01 00:00:00'
//    AND u.second_id is not null AND u.utm_source is NOT NULL
//    AND u.utm_source NOT IN('test','preproduction','production','newtest','channel_10')
//    GROUP BY u.utm_source  ORDER BY userCount DESC LIMIT 10
@Query(value = "SELECT u.utm_source,COUNT(u.second_id) as userCount FROM songshu_shence_users u\n" +
    "INNER JOIN songshu_cs_member mem ON u.second_id = mem.\"id\"\n" +
    "WHERE  mem.\"regTime\" BETWEEN ?1 AND ?2 \n" +
    "AND u.second_id is not null AND u.utm_source is NOT NULL\n" +
    "AND u.utm_source NOT IN('test','preproduction','production','newtest','channel_10')\n" +
    "GROUP BY u.utm_source  ORDER BY userCount DESC LIMIT ?3", nativeQuery = true)
    List<Object[]> getChannelMemberRegisterCountAllPlatform(Timestamp beginTime, Timestamp endTime, Integer topCount);


//    SELECT u.utm_source,COUNT(u.second_id) as userCount FROM songshu_shence_users u
//    INNER JOIN songshu_cs_member mem ON u.second_id = mem."id"
//    WHERE  mem."regTime" BETWEEN '2017-01-01 00:00:00' AND '2017-12-01 00:00:00'
//    AND u.second_id is not null AND u.utm_source is NOT NULL
//    AND u.utm_source NOT IN('test','preproduction','production','newtest','channel_10')
//    AND mem."multipleChannelsId" = 1   GROUP BY u.utm_source  ORDER BY userCount DESC LIMIT 10
    @Query(value = "SELECT u.utm_source,COUNT(u.second_id) as userCount FROM songshu_shence_users u\n" +
        "INNER JOIN songshu_cs_member mem ON u.second_id = mem.\"id\"\n" +
        "WHERE  mem.\"regTime\" BETWEEN ?1 AND ?2 \n" +
        "AND u.second_id is not null AND u.utm_source is NOT NULL\n" +
        "AND u.utm_source NOT IN('test','preproduction','production','newtest','channel_10')\n" +
        "AND mem.\"multipleChannelsId\" = ?3   GROUP BY u.utm_source  ORDER BY userCount DESC LIMIT ?4", nativeQuery = true)
    List<Object[]> getChannelMemberRegisterCountSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer plateForm, Integer topCount);
}
