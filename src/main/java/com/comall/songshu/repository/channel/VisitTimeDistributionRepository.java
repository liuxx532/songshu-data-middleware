package com.comall.songshu.repository.channel;

import com.comall.songshu.constants.CommonConstants;
import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 访问时长分布Repository
 * Created by huanghaizhou on 2017/5/8.
 */
public interface VisitTimeDistributionRepository  extends JpaRepository<Author,Long> {

    /**
     * 访问时长(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT tcomm.ttime,COUNT(tcomm.did) " +
        "FROM(SELECT CASE WHEN tcom.stime >= 1 AND tcom.stime <= 3 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_ONE+"' " +
        "            WHEN tcom.stime >= 4 AND tcom.stime <= 10 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_TWO+"' " +
        "            WHEN tcom.stime >= 11 AND tcom.stime <= 39 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_THREE+"' " +
        "            WHEN tcom.stime >= 31 AND tcom.stime <= 60 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_FOUR+"' " +
        "            WHEN tcom.stime > 60 AND tcom.stime <= 120 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_FIVE+"' " +
        "            WHEN tcom.stime > 120 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_SIX+"' END AS ttime, tcom.distinct_id AS did " +
        "     FROM (SELECT COALESCE(SUM(se.staytime),0)/1000 AS stime, distinct_id " +
        "           FROM songshu_shence_events se WHERE se.event = '$userStayTime'  " +
        "           AND se.times BETWEEN ?1 AND ?2 GROUP BY se.distinct_id " +
        "          ) tcom " +
        "    ) tcomm " +
        "WHERE tcomm.ttime IS NOT NULL GROUP BY tcomm.ttime;", nativeQuery = true)
    List<Object[]> getVisitTimeDistributionWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 访问时长(单平台)
     * @param beginTime
     * @param endTime
     * @param platformName
     * @return
     */
    @Query(value = "SELECT tcomm.ttime,COUNT(tcomm.did) " +
        "FROM(SELECT CASE WHEN tcom.stime >= 1 AND tcom.stime <= 3 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_ONE+"' " +
        "            WHEN tcom.stime >= 4 AND tcom.stime <= 10 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_TWO+"' " +
        "            WHEN tcom.stime >= 11 AND tcom.stime <= 39 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_THREE+"' " +
        "            WHEN tcom.stime >= 31 AND tcom.stime <= 60 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_FOUR+"' " +
        "            WHEN tcom.stime > 60 AND tcom.stime <= 120 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_FIVE+"' " +
        "            WHEN tcom.stime > 120 THEN '"+ CommonConstants.VISIT_TIME_LEVEL_SIX+"' END AS ttime, tcom.distinct_id AS did " +
        "     FROM (SELECT COALESCE(SUM(se.staytime),0)/1000 AS stime, distinct_id " +
        "           FROM songshu_shence_events se WHERE se.event = '$userStayTime' AND se.platform = ?3 " +
        "           AND se.times BETWEEN ?1 AND ?2 GROUP BY se.distinct_id " +
        "          ) tcom " +
        "    ) tcomm " +
        "WHERE tcomm.ttime IS NOT NULL GROUP BY tcomm.ttime;", nativeQuery = true)
    List<Object[]> getVisitTimeDistributionSinglePlatform(Timestamp beginTime, Timestamp endTime,String platformName);
}
