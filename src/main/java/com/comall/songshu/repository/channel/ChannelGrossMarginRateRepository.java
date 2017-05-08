package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 渠道页面毛利率
 * Created by liugaoyu on 2017/4/20.
 */
// 毛利率 = (销售额 - 商品成本） / 销售额 * 100%
public interface ChannelGrossMarginRateRepository extends JpaRepository<Author,Long> {


    @Query(value = "SELECT  NOW()", nativeQuery = true)
    Double getChannelCrossMarginWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "SELECT  NOW()", nativeQuery = true)
    Double getChannelCrossMarginWithSinglePlatform(Timestamp beginTime, Timestamp endTime, Integer platform);


}
