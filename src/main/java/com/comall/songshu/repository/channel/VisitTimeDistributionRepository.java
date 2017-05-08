package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 访问时长分布Repository
 * Created by huanghaizhou on 2017/5/8.
 */
public interface VisitTimeDistributionRepository  extends JpaRepository<Author,Long> {

    @Query(value = "", nativeQuery = true)
    List<Object[]> getVisitTimeDistributionWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "", nativeQuery = true)
    List<Object[]> getVisitTimeDistributionSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);
}
