package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * 渠道页面访客数
 * Created by lgx on 17/4/25.
 */
public interface ChannelUniqueVisitorsRepository extends JpaRepository<Author,Long> {

    //不和渠道关联

    //SQL
    // SELECT count(DISTINCT se.distinct_id) FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.times BETWEEN '2017-01-01 00:00:00' AND '2017-03-01 00:00:00'
    // AND se.platform='android';

    @Query(value = "SELECT COALESCE(count(DISTINCT se.distinct_id),0) FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2 ;", nativeQuery = true)
    Double getChannelUniqueVisitorsAllPlatformAllChannel(Timestamp beginTime, Timestamp endTime);


    @Query(value = "SELECT COALESCE(count(DISTINCT se.distinct_id),0) FROM songshu_shence_events se WHERE se.event = '$pageview' AND se.times BETWEEN ?2 AND ?3 AND se.platform = ?1 ;", nativeQuery = true)
    Double getChannelUniqueVisitorsSinglePlatformAllChannel(String platform, Timestamp beginTime, Timestamp endTime);

    //和渠道关联

    //SQL
    //SELECT count(distinct tcom.distinct_id) FROM(SELECT se.distinct_id AS distinct_id, CASE
    // WHEN se.platform = 'android' AND se.utm_source IS NOT NULL THEN upper(se.utm_source) WHEN se.platform = 'android'
    // AND se.utm_source IS NULL THEN 'YINGYONGBAO' WHEN se.platform = 'ios' THEN 'APPLESTORE' WHEN se.platform = 'weixin'
    // THEN 'WEIXIN' WHEN se.platform = 'wap' THEN 'WAP' ELSE 'WAP' END AS utm_source, se.platform AS platform FROM songshu_shence_events se
    // WHERE se.event = '$pageview' AND se.times BETWEEN '2017-01-01 00:00:00' AND '2017-03-01 00:00:00') AS tcom WHERE tcom.utm_source='YINGYONGBAO';

    @Query(value = "SELECT COALESCE(count(distinct tcom.distinct_id),0) " +
        "FROM(SELECT se.distinct_id AS distinct_id, CASE " +
        "WHEN se.platform = 'android' AND se.utm_source IS NOT NULL THEN upper(se.utm_source) " +
        "WHEN se.platform = 'android' AND se.utm_source IS NULL THEN 'YINGYONGBAO' " +
        "WHEN se.platform = 'ios' THEN 'APPLESTORE' WHEN se.platform = 'weixin' THEN 'WEIXIN' " +
        "WHEN se.platform = 'wap' THEN 'WAP' ELSE 'WAP' END AS utm_source, se.platform AS platform FROM songshu_shence_events se " +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2 ) AS tcom WHERE tcom.utm_source = ?3 ;", nativeQuery = true)
    Double getChannelUniqueVisitorsAllPlatformSingleChannel(Timestamp beginTime, Timestamp endTime,String channelName);

    @Query(value = "SELECT COALESCE(count(distinct tcom.distinct_id),0) " +
        "FROM(SELECT se.distinct_id AS distinct_id, CASE " +
        "WHEN se.platform = 'android' AND se.utm_source IS NOT NULL THEN upper(se.utm_source) " +
        "WHEN se.platform = 'android' AND se.utm_source IS NULL THEN 'YINGYONGBAO' " +
        "WHEN se.platform = 'ios' THEN 'APPLESTORE' WHEN se.platform = 'weixin' THEN 'WEIXIN' " +
        "WHEN se.platform = 'wap' THEN 'WAP' ELSE 'WAP' END AS utm_source, se.platform AS platform FROM songshu_shence_events se " +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?2 AND ?3 AND se.platform = ?1 ) AS tcom WHERE tcom.utm_source = ?4 ;", nativeQuery = true)
    Double getChannelUniqueVisitorsSinglePlatformSingleChannel(String platform,Timestamp beginTime, Timestamp endTime,String channelName);
}
