package com.comall.songshu.repository.channel;

import com.comall.songshu.constants.CommonConstants;
import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by huanghaizhou on 2017/5/8.
 * 访问深度（用户页面访问数）
 */
public interface VisitDeepDistributionRepository extends JpaRepository<Author,Long> {

    /**
     * 访问深度(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT tcomm.tdeep,COUNT(tcomm.did) " +
        "FROM(SELECT CASE WHEN tcom.tcount = 1 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_ONE+"' " +
        "        WHEN tcom.tcount >= 2 AND tcom.tcount <= 5 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_TWO+"' " +
        "        WHEN tcom.tcount >= 6 AND tcom.tcount <= 10 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_THREE+"' " +
        "        WHEN tcom.tcount >= 11 AND tcom.tcount <= 30 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_FOUR+"' " +
        "        WHEN tcom.tcount >= 31 AND tcom.tcount <= 40 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_FIVE+"' " +
        "        WHEN tcom.tcount >= 41 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_SIX+"' END AS tdeep, tcom.distinct_id AS did " +
        "        FROM (SELECT COUNT(se.url) AS tcount, distinct_id " +
        "              FROM songshu_shence_events se WHERE se.event = '$pageview'  " +
        "              AND se.times BETWEEN ?1 AND ?2 GROUP BY se.distinct_id " +
        "             ) tcom " +
        ") tcomm " +
        "WHERE tcomm.tdeep IS NOT NULL GROUP BY tcomm.tdeep", nativeQuery = true)
    List<Object[]> getVisitDeepWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 访问深度(单平台)
     * @param beginTime
     * @param endTime
     * @param platformName
     * @return
     */
    @Query(value = "SELECT tcomm.tdeep,COUNT(tcomm.did) " +
        "FROM(SELECT CASE WHEN tcom.tcount = 1 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_ONE+"' " +
        "        WHEN tcom.tcount >= 2 AND tcom.tcount <= 5 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_TWO+"' " +
        "        WHEN tcom.tcount >= 6 AND tcom.tcount <= 10 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_THREE+"' " +
        "        WHEN tcom.tcount >= 11 AND tcom.tcount <= 30 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_FOUR+"' " +
        "        WHEN tcom.tcount >= 31 AND tcom.tcount <= 40 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_FIVE+"' " +
        "        WHEN tcom.tcount >= 41 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_SIX+"' END AS tdeep, tcom.distinct_id AS did " +
        "        FROM (SELECT COUNT(se.url) AS tcount, distinct_id " +
        "              FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.platform = ?3  " +
        "              AND se.times BETWEEN ?1 AND ?2 GROUP BY se.distinct_id " +
        "             ) tcom " +
        ") tcomm " +
        "WHERE tcomm.tdeep IS NOT NULL GROUP BY tcomm.tdeep", nativeQuery = true)
    List<Object[]> getVisitDeepSinglePlatform(Timestamp beginTime, Timestamp endTime,String platformName);
}
