package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户年龄及性别分布
 *
 * @author liushengling
 * @create 2017-05-08-11:53
 **/
public interface AgeAndSexDistributionRepository extends JpaRepository<Author,Long> {

    /**
     * 性别分布(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT CASE WHEN sexInfo.gender =1  THEN '男' " +
        "       WHEN sexInfo.gender =0  THEN '女' " +
        "       END AS sexGroup, sexInfo.memberCount AS memberCount " +
        "FROM (SELECT info.\"gender\" AS gender, count(DISTINCT(m.id)) AS memberCount " +
        "      FROM  songshu_cs_member m " +
        "      INNER JOIN songshu_cs_member_info info ON info.\"memberId\" = m.id " +
        "      WHERE m.\"regTime\" BETWEEN ?1 AND ?2 AND info.gender IN (0,1) " +
        "      GROUP BY info.\"gender\" ORDER BY info.\"gender\" DESC " +
        ") sexInfo;", nativeQuery = true)
    List<Object[]> getSexDistributionWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 性别分布(单平台)
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT CASE WHEN sexInfo.gender =1  THEN '男' " +
        "       WHEN sexInfo.gender =0  THEN '女' " +
        "       END AS sexGroup, sexInfo.memberCount AS memberCount " +
        "FROM (SELECT info.\"gender\" AS gender, count(DISTINCT(m.id)) AS memberCount " +
        "      FROM  songshu_cs_member m " +
        "      INNER JOIN songshu_cs_member_info info ON info.\"memberId\" = m.id " +
        "      WHERE m.\"regTime\" BETWEEN ?1 AND ?2 AND m.\"multipleChannelsId\" = ?3 AND info.gender IN (0,1) " +
        "      GROUP BY info.\"gender\" ORDER BY info.\"gender\" DESC " +
        ") sexInfo;", nativeQuery = true)
    List<Object[]> getSexDistributionSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);


    /**
     * 用户年龄分布(全平台)
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT ageGroupInfo.ageGroup AS ageGroup , COALESCE(SUM(ageGroupInfo.memberCount),0) AS memberCount FROM " +
        "(SELECT " +
        "     CASE " +
        "     WHEN birthInfo.age<=20 OR birthInfo.age IS NULL  THEN '0-20岁' " +
        "     WHEN birthInfo.age>=21 AND birthInfo.age<=25 THEN '21-25岁' " +
        "     WHEN birthInfo.age>=26 AND birthInfo.age<=30 THEN '26-30岁' " +
        "     WHEN birthInfo.age>=31 AND birthInfo.age<=35 THEN '31-35岁' " +
        "     WHEN birthInfo.age>=36 THEN '36岁+' " +
        "     END AS ageGroup, " +
        "     birthInfo.memberCount AS memberCount " +
        "     FROM  (SELECT (EXTRACT(YEAR FROM NOW())-EXTRACT(YEAR FROM info.\"birthday\")-1) + " +
        "              CASE " +
        "              WHEN ( DATE_PART('month',info.\"birthday\") < DATE_PART('month',NOW())) THEN 0 " +
        "              WHEN ( DATE_PART('month',info.\"birthday\") = DATE_PART('month',NOW())) AND (DATE_PART('day',info.\"birthday\") < DATE_PART('day',NOW())) THEN 0 " +
        "              ELSE 1 " +
        "              END AS age " +
        "              ,COUNT(DISTINCT(m.id)) as memberCount " +
        "              FROM  songshu_cs_member m " +
        "              INNER JOIN songshu_cs_member_info info ON info.\"memberId\" = m.id " +
        "              WHERE m.\"regTime\"  BETWEEN ?1 AND ?2  AND info.birthday IS NOT NULL " +
        "              GROUP BY age ORDER BY age ASC " +
        "            ) birthInfo " +
        ")ageGroupInfo " +
        "GROUP BY ageGroupInfo.ageGroup ORDER BY ageGroupInfo.ageGroup ASC", nativeQuery = true)
    List<Object[]> getAgeDistributionWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 用户年龄分布(单平台)
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT ageGroupInfo.ageGroup AS ageGroup , COALESCE(SUM(ageGroupInfo.memberCount),0) AS memberCount FROM " +
        "(SELECT " +
        "     CASE " +
        "     WHEN birthInfo.age<=20 OR birthInfo.age IS NULL  THEN '0-20岁' " +
        "     WHEN birthInfo.age>=21 AND birthInfo.age<=25 THEN '21-25岁' " +
        "     WHEN birthInfo.age>=26 AND birthInfo.age<=30 THEN '26-30岁' " +
        "     WHEN birthInfo.age>=31 AND birthInfo.age<=35 THEN '31-35岁' " +
        "     WHEN birthInfo.age>=36 THEN '36岁+' " +
        "     END AS ageGroup, " +
        "     birthInfo.memberCount AS memberCount " +
        "     FROM  (SELECT (EXTRACT(YEAR FROM NOW())-EXTRACT(YEAR FROM info.\"birthday\")-1) + " +
        "              CASE " +
        "              WHEN ( DATE_PART('month',info.\"birthday\") < DATE_PART('month',NOW())) THEN 0 " +
        "              WHEN ( DATE_PART('month',info.\"birthday\") = DATE_PART('month',NOW())) AND (DATE_PART('day',info.\"birthday\") < DATE_PART('day',NOW())) THEN 0 " +
        "              ELSE 1 " +
        "              END AS age " +
        "              ,COUNT(DISTINCT(m.id)) as memberCount " +
        "              FROM  songshu_cs_member m " +
        "              INNER JOIN songshu_cs_member_info info ON info.\"memberId\" = m.id " +
        "              WHERE m.\"regTime\"  BETWEEN ?1 AND ?2 AND m.\"multipleChannelsId\" = ?3  AND info.birthday IS NOT NULL " +
        "              GROUP BY age ORDER BY age ASC " +
        "            ) birthInfo " +
        ")ageGroupInfo " +
        "GROUP BY ageGroupInfo.ageGroup ORDER BY ageGroupInfo.ageGroup ASC", nativeQuery = true)
    List<Object[]> getAgeDistributionSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);
}
