package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 地区排行
 * Created by huanghaizhou on 2017/5/8.
 */
public interface RegionRankRepository  extends JpaRepository<Author,Long> {

    /**
     * 地区排行（全平台）
     * @param beginTime
     * @param endTime
     * @param topCount
     * @return
     */
    @Query(value = "SELECT se.city, COUNT(DISTINCT se.distinct_id) AS tcount\n" +
        "FROM songshu_shence_events se\n" +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2\n" +
        "AND se.city IS NOT NULL AND se.city != '未知'\n" +
        "GROUP BY se.city ORDER BY tcount DESC LIMIT ?3", nativeQuery = true)
    List<Object[]> getRegionRankWithAllPlatform(Timestamp beginTime, Timestamp endTime,Integer topCount);

    /**
     * 地区排行（单平台）
     * @param beginTime
     * @param endTime
     * @param platformName
     * @param topCount
     * @return
     */
    @Query(value = "SELECT se.city, COUNT(DISTINCT se.distinct_id) AS tcount\n" +
        "FROM songshu_shence_events se\n" +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2\n" +
        "AND se.platform = ?3 AND se.city IS NOT NULL AND se.city != '未知'\n" +
        "GROUP BY se.city ORDER BY tcount DESC LIMIT ?4", nativeQuery = true)
    List<Object[]> getRegionRankWithSinglePlatform(Timestamp beginTime, Timestamp endTime,String platformName,Integer topCount);
}
