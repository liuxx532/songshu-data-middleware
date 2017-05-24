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


    /**
     * 毛利率（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END),0) AS goodsGrossMargin FROM " +
        "(SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice  FROM " +
        "        (SELECT coi.\"Quantity\",CASE WHEN COALESCE(coi.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END AS cost,coi.\"AfterFoldingPrice\" " +
        "         FROM  songshu_cs_order co " +
        "         RIGHT JOIN ( SELECT DISTINCT oo.\"Id\" FROM songshu_cs_order oo " +
        "                     INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "                     INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record " +
        "                                 WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                                 GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "                     WHERE cop.\"PaymentStatus\" = 1 AND oo.\"OrderStatus\" NOT IN (6,7) AND oo.\"orderType\" IN(0, 1) " +
        "                     AND cpr.paidTime BETWEEN ?1 AND ?2  " +
        "                    ) coo ON co.\"Id\" = coo.\"Id\" " +
        "         INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\" " +
        "         INNER JOIN songshu_cs_goods g ON  g.\"Id\" = coi.\"GoodsId\" " +
        "        )base " +
        ")grossmargin", nativeQuery = true)
    Double getCrossMarginWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 毛利率（单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END),0) AS goodsGrossMargin FROM " +
        "(SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice  FROM " +
        "        (SELECT coi.\"Quantity\",CASE WHEN COALESCE(coi.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END AS cost,coi.\"AfterFoldingPrice\" " +
        "         FROM  songshu_cs_order co " +
        "         RIGHT JOIN ( SELECT DISTINCT oo.\"Id\" FROM songshu_cs_order oo " +
        "                     INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "                     INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record " +
        "                                 WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                                 GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "                     WHERE cop.\"PaymentStatus\" = 1 AND oo.\"OrderStatus\" NOT IN (6,7) AND oo.\"orderType\" IN(0, 1) " +
        "                     AND cpr.paidTime BETWEEN ?1 AND ?2 AND oo.\"Channel\" = ?3 " +
        "                    ) coo ON co.\"Id\" = coo.\"Id\" " +
        "         INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\" " +
        "         INNER JOIN songshu_cs_goods g ON  g.\"Id\" = coi.\"GoodsId\" " +
        "        )base " +
        ")grossmargin", nativeQuery = true)
    Double getCrossMarginWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);

    /**
     * 毛利率趋势（全平台）
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END)),0) AS goodsGrossMargin FROM " +
        "( SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice, base.stime, base.etime FROM " +
        "    (SELECT  coi.\"Quantity\",coi.\"AfterFoldingPrice\",CASE WHEN coi.\"ReferCost\" = 0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END  AS cost,tss.stime,tss.etime " +
        "     FROM (SELECT DISTINCT oo.\"Id\", cpr.paidTime " +
        "                FROM songshu_cs_order oo " +
        "                INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "                INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record " +
        "                            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "                WHERE cop.\"PaymentStatus\" = 1 AND oo.\"OrderStatus\" NOT IN (6,7) AND oo.\"orderType\" IN(0, 1) " +
        "                AND cpr.paidTime BETWEEN ?1 AND ?2  " +
        "            ) coo " +
        "     INNER JOIN songshu_cs_order_item coi ON coo.\"Id\" = coi.\"OrderId\" " +
        "     INNER JOIN songshu_cs_goods g ON g.\"Id\" = coi.\"GoodsId\" " +
        "     RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "                FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "                ON (coo.paidTime < tss.etime AND coo.paidTime >= tss.stime) " +
        "    )base GROUP BY base.stime, base.etime ORDER BY base.stime " +
        ")grossmargin", nativeQuery = true)
    List<Object[]> getCrossMarginTrendWithAllPlatform(Timestamp beginTime, Timestamp endTime, Integer interval);

    /**
     * 毛利率趋势（单平台）
     * @param beginTime
     * @param endTime
     * @param interval
     * @param platform
     * @return
     */
    @Query(value = "SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END)),0) AS goodsGrossMargin FROM " +
        "( SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice, base.stime, base.etime FROM " +
        "    (SELECT  coi.\"Quantity\",coi.\"AfterFoldingPrice\",CASE WHEN coi.\"ReferCost\" = 0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END  AS cost,tss.stime,tss.etime " +
        "     FROM (SELECT DISTINCT oo.\"Id\", cpr.paidTime " +
        "                FROM songshu_cs_order oo " +
        "                INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "                INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record " +
        "                            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "                WHERE cop.\"PaymentStatus\" = 1 AND oo.\"OrderStatus\" NOT IN (6,7) AND oo.\"orderType\" IN(0, 1) " +
        "                AND cpr.paidTime BETWEEN ?1 AND ?2 AND oo.\"Channel\" = ?4 " +
        "            ) coo " +
        "     INNER JOIN songshu_cs_order_item coi ON coo.\"Id\" = coi.\"OrderId\" " +
        "     INNER JOIN songshu_cs_goods g ON g.\"Id\" = coi.\"GoodsId\" " +
        "     RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "                FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss " +
        "                ON (coo.paidTime < tss.etime AND coo.paidTime >= tss.stime) " +
        "    )base GROUP BY base.stime, base.etime ORDER BY base.stime " +
        ")grossmargin", nativeQuery = true)
    List<Object[]> getCrossMarginTrendWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer interval,Integer platform);


}
