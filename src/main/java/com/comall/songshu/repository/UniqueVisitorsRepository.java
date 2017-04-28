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

//    @Query(value = "SELECT count(distinct case when log ->> 'userid' <> '' and log ->> 'userid' <> '-' then  log ->> 'userid' end ) + \n" +
//        "count(distinct case when log ->> 'userid' = '' or log ->> 'userid'='-' then log ->> 'unique' end) FROM songshu_log " +
//        "where to_timestamp(log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?1 and ?2", nativeQuery = true)

    // unique is key word in SQL

    @Query(value = "SELECT count(distinct case when userid <> '' and userid <> '-' then  userid end ) + \n" +
    "count(distinct case when userid = '' or userid = '-' then ss.\"unique\" end) FROM songshu_log ss\n" +
    "where logTime BETWEEN ?1 and ?2", nativeQuery = true)
    Double getUniqueVisitorsAllPlatform(Timestamp beginTime, Timestamp endTime);

//    @Query(value = "SELECT count(distinct case when log ->> 'userid' <> '' and log ->> 'userid' <> '-' then  log ->> 'userid' end ) + \n" +
//        "count(distinct case when log ->> 'userid' = '' or log ->> 'userid'='-' then log ->> 'unique' end) FROM songshu_log " +
//        "where log ->> 'os' = ?1 and to_timestamp(log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?2 and ?3", nativeQuery = true)

    @Query(value = "SELECT count(distinct case when userid <> '' and userid <> '-' then userid end) + \n" +
    "count(distinct case when userid = '' or userid = '-' then ss.\"unique\" end) FROM songshu_log ss\n" +
    "where os = ?1 AND logTime BETWEEN ?2 and ?3", nativeQuery = true)
    Double getUniqueVisitorsSinglePlatform(String platform, Timestamp beginTime, Timestamp endTime);


    // 趋势
//SELECT tss.stime, tss.etime, count(DISTINCT CASE WHEN comt.log ->> 'userid' <> '' AND comt.log ->> 'userid' <> '-' THEN comt.log ->> 'userid' END),
// count(DISTINCT CASE WHEN comt.log ->> 'userid' = '' OR comt.log ->> 'userid' = '-' THEN comt.log ->> 'unique' END) AS uv
// FROM(select * from songshu_log ss WHERE to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss')
// BETWEEN '2017-04-23 00:00:00' AND '2017-04-30 00:00:00') comt
// RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + 86400 * INTERVAL '1 second' AS etime
// FROM (SELECT generate_series('2017-04-23 00:00:00', '2017-04-30 00:00:00', 86400 * INTERVAL '1 second')) ts) tss ON (to_timestamp(comt.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') < tss.etime AND to_timestamp(comt.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime

    // 单个平台
//    @Query(value = "SELECT tss.stime, tss.etime, count(DISTINCT CASE WHEN comt.log ->> 'userid' <> '' AND comt.log ->> 'userid' <> '-' THEN comt.log ->> 'userid' END), " +
//        "count(DISTINCT CASE WHEN comt.log ->> 'userid' = '' OR comt.log ->> 'userid' = '-' THEN comt.log ->> 'unique' END) AS uv " +
//        "FROM(SELECT * FROM songshu_log ss WHERE ss.log ->> 'os' = ?1 AND to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?2 AND ?3) comt " +
//        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?4 * INTERVAL '1 second' AS etime " +
//        "FROM (SELECT generate_series(?2, ?3, ?4 * INTERVAL '1 second')) ts) tss " +
//        "ON (to_timestamp(comt.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') < tss.etime AND to_timestamp(comt.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') >= tss.stime) " +
//        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)

    @Query(value = "SELECT tss.stime, tss.etime, count(DISTINCT CASE WHEN comt.userid <> '' AND comt.userid <> '-' THEN comt.userid END) + count(DISTINCT CASE WHEN comt.userid = '' OR comt.userid = '-' THEN comt.unique END) AS uv\n" +
        "FROM (select * from songshu_log ss WHERE ss.os = ?1 AND ss.logTime BETWEEN ?2 AND ?3) comt\n" +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?4 * INTERVAL '1 second' AS etime\n" +
        "            FROM (SELECT generate_series(?2, ?3, ?4 * INTERVAL '1 second')) ts) tss\n" +
        "    ON (comt.logTime < tss.etime AND comt.logTime >= tss.stime)\n" +
        "GROUP BY tss.stime, tss.etime\n" +
        "ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getUniqueVisitorsTrendSinglePlatform(String platform, Timestamp beginTime, Timestamp endTime, Integer intervals);

    // 所有平台
//    @Query(value = "SELECT tss.stime, tss.etime, count(DISTINCT CASE WHEN comt.log ->> 'userid' <> '' AND comt.log ->> 'userid' <> '-' THEN comt.log ->> 'userid' END), " +
//        "count(DISTINCT CASE WHEN comt.log ->> 'userid' = '' OR comt.log ->> 'userid' = '-' THEN comt.log ->> 'unique' END) AS uv " +
//        "FROM(select * from songshu_log ss WHERE to_timestamp(ss.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?1 AND ?2) comt " +
//        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
//        "FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss " +
//        "ON (to_timestamp(comt.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') < tss.etime AND to_timestamp(comt.log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') >= tss.stime) " +
//        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)

    @Query(value = "SELECT tss.stime, tss.etime, count(DISTINCT CASE WHEN comt.userid <> '' AND comt.userid <> '-' THEN comt.userid END) + count(DISTINCT CASE WHEN comt.userid = '' OR comt.userid = '-' THEN comt.unique END) AS uv\n" +
        "FROM (select * from songshu_log ss WHERE ss.logTime BETWEEN ?1 AND ?2) comt\n" +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime\n" +
        "            FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss\n" +
        "    ON (comt.logTime < tss.etime AND comt.logTime >= tss.stime)\n" +
        "GROUP BY tss.stime, tss.etime\n" +
        "ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getUniqueVisitorsTrendAllPlatform(Timestamp beginTime, Timestamp endTime, Integer intervals);


}
