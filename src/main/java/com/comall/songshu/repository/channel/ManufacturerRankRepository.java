package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 机型分布排行
 * Created by huanghaizhou on 2017/5/8.
 */
public interface ManufacturerRankRepository  extends JpaRepository<Author,Long> {

    /**
     * 机型分布排行(全平台)
     * @param beginTime
     * @param endTime
     * @param topCount
     * @return
     */
    @Query(value = "SELECT UPPER(se.manufacturer) AS tmanufacturer,COUNT(DISTINCT se.distinct_id) AS tcount " +
        "FROM songshu_shence_events se " +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2 " +
        "AND se.manufacturer IS NOT NULL AND  UPPER(se.manufacturer) NOT LIKE 'ITOOLSAVM%'" +
        "GROUP BY UPPER(se.manufacturer) ORDER BY tcount DESC LIMIT ?3", nativeQuery = true)
    List<Object[]> getManufacturerRankWithAllPlatform(Timestamp beginTime, Timestamp endTime,Integer topCount);

    /**
     * 机型分布排行（单平台）
     * @param beginTime
     * @param endTime
     * @param platformName
     * @param topCount
     * @return
     */
    @Query(value = "SELECT UPPER(se.manufacturer) AS tmanufacturer,COUNT(DISTINCT se.distinct_id) AS tcount " +
        "FROM songshu_shence_events se " +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2 " +
        "AND se.platform = ?3 AND se.manufacturer IS NOT NULL AND  UPPER(se.manufacturer) NOT LIKE 'ITOOLSAVM%'" +
        "GROUP BY UPPER(se.manufacturer) ORDER BY tcount DESC LIMIT ?4", nativeQuery = true)
    List<Object[]> getManufacturerRankWithSinglePlatform(Timestamp beginTime, Timestamp endTime,String platformName,Integer topCount);
}
