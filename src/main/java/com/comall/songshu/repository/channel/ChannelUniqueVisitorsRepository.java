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


    /**
     * 全渠道页面访客数（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT se.distinct_id) " +
        "FROM songshu_shence_events se " +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2 ", nativeQuery = true)
    Double getChannelUniqueVisitorsAllPlatformAllChannel(Timestamp beginTime, Timestamp endTime);

    /**
     * 全渠道页面访客数（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT se.distinct_id) " +
        "FROM songshu_shence_events se " +
        "WHERE se.event = '$pageview' AND se.times BETWEEN ?2 AND ?3 AND se.platform = ?1 ", nativeQuery = true)
    Double getChannelUniqueVisitorsSinglePlatformAllChannel(String platform, Timestamp beginTime, Timestamp endTime);

    /**
     * 单渠道页面访客数（全平台）
     * @param beginTime
     * @param endTime
     * @param channelName
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT tcom.distinct_id) " +
        "FROM(SELECT se.distinct_id AS distinct_id, " +
        "     CASE WHEN se.platform = 'android' AND se.utm_source IS NOT NULL THEN upper(se.utm_source) " +
        "     WHEN se.platform = 'android' AND se.utm_source IS NULL THEN 'YINGYONGBAO' " +
        "     WHEN se.platform = 'ios' THEN 'APPLESTORE' " +
        "     WHEN se.platform = 'weixin' THEN 'WEIXIN' " +
        "     WHEN se.platform = 'wap' THEN 'WAP' " +
        "     ELSE 'WAP' END AS utm_source, se.platform AS platform " +
        "     FROM songshu_shence_events se " +
        "     WHERE se.event = '$pageview' AND se.times BETWEEN ?1 AND ?2  " +
        "    ) AS tcom " +
        "WHERE tcom.utm_source = ?3 ", nativeQuery = true)
    Double getChannelUniqueVisitorsAllPlatformSingleChannel(Timestamp beginTime, Timestamp endTime,String channelName);

    /**
     * 单渠道页面访客数（单平台）
     * @param platform
     * @param beginTime
     * @param endTime
     * @param channelName
     * @return
     */
    @Query(value = "SELECT COUNT(DISTINCT tcom.distinct_id) " +
        "FROM(SELECT se.distinct_id AS distinct_id, " +
        "     CASE WHEN se.platform = 'android' AND se.utm_source IS NOT NULL THEN upper(se.utm_source) " +
        "     WHEN se.platform = 'android' AND se.utm_source IS NULL THEN 'YINGYONGBAO' " +
        "     WHEN se.platform = 'ios' THEN 'APPLESTORE' " +
        "     WHEN se.platform = 'weixin' THEN 'WEIXIN' " +
        "     WHEN se.platform = 'wap' THEN 'WAP' " +
        "     ELSE 'WAP' END AS utm_source, se.platform AS platform " +
        "     FROM songshu_shence_events se " +
        "     WHERE se.event = '$pageview' AND se.times BETWEEN ?2 AND ?3 AND se.platform = ?1 " +
        "    ) AS tcom " +
        "WHERE tcom.utm_source = ?4 ", nativeQuery = true)
    Double getChannelUniqueVisitorsSinglePlatformSingleChannel(String platform,Timestamp beginTime, Timestamp endTime,String channelName);
}
