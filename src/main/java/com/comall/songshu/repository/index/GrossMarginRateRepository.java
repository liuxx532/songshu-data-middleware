package com.comall.songshu.repository.index;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by liugaoyu on 2017/4/20.
 */
// 毛利率 = (销售额 - 商品成本） / 销售额 * 100%
public interface GrossMarginRateRepository extends JpaRepository<Author,Long> {


//SQL:

//    SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ grossmargin.AfterFoldingPrice,0) AS goodsGrossMargin FROM
//        ( SELECT SUM(base."Quantity" * base.cost)AS referCost, SUM(base."AfterFoldingPrice")AS AfterFoldingPrice  FROM
//        (SELECT coi."Quantity",CASE WHEN COALESCE(coi."ReferCost",0) =0 THEN g."CostPrice" ELSE coi."ReferCost" END AS cost,coi."AfterFoldingPrice" FROM
//            songshu_cs_order co
//            RIGHT JOIN ( SELECT DISTINCT oo."Id" FROM songshu_cs_order oo
//            INNER JOIN songshu_cs_order_payable cop ON oo."Id" = cop."OrderId"
//            INNER JOIN songshu_cs_payment_record cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
//            WHERE cop."PaymentStatus" = 1 AND cpr."PaymentModeType" = 2 AND cpr."PaidTime"
//            BETWEEN ?1 AND ?2 AND oo."OrderStatus" NOT IN (6,7) AND oo."orderType" IN(0, 1)
//    AND oo."Channel" IN(0, 1, 2, 3, 5) ) coo ON co."Id" = coo."Id"
//    INNER JOIN songshu_cs_order_item coi ON co."Id" = coi."OrderId"
//    INNER JOIN songshu_cs_goods g ON  g."Id" = coi."GoodsId"
//        )base
//)grossmargin;

    @Query(value = "SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ grossmargin.AfterFoldingPrice,0) AS goodsGrossMargin FROM  " +
        "( SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice  FROM   " +
        "(SELECT coi.\"Quantity\",CASE WHEN COALESCE(coi.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END AS cost,coi.\"AfterFoldingPrice\" FROM  " +
        "songshu_cs_order co  " +
        "RIGHT JOIN ( SELECT DISTINCT oo.\"Id\" FROM songshu_cs_order oo  " +
        "    INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\"   " +
        "    INNER JOIN songshu_cs_payment_record cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\"  " +
        "    WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.\"PaidTime\"  " +
        "    BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN (6,7) AND oo.\"orderType\" IN(0, 1)  " +
        "    AND oo.\"Channel\" IN(0, 1, 2, 3, 5) ) coo ON co.\"Id\" = coo.\"Id\"  " +
        "INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\" " +
        "INNER JOIN songshu_cs_goods g ON  g.\"Id\" = coi.\"GoodsId\" " +
        ")base  " +
        ")grossmargin", nativeQuery = true)
    Double getCrossMarginWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ grossmargin.AfterFoldingPrice,0) AS goodsGrossMargin FROM  " +
        "( SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice  FROM   " +
        "(SELECT coi.\"Quantity\",CASE WHEN COALESCE(coi.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END AS cost,coi.\"AfterFoldingPrice\" FROM  " +
        "songshu_cs_order co  " +
        "RIGHT JOIN ( SELECT DISTINCT oo.\"Id\" FROM songshu_cs_order oo  " +
        "    INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\"   " +
        "    INNER JOIN songshu_cs_payment_record cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\"  " +
        "    WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.\"PaidTime\"  " +
        "    BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN (6,7) AND oo.\"orderType\" IN(0, 1)  " +
        "    AND oo.\"Channel\" =?3 ) coo ON co.\"Id\" = coo.\"Id\"  " +
        "INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\" " +
        "INNER JOIN songshu_cs_goods g ON  g.\"Id\" = coi.\"GoodsId\" " +
        ")base  " +
        ")grossmargin", nativeQuery = true)
    Double getCrossMarginWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);

//SQL:

//    SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (grossmargin.AfterFoldingPrice)),0) AS goodsGrossMargin FROM
//        ( SELECT SUM(base."Quantity" * base.cost)AS referCost, SUM(base."AfterFoldingPrice")AS AfterFoldingPrice, base.stime, base.etime FROM
//        (SELECT  coi."Quantity",coi."AfterFoldingPrice",CASE WHEN coi."ReferCost" = 0 THEN g."CostPrice" ELSE coi."ReferCost" END  AS cost,tss.stime,tss.etime
//            FROM songshu_cs_order co
//             RIGHT JOIN (SELECT  oo."Id", MAX(cpr."PaidTime") AS MPaidTime
//    FROM songshu_cs_order oo
//    INNER JOIN songshu_cs_order_payable cop ON oo."Id" = cop."OrderId"
//    INNER JOIN songshu_cs_payment_record cpr ON cpr."MergePaymentNo" = cop."MergePaymentId"
//    WHERE cop."PaymentStatus" = 1 AND cpr."PaymentModeType" = 2 AND cpr."PaidTime"
//    BETWEEN '2016-01-01 00:00:00' AND '2016-01-09 00:00:00' AND oo."OrderStatus" NOT IN (6, 7) AND oo."orderType" IN (0, 1)
//    AND  oo."Channel" IN (0, 1, 2, 3, 5)  GROUP BY oo."Id") coo ON co."Id" = coo."Id"
//    INNER JOIN songshu_cs_order_item coi ON co."Id" = coi."OrderId"
//    INNER JOIN songshu_cs_goods g ON g."Id" = coi."GoodsId"
//    RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + 86400 * INTERVAL '1 second' AS etime
//        FROM (SELECT generate_series('2016-01-01 00:00:00', '2016-01-09 00:00:00', 86400 * INTERVAL '1 second')) ts) tss
//    ON (coo.MPaidTime < tss.etime AND coo.MPaidTime >= tss.stime)
//    )base GROUP BY base.stime, base.etime ORDER BY base.stime
//)grossmargin;
    @Query(value = "SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (grossmargin.AfterFoldingPrice)),0) AS goodsGrossMargin FROM " +
        "( SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice, base.stime, base.etime FROM " +
        "    (SELECT  coi.\"Quantity\",coi.\"AfterFoldingPrice\",CASE WHEN coi.\"ReferCost\" = 0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END  AS cost,tss.stime,tss.etime " +
        "     FROM songshu_cs_order co " +
        "     RIGHT JOIN (SELECT  oo.\"Id\", MAX(cpr.\"PaidTime\") AS MPaidTime " +
        "                FROM songshu_cs_order oo " +
        "                INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "                INNER JOIN songshu_cs_payment_record cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "                WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.\"PaidTime\" " +
        "                BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN (6, 7) AND oo.\"orderType\" IN (0, 1) " +
        "                AND  oo.\"Channel\" IN (0, 1, 2, 3, 5)  GROUP BY oo.\"Id\") coo ON co.\"Id\" = coo.\"Id\" " +
        "     INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\" " +
        "     INNER JOIN songshu_cs_goods g ON g.\"Id\" = coi.\"GoodsId\" " +
        "     RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "                FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "                ON (coo.MPaidTime < tss.etime AND coo.MPaidTime >= tss.stime) " +
        "    )base GROUP BY base.stime, base.etime ORDER BY base.stime " +
        ")grossmargin", nativeQuery = true)
    List<Object[]> getCrossMarginTrendWithAllPlatform(Timestamp beginTime, Timestamp endTime, Integer interval);

    @Query(value = "SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (grossmargin.AfterFoldingPrice)),0) AS goodsGrossMargin FROM " +
        "( SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice, base.stime, base.etime FROM " +
        "    (SELECT  coi.\"Quantity\",coi.\"AfterFoldingPrice\",CASE WHEN coi.\"ReferCost\" = 0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END  AS cost,tss.stime,tss.etime " +
        "     FROM songshu_cs_order co " +
        "     RIGHT JOIN (SELECT  oo.\"Id\", MAX(cpr.\"PaidTime\") AS MPaidTime " +
        "                FROM songshu_cs_order oo " +
        "                INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "                INNER JOIN songshu_cs_payment_record cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "                WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.\"PaidTime\" " +
        "                BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN (6, 7) AND oo.\"orderType\" IN (0, 1) " +
        "                AND  oo.\"Channel\" =?4  GROUP BY oo.\"Id\") coo ON co.\"Id\" = coo.\"Id\" " +
        "     INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\" " +
        "     INNER JOIN songshu_cs_goods g ON g.\"Id\" = coi.\"GoodsId\" " +
        "     RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "                FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "                ON (coo.MPaidTime < tss.etime AND coo.MPaidTime >= tss.stime) " +
        "    )base GROUP BY base.stime, base.etime ORDER BY base.stime " +
        ")grossmargin;", nativeQuery = true)
    List<Object[]> getCrossMarginTrendWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer interval,Integer platform);

    // TODO fix slow sql query

}
