package com.comall.songshu.repository;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by lgx on 17/4/25.
 */
public interface UniqueVisitorsRepository extends JpaRepository<Author,Long> {

    @Query(value = "SELECT count(distinct case when log ->> 'userid' <> '' and log ->> 'userid' <> '-' then  log ->> 'userid' end ) + \n" +
        "count(distinct case when log ->> 'userid' = '' or log ->> 'userid'='-' then log ->> 'unique' end) FROM songshu_log " +
        "where to_timestamp(log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?1 and ?2", nativeQuery = true)
    Double getUniqueVisitorsAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "SELECT count(distinct case when log ->> 'userid' <> '' and log ->> 'userid' <> '-' then  log ->> 'userid' end ) + \n" +
        "count(distinct case when log ->> 'userid' = '' or log ->> 'userid'='-' then log ->> 'unique' end) FROM songshu_log " +
        "where log ->> 'os' = ?1 and to_timestamp(log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?2 and ?3", nativeQuery = true)
    Double getUniqueVisitorsSinglePlatform(String platform, Timestamp beginTime, Timestamp endTime);


    // 趋势
//    SELECT tss.stime, tss.etime,
//    count(distinct case when ss.log ->> 'userid' <> '' and ss.log ->> 'userid' <> '-' then  ss.log ->> 'userid' end ) +
//    count(distinct case when ss.log ->> 'userid' = '' or ss.log ->> 'userid'='-' then ss.log ->> 'unique' end) as uv
//    FROM songshu_log ss
//    JOIN (SELECT ts.generate_series as stime, ts.generate_series + '12 hour' as etime
//            FROM (select generate_series('2017-01-23 09:22:41', '2017-09-23 09:22:41', interval '12 hour')) ts) tss
//    ON (to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') < tss.etime AND to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') >= tss.stime)
//    where ss.log ->> 'os' = 'weixin'
//    AND to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN '2017-01-23 09:22:41' and '2017-09-23 09:22:41'
//    GROUP BY tss.stime, tss.etime
//    ORDER BY tss.stime;

    // 单个平台
    @Query(value = "SELECT tss.stime, tss.etime,\n" +
        "count(distinct case when ss.log ->> 'userid' <> '' and ss.log ->> 'userid' <> '-' then  ss.log ->> 'userid' end ) + \n" +
        "count(distinct case when ss.log ->> 'userid' = '' or ss.log ->> 'userid'='-' then ss.log ->> 'unique' end) as uv\n" +
        "FROM songshu_log ss\n" +
        "JOIN (SELECT ts.generate_series as stime, ts.generate_series + ?4 * interval '1 second' as etime\n" +
        "FROM (select generate_series(?2, ?3, ?4 * interval '1 second')) ts) tss\n" +
        "ON (to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') < tss.etime AND to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') >= tss.stime)\n" +
        "where ss.log ->> 'os' = ?1\n" +
        "AND to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?2 and ?3\n" +
        "GROUP BY tss.stime, tss.etime\n" +
        "ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getUniqueVisitorsTrendSinglePlatform(String platform, Timestamp beginTime, Timestamp endTime, Integer intervals);

    // 所有平台
    @Query(value = "SELECT tss.stime, tss.etime,\n" +
        "count(distinct case when ss.log ->> 'userid' <> '' and ss.log ->> 'userid' <> '-' then  ss.log ->> 'userid' end ) + \n" +
        "count(distinct case when ss.log ->> 'userid' = '' or ss.log ->> 'userid'='-' then ss.log ->> 'unique' end) as uv\n" +
        "FROM songshu_log ss\n" +
        "JOIN (SELECT ts.generate_series as stime, ts.generate_series + ?3 * interval '1 second' as etime\n" +
        "FROM (select generate_series(?1, ?2, ?3 * interval '1 second')) ts) tss\n" +
        "ON (to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') < tss.etime AND to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') >= tss.stime)\n" +
        "WHERE to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?1 and ?2\n" +
        "GROUP BY tss.stime, tss.etime\n" +
        "ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getUniqueVisitorsTrendAllPlatform(Timestamp beginTime, Timestamp endTime, Integer intervals);


}
