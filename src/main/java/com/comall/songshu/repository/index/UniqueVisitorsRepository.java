package com.comall.songshu.repository.index;

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

    @Query(value = "SELECT COALESCE(count(DISTINCT se.distinct_id),0) FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2 ;", nativeQuery = true)
    Double getUniqueVisitorsAllPlatform(Timestamp beginTime, Timestamp endTime);

//    @Query(value = "SELECT count(distinct case when log ->> 'userid' <> '' and log ->> 'userid' <> '-' then  log ->> 'userid' end ) + \n" +
//        "count(distinct case when log ->> 'userid' = '' or log ->> 'userid'='-' then log ->> 'unique' end) FROM songshu_log " +
//        "where log ->> 'os' = ?1 and to_timestamp(log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?2 and ?3", nativeQuery = true)

    @Query(value = "SELECT COALESCE(count(DISTINCT se.distinct_id),0) FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.times BETWEEN ?2 AND ?3 AND se.platform = ?1 ;", nativeQuery = true)
    Double getUniqueVisitorsSinglePlatform(String platform, Timestamp beginTime, Timestamp endTime);


    // 趋势

    //SQL
    //SELECT tss.stime, tss.etime, COALESCE(count(DISTINCT comt.distinct_id), 0)
    // FROM(SELECT se.times, se.distinct_id FROM songshu_shence_events se
    // WHERE se.event = '$pageview' AND se.times BETWEEN '2017-01-01 00:00:00' AND '2017-03-01 00:00:00') comt
    // RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + 86400 * INTERVAL '1 second' AS etime
    // FROM (SELECT generate_series('2017-01-01 00:00:00', '2017-03-01 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
    // ON (comt.times < tss.etime AND comt.times >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime;
    // 单个平台

    @Query(value = "SELECT tss.stime, tss.etime, COALESCE(count(DISTINCT comt.distinct_id), 0) " +
        "FROM(SELECT se.times, se.distinct_id FROM songshu_shence_events se " +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?2 AND ?3 AND se.platform = ?1 ) comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?4 * INTERVAL '1 second' AS etime " +
        "FROM (SELECT generate_series( ?2 , ?3 , ?4 * INTERVAL '1 second')) ts) tss " +
        "ON (comt.times < tss.etime AND comt.times >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime;", nativeQuery = true)
    List<Object[]> getUniqueVisitorsTrendSinglePlatform(String platform, Timestamp beginTime, Timestamp endTime, Integer intervals);

    // 所有平台

    @Query(value = "SELECT tss.stime, tss.etime, COALESCE(count(DISTINCT comt.distinct_id), 0) " +
        "FROM(SELECT se.times, se.distinct_id FROM songshu_shence_events se " +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2 ) comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "FROM (SELECT generate_series( ?1 , ?2 , ?3 * INTERVAL '1 second')) ts) tss " +
        "ON (comt.times < tss.etime AND comt.times >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime;", nativeQuery = true)
    List<Object[]> getUniqueVisitorsTrendAllPlatform(Timestamp beginTime, Timestamp endTime, Integer intervals);


}
