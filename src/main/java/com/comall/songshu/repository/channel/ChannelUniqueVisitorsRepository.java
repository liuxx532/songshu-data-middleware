package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * 渠道页面访客数
 * Created by lgx on 17/4/25.
 */
public interface ChannelUniqueVisitorsRepository extends JpaRepository<Author,Long> {


    @Query(value = "SELECT  NOW()", nativeQuery = true)
    Double getChannelUniqueVisitorsAllPlatform(Timestamp beginTime, Timestamp endTime);


    @Query(value = "SELECT  NOW()", nativeQuery = true)
    Double getChannelUniqueVisitorsSinglePlatform(String platform, Timestamp beginTime, Timestamp endTime);



}
