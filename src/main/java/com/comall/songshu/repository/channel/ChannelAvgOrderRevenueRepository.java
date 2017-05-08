package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * Created by wdc on 2017/4/19.
 */

// 渠道客单价
public interface ChannelAvgOrderRevenueRepository extends JpaRepository<Author, Long> {




    // 所有平台
    @Query(value = "SELECT NOW()", nativeQuery = true)
    Double getChannelAvgRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT NOW()", nativeQuery = true)
    Double getChannelAvgRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);


}
