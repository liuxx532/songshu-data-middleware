package com.comall.songshu.repository.index;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by lgx on 17/4/25.
 * 访客数统计
 */
public interface UniqueVisitorsRepository extends JpaRepository<Author,Long> {

    /**
     * 访客数（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT se.distinct_id) FROM songshu_shence_events se " +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2", nativeQuery = true)
    Double getUniqueVisitorsAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 访客数（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT se.distinct_id) FROM songshu_shence_events se " +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?2 AND ?3 AND se.platform =?1", nativeQuery = true)
    Double getUniqueVisitorsSinglePlatform(String platform, Timestamp beginTime, Timestamp endTime);

    /**
     * 访客数趋势图（全平台）
     * @param beginTime
     * @param endTime
     * @param intervals
     * @return
     */
    @Query(value = "SELECT tss.stime, tss.etime, COUNT(DISTINCT comt.distinct_id) " +
        "FROM(SELECT se.times, se.distinct_id FROM songshu_shence_events se " +
        "    WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2  " +
        "    ) comt " +
        "RIGHT JOIN(SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "          FROM (SELECT generate_series(?1,?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "          ON (comt.times < tss.etime AND comt.times >= tss.stime) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getUniqueVisitorsTrendAllPlatform(Timestamp beginTime, Timestamp endTime, Integer intervals);

    /**
     * 访客数趋势图（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @param intervals
     * @return
     */
    @Query(value = "SELECT tss.stime, tss.etime, COUNT(DISTINCT comt.distinct_id) " +
        "FROM(SELECT se.times, se.distinct_id FROM songshu_shence_events se " +
        "    WHERE se.event = '$pageview' AND se.times BETWEEN ?2 AND ?3 AND se.platform = ?1  " +
        "    ) comt " +
        "RIGHT JOIN(SELECT ts.generate_series AS stime, ts.generate_series + ?4 * INTERVAL '1 second' AS etime " +
        "          FROM (SELECT generate_series(?2,?3, ?4 * INTERVAL '1 second')) ts) tss " +
        "          ON (comt.times < tss.etime AND comt.times >= tss.stime) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime", nativeQuery = true)
    List<Object[]> getUniqueVisitorsTrendSinglePlatform(String platform, Timestamp beginTime, Timestamp endTime, Integer intervals);

}
