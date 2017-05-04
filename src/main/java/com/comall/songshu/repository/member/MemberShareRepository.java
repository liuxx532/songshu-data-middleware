package com.comall.songshu.repository.member;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

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
    @Query(value = "", nativeQuery = true)
    Integer getMemberShareWithAllPlatformByName(String name,Timestamp beginTime, Timestamp endTime);

    /**
     * 获取用户分享
     * @param name 分享事件的名称
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "", nativeQuery = true)
    Integer getMemberShareWithSinglePlatformByName(String name,Timestamp beginTime, Timestamp endTime,Integer platform);
}
