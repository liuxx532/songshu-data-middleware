package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 机型分布排行
 * Created by huanghaizhou on 2017/5/8.
 */
public interface ManufacturerRankRepository  extends JpaRepository<Author,Long> {

    @Query(value = "", nativeQuery = true)
    List<Object[]> getManufacturerRankWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "", nativeQuery = true)
    List<Object[]> getManufacturerRankWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);
}
