package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 地区排行
 * Created by huanghaizhou on 2017/5/8.
 */
public interface RegionRankRepository  extends JpaRepository<Author,Long> {

    @Query(value = "", nativeQuery = true)
    List<Object[]> getRegionRankWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "", nativeQuery = true)
    List<Object[]> getRegionRankWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);
}
