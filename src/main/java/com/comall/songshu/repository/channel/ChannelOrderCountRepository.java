package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * Created by liugaoyu on 2017/4/20.
 * 渠道订单量
 */
public interface ChannelOrderCountRepository extends JpaRepository<Author,Long> {


    // 所有平台
    @Query(value = "SELECT  NOW()", nativeQuery = true)
    Double getChannelOrderCountWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT  NOW()", nativeQuery = true)
    Double getChannelOrderCountWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);


}
