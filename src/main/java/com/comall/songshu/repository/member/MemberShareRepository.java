package com.comall.songshu.repository.member;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by huanghaizhou on 2017/5/4.
 */
public interface MemberShareRepository  extends JpaRepository<Author,Long> {

    //SQL
    //SELECT count(1) FROM songshu_shence_events se where se.event='$pageview' AND
    // se.times BETWEEN '2016-01-01 00:00:00' AND '2017-03-01 00:00:00' AND se.platform='ios';

    /**
     * 获取用户分享
     * @param name 分享事件的名称
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT count(1) FROM songshu_shence_events se WHERE se.event= ?1 " +
        "AND se.times BETWEEN ?2 AND ?3  ;\n ", nativeQuery = true)
    Integer getMemberShareWithAllPlatformByName(String name,Timestamp beginTime, Timestamp endTime);

    /**
     * 获取用户分享
     * @param name 分享事件的名称
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT count(1) FROM songshu_shence_events se WHERE se.event= ?1 " +
        "AND se.times BETWEEN ?2 AND ?3 AND se.platform= ?4 ;\n ", nativeQuery = true)
    Integer getMemberShareWithSinglePlatformByName(String name,Timestamp beginTime, Timestamp endTime,String platform);

    //SQL
    //SELECT tss.stime, tss.etime, count(comt.event)
    // FROM(SELECT se.* FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.times
    // BETWEEN '2016-12-01 00:00:00' AND '2017-01-11 00:00:00' AND se.platform = 'ios') comt
    // RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + 86400 * INTERVAL '1 second' AS etime
    // FROM (SELECT generate_series('2016-12-01 00:00:00', '2017-01-11 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
    // ON (comt.times < tss.etime AND comt.times >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime;


    /**
     * 获取用户分享趋势
     * @param name
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT tss.stime, tss.etime, count(comt.event) " +
        "FROM(SELECT se.* FROM songshu_shence_events se " +
        "WHERE se.event = ?1 AND se.times BETWEEN ?2 AND ?3 ) comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?4 * INTERVAL '1 second' AS etime " +
        "FROM (SELECT generate_series( ?2 , ?3 , ?4 * INTERVAL '1 second')) ts) tss " +
        "ON (comt.times < tss.etime AND comt.times >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime;", nativeQuery = true)
    List<Object[]> getMemberShareTrendWithAllPlatformByName(String name, Timestamp beginTime, Timestamp endTime, Integer interval);

    /**
     * 获取用户分享趋势
     * @param name
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT tss.stime, tss.etime, count(comt.event) " +
        "FROM(SELECT se.* FROM songshu_shence_events se " +
        "WHERE se.event = ?1 AND se.times BETWEEN ?2 AND ?3 AND se.platform = ?4 ) comt " +
        "RIGHT JOIN (SELECT ts.generate_series AS stime, ts.generate_series + ?5 * INTERVAL '1 second' AS etime " +
        "FROM (SELECT generate_series( ?2 , ?3 , ?5 * INTERVAL '1 second')) ts) tss " +
        "ON (comt.times < tss.etime AND comt.times >= tss.stime) GROUP BY tss.stime, tss.etime ORDER BY tss.stime;", nativeQuery = true)
    List<Object[]> getMemberShareTrendWithSinglePlatformByName(String name, Timestamp beginTime, Timestamp endTime, String platform, Integer interval);
}
