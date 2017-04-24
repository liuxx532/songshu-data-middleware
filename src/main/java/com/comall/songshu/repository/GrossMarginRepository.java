package com.comall.songshu.repository;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

/**
 * Created by liugaoyu on 2017/4/20.
 */
// 毛利率 = (销售额 - 商品成本） / 销售额 * 100%
public interface GrossMarginRepository extends JpaRepository<Author,Long> {

    // 获取商品成本

//    SELECT sum(g."CostPrice") as cp
//    FROM songshu_cs_order o JOIN songshu_cs_order_payable p ON o."Id" = p."OrderId"
//    JOIN songshu_cs_order_item i ON o."Id" = i."OrderId"
//    JOIN songshu_cs_goods g ON i."ProductId" = g."ProductId"
//    WHERE o."Channel" IN (0, 1, 2, 3, 5)
//    AND o."OrderStatus" NOT IN (7, 8)
//    AND p."PaymentStatus" = 1
//    AND o."OrderCreateTime" BETWEEN '2015-01-06 18:22:50' AND '2017-01-06 18:22:50'

// 获取商品成本

    @Query(value = "SELECT sum(g.\"CostPrice\") as cp\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "  JOIN songshu_cs_order_item i ON o.\"Id\" = i.\"OrderId\"\n" +
        "  JOIN songshu_cs_goods g ON i.\"ProductId\" = g.\"ProductId\"\n" +
        "WHERE o.\"Channel\" IN (0, 1, 2, 3, 5)\n" +
        "      AND o.\"OrderStatus\" NOT IN (7, 8)\n" +
        "      AND p.\"PaymentStatus\" = 1\n" +
        "      AND o.\"OrderCreateTime\" BETWEEN ?1 AND ?2", nativeQuery = true)
    Double getProductCostWithAllPlatform(Timestamp beginTime, Timestamp endTime);


    @Query(value = "SELECT sum(g.\"CostPrice\") as cp\n" +
        "FROM songshu_cs_order o JOIN songshu_cs_order_payable p ON o.\"Id\" = p.\"OrderId\"\n" +
        "  JOIN songshu_cs_order_item i ON o.\"Id\" = i.\"OrderId\"\n" +
        "  JOIN songshu_cs_goods g ON i.\"ProductId\" = g.\"ProductId\"\n" +
        "WHERE o.\"Channel\" = ?1\n" +
        "      AND o.\"OrderStatus\" NOT IN (7, 8)\n" +
        "      AND p.\"PaymentStatus\" = 1\n" +
        "      AND o.\"OrderCreateTime\" BETWEEN ?2 AND ?3", nativeQuery = true)
    Double getProductCostWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);

    // TODO add trend
    // TODO fix slow sql query

}
