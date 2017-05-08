package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 渠道页面消费用户数
 *
 * @author liushengling
 * @create 2017-05-08-11:53
 **/
public interface ChannelConsumerCountRepository extends JpaRepository<Author,Long> {


    @Query(value = "SELECT NOW()", nativeQuery = true)
    Double getChannelConsumerCountWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "SELECT NOW()", nativeQuery = true)
    Double getChannelConsumerCountSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer platform);



}
