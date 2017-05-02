package com.comall.songshu.repository.index;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * Created by wdc on 2017/4/24.
 */
public interface FirstOrderedConsumerCountRepository extends JpaRepository<Author, Long> {
// SQL
//    SELECT count(DISTINCT temp.id) AS tc
//    FROM
//        (SELECT o."MemberId" AS id, min(o."OrderCreateTime") AS mt
//    FROM songshu_cs_order o JOIN songshu_cs_order_payable p on o."Id" = p."OrderId"
//    WHERE p."PaymentStatus" = 1  AND o."Channel" IN (0, 1, 2, 3, 5) --
//    GROUP BY o."MemberId"
//    HAVING
//    min(o."OrderCreateTime") BETWEEN '2015-01-06 18:22:50' AND '2017-01-06 18:22:50') temp

    @Query(value = "SELECT count(DISTINCT temp.id) AS tc\n" +
        "FROM\n" +
        "  (SELECT o.\"MemberId\" AS id, min(o.\"OrderCreateTime\") AS mt\n" +
        "   FROM songshu_cs_order o JOIN songshu_cs_order_payable p on o.\"Id\" = p.\"OrderId\"\n" +
        "   WHERE p.\"PaymentStatus\" = 1  AND o.\"Channel\" IN (0, 1, 2, 3, 5)\n" +
        "   GROUP BY o.\"MemberId\"\n" +
        "   HAVING\n" +
        "     min(o.\"OrderCreateTime\") BETWEEN ?1 AND ?2) temp", nativeQuery = true)
    Double getFirstOrderedConsumerCountWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "SELECT count(DISTINCT temp.id) AS tc\n" +
        "FROM\n" +
        "  (SELECT o.\"MemberId\" AS id, min(o.\"OrderCreateTime\") AS mt\n" +
        "   FROM songshu_cs_order o JOIN songshu_cs_order_payable p on o.\"Id\" = p.\"OrderId\"\n" +
        "   WHERE p.\"PaymentStatus\" = 1  AND o.\"Channel\" = ?1\n" +
        "   GROUP BY o.\"MemberId\"\n" +
        "   HAVING\n" +
        "     min(o.\"OrderCreateTime\") BETWEEN ?2 AND ?3) temp", nativeQuery = true)
    Double getFirstOrderedConsumerCountWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);

}