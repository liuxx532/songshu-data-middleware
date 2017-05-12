package com.comall.songshu.repository.channel;

import com.comall.songshu.constants.CommonConstants;
import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by huanghaizhou on 2017/5/8.
 */
public interface VisitDeepDistributionRepository extends JpaRepository<Author,Long> {

    //SQL
    //SELECT tcomm.tdeep,count(tcomm.did)
    // FROM(SELECT CASE WHEN tcom.tcount = 1 THEN '1' WHEN tcom.tcount >= 2 AND tcom.tcount <= 5 THEN '2-5' WHEN tcom.tcount >= 6 AND tcom.tcount <= 10 THEN '6-10' WHEN tcom.tcount >= 11 AND tcom.tcount <= 30 THEN '11-30' WHEN tcom.tcount >= 31 AND tcom.tcount <= 40 THEN '31-40' WHEN tcom.tcount >= 41 THEN '>41' END AS tdeep, tcom.distinct_id AS did FROM (SELECT count(DISTINCT se.url) AS tcount, distinct_id FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.times BETWEEN '2017-04-28 00:00:00' AND '2017-04-30 00:00:00' GROUP BY se.distinct_id) tcom) tcomm where tcomm.tdeep IS NOT NULL GROUP BY tcomm.tdeep

    @Query(value = "SELECT tcomm.tdeep,count(tcomm.did) " +
        "FROM(SELECT CASE WHEN tcom.tcount = 1 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_ONE+"' " +
        "WHEN tcom.tcount >= 2 AND tcom.tcount <= 5 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_TWO+"' " +
        "WHEN tcom.tcount >= 6 AND tcom.tcount <= 10 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_THREE+"' " +
        "WHEN tcom.tcount >= 11 AND tcom.tcount <= 30 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_FOUR+"' " +
        "WHEN tcom.tcount >= 31 AND tcom.tcount <= 40 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_FIVE+"' " +
        "WHEN tcom.tcount >= 41 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_SIX+"' END AS tdeep, tcom.distinct_id AS did " +
        "FROM (SELECT count(DISTINCT se.url) AS tcount, distinct_id " +
        "FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.times " +
        "BETWEEN ?1 AND ?2 GROUP BY se.distinct_id) tcom) tcomm " +
        "where tcomm.tdeep IS NOT NULL GROUP BY tcomm.tdeep", nativeQuery = true)
    List<Object[]> getVisitDeepWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "SELECT tcomm.tdeep,count(tcomm.did) " +
        "FROM(SELECT CASE WHEN tcom.tcount = 1 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_ONE+"' " +
        "WHEN tcom.tcount >= 2 AND tcom.tcount <= 5 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_TWO+"' " +
        "WHEN tcom.tcount >= 6 AND tcom.tcount <= 10 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_THREE+"' " +
        "WHEN tcom.tcount >= 11 AND tcom.tcount <= 30 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_FOUR+"' " +
        "WHEN tcom.tcount >= 31 AND tcom.tcount <= 40 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_FIVE+"' " +
        "WHEN tcom.tcount >= 41 THEN '"+ CommonConstants.VISIT_DEEP_LEVEL_SIX+"' END AS tdeep, tcom.distinct_id AS did " +
        "FROM (SELECT count(DISTINCT se.url) AS tcount, distinct_id " +
        "FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.platform = ?3 AND se.times " +
        "BETWEEN ?1 AND ?2 GROUP BY se.distinct_id) tcom) tcomm " +
        "where tcomm.tdeep IS NOT NULL GROUP BY tcomm.tdeep", nativeQuery = true)
    List<Object[]> getVisitDeepSinglePlatform(Timestamp beginTime, Timestamp endTime,String platformName);
}
