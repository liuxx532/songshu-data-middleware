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

    /**
     * 注册用户数（单平台）
     * @param platform
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT count(id) as tc\n" +
        "FROM songshu_cs_member\n" +
        "WHERE \"regTime\" BETWEEN ?2 AND ?3\n" +
        "AND \"multipleChannelsId\" = ?1 ;", nativeQuery = true)
    Integer getNewRegisterCountWithSinglePlatform(Integer platform, Timestamp startTime, Timestamp endTime);

    /**
     * 注册用户数（其他平台）
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT count(id) as tc\n" +
        "FROM songshu_cs_member\n" +
        "WHERE \"regTime\" BETWEEN ?1 AND ?2\n" +
        "AND \"multipleChannelsId\" NOT IN (1,2,3,5) ;", nativeQuery = true)
    Integer getNewRegisterCountWithOthersPlatform(Timestamp startTime, Timestamp endTime);
}
