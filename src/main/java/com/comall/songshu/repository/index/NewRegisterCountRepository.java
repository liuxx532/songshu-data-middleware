package com.comall.songshu.repository.index;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * Created by wdc on 2017/4/24.
 */
// 新注册用户数
public interface NewRegisterCountRepository extends JpaRepository<Author, Long> {
// SQL
//    SELECT count(id) as tc
//    FROM songshu_cs_member
//    WHERE "regTime" BETWEEN '2015-01-06 18:22:50' AND '2017-01-06 18:22:50' -- 时间
//    AND "multipleChannelsId" IN (1, 2, 3, 5)



    /**
     * 新注册用户数（渠道ID!=0的单渠道）
     * @param platform
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(id) AS tc FROM songshu_cs_member  " +
        "WHERE \"regTime\" BETWEEN ?2 AND ?3  " +
        "AND \"multipleChannelsId\" = ?1 ", nativeQuery = true)
    Integer getNewRegisterCountWithSinglePlatform(Integer platform, Timestamp startTime, Timestamp endTime);

    /**
     * 新注册用户数（渠道ID=0的渠道）
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(id) AS tc FROM songshu_cs_member  " +
        "WHERE \"regTime\" BETWEEN ?1 AND ?2  " +
        "AND \"multipleChannelsId\" NOT IN (1,2,3,5) ", nativeQuery = true)
    Integer getNewRegisterCountWithOthersPlatform(Timestamp startTime, Timestamp endTime);
}
