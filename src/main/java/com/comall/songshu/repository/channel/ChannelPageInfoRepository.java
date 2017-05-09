package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 渠道分页信息
 *
 * @author liushengling
 * @create 2017-05-09-09:54
 **/
public interface ChannelPageInfoRepository extends JpaRepository<Author,Long> {

    // 所有平台
    @Query(value = "SELECT  NOW()", nativeQuery = true)
    List<Object[]> getChannelDownLoadCountWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT  NOW()", nativeQuery = true)
    List<Object[]> getChannelDownLoadCountWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);


    // 所有平台
    @Query(value = "SELECT  NOW()", nativeQuery = true)
    Integer getChannelMemberRegisterCountWithAllPlatform(Timestamp beginTime, Timestamp endTime,String channelName);


    // 单个平台
    @Query(value = "SELECT  NOW()", nativeQuery = true)
    Integer getChannelMemberRegisterCountWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime,String channelName);
}
