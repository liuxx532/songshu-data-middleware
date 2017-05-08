package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * 渠道销售额
 * Created by huanghaizhou on 2017/5/8.
 */
public interface ChannelRevenueRepository  extends JpaRepository<Author,Long> {

    // 所有平台（5 个）
    @Query(value = "SELECT now()", nativeQuery = true)
    Double getChannelRevenueWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    // 单个平台
    @Query(value = "SELECT now()", nativeQuery = true)
    Double getChannelRevenueWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);
}
