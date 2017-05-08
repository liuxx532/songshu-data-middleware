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
    /**
     * 获取用户分享
     * @param name 分享事件的名称
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberShareWithAllPlatformByName(String name,Timestamp beginTime, Timestamp endTime);

    /**
     * 获取用户分享
     * @param name 分享事件的名称
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    Integer getMemberShareWithSinglePlatformByName(String name,Timestamp beginTime, Timestamp endTime,Integer platform);

    /**
     * 获取用户分享趋势
     * @param name
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    List<Object[]> getMemberShareTrendWithAllPlatformByName(String name, Timestamp beginTime, Timestamp endTime, Integer interval);

    /**
     * 获取用户分享趋势
     * @param name
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT now()", nativeQuery = true)
    List<Object[]> getMemberShareTrendWithSinglePlatformByName(String name, Timestamp beginTime, Timestamp endTime, Integer platform, Integer interval);
}
