package com.comall.songshu.repository;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * Created by lgx on 17/4/25.
 */
public interface UniqueVisitorsRepository extends JpaRepository<Author,Long> {

    @Query(value = "SELECT count(distinct case when log ->> 'userid' <> '' and log ->> 'userid' <> '-' then  log ->> 'userid' end ) + \n" +
        "count(distinct case when log ->> 'userid' = '' or log ->> 'userid'='-' then log ->> 'unique' end) FROM songshu_log " +
        "where to_timestamp(log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?1 and ?2", nativeQuery = true)
    Double getUniqueVisitorsAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "SELECT count(distinct case when log ->> 'userid' <> '' and log ->> 'userid' <> '-' then  log ->> 'userid' end ) + \n" +
        "count(distinct case when log ->> 'userid' = '' or log ->> 'userid'='-' then log ->> 'unique' end) FROM songshu_log " +
        "where log ->> 'os' = ?1 and to_timestamp(log ->> 'logTime', 'DD/Mon/YYYY:HH24:mi:ss') BETWEEN ?2 and ?3", nativeQuery = true)
    Double getUniqueVisitorsSinglePlatform(String platform, Timestamp beginTime, Timestamp endTime);

}
