package com.comall.songshu.repository.index;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * Created by wdc on 2017/4/24.
 */
public interface NotFirstOrderedConsumerCountRepository extends JpaRepository<Author, Long> {

    /**
     * 非首单用户数（全平台）
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT count(DISTINCT o.\"MemberId\") as tc " +
        "FROM songshu_cs_order o LEFT JOIN songshu_cs_order_payable p on o.\"Id\" = p.\"OrderId\" " +
        "WHERE  p.\"PaymentStatus\" = 1  AND o.\"OrderCreateTime\" BETWEEN ?1 AND ?2 " +
        "AND o.\"MemberId\" IN ( " +
        "                    SELECT o.\"MemberId\" AS id FROM songshu_cs_order o " +
        "                    LEFT JOIN songshu_cs_order_payable p on o.\"Id\" = p.\"OrderId\" " +
        "                    WHERE p.\"PaymentStatus\" = 1 AND o.\"Channel\" =1   " +
        "                    GROUP BY o.\"MemberId\" " +
        "                    HAVING min(o.\"OrderCreateTime\") < ?1)", nativeQuery = true)
    Double getNotFirstOrderedConsumerCountWithAllPlatform(Timestamp startTime, Timestamp endTime);


    /**
     * 非首单用户数（单平台）
     * @param platform
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT count(DISTINCT o.\"MemberId\") as tc " +
        "FROM songshu_cs_order o LEFT JOIN songshu_cs_order_payable p on o.\"Id\" = p.\"OrderId\" " +
        "WHERE  p.\"PaymentStatus\" = 1 AND o.\"Channel\" = ?1   AND o.\"OrderCreateTime\" BETWEEN ?2 AND ?3 " +
        "AND o.\"MemberId\" IN ( " +
        "                    SELECT o.\"MemberId\" AS id FROM songshu_cs_order o " +
        "                    LEFT JOIN songshu_cs_order_payable p on o.\"Id\" = p.\"OrderId\" " +
        "                    WHERE p.\"PaymentStatus\" = 1 AND o.\"Channel\" =1   " +
        "                    GROUP BY o.\"MemberId\" " +
        "                    HAVING min(o.\"OrderCreateTime\") < ?2)", nativeQuery = true)
    Double getNotFirstOrderedConsumerCountWithSinglePlatform(Integer platform, Timestamp startTime, Timestamp endTime);
}
