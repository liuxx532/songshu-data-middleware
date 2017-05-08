package com.comall.songshu.repository.channel;

import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * 渠道销售额
 * Created by huanghaizhou on 2017/5/8.
 */
public interface ChannelRevenueRepository {

    // 所有平台（5 个）
    @Query(value = "SELECT now()", nativeQuery = true)
    Double getChannelRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime,String channelName);


    // 单个平台
    @Query(value = "SELECT now()", nativeQuery = true)
    Double getChannelRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime,String channelName);
}
