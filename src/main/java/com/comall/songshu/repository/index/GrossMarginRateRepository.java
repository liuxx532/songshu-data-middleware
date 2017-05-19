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
    @Query(value = "SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ grossmargin.AfterFoldingPrice,0) AS goodsGrossMargin FROM\n" +
        "(SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice  FROM\n" +
        "        (SELECT coi.\"Quantity\",CASE WHEN COALESCE(coi.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END AS cost,coi.\"AfterFoldingPrice\"\n" +
        "         FROM  songshu_cs_order co\n" +
        "         RIGHT JOIN ( SELECT DISTINCT oo.\"Id\" FROM songshu_cs_order oo\n" +
        "                     INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\"\n" +
        "                     INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record\n" +
        "                                 WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr\n" +
        "                                 GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\"\n" +
        "                     WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.paidTime\n" +
        "                     BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN (6,7) AND oo.\"orderType\" IN(0, 1)\n" +
        "                     AND oo.\"Channel\" IN(0, 1, 2, 3, 5) ) coo ON co.\"Id\" = coo.\"Id\"\n" +
        "         INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\"\n" +
        "         INNER JOIN songshu_cs_goods g ON  g.\"Id\" = coi.\"GoodsId\"\n" +
        "        )base\n" +
        ")grossmargin;", nativeQuery = true)
    Double getCrossMarginWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    /**
     * 毛利率（单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ grossmargin.AfterFoldingPrice,0) AS goodsGrossMargin FROM\n" +
        "(SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice  FROM\n" +
        "        (SELECT coi.\"Quantity\",CASE WHEN COALESCE(coi.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END AS cost,coi.\"AfterFoldingPrice\"\n" +
        "         FROM  songshu_cs_order co\n" +
        "         RIGHT JOIN ( SELECT DISTINCT oo.\"Id\" FROM songshu_cs_order oo\n" +
        "                     INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\"\n" +
        "                     INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record\n" +
        "                                 WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr\n" +
        "                                 GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\"\n" +
        "                     WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.paidTime\n" +
        "                     BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN (6,7) AND oo.\"orderType\" IN(0, 1)\n" +
        "                     AND oo.\"Channel\" = ?3 ) coo ON co.\"Id\" = coo.\"Id\"\n" +
        "         INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\"\n" +
        "         INNER JOIN songshu_cs_goods g ON  g.\"Id\" = coi.\"GoodsId\"\n" +
        "        )base\n" +
        ")grossmargin;", nativeQuery = true)
    Double getCrossMarginWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);

    /**
     * 毛利率趋势（全平台）
     * @param beginTime
     * @param endTime
     * @param interval
     * @return
     */
    @Query(value = "SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (grossmargin.AfterFoldingPrice)),0) AS goodsGrossMargin FROM\n" +
        "( SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice, base.stime, base.etime FROM\n" +
        "    (SELECT  coi.\"Quantity\",coi.\"AfterFoldingPrice\",CASE WHEN coi.\"ReferCost\" = 0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END  AS cost,tss.stime,tss.etime\n" +
        "     FROM songshu_cs_order co\n" +
        "     RIGHT JOIN (SELECT  oo.\"Id\", MAX(cpr.paidTime) AS MPaidTime\n" +
        "                FROM songshu_cs_order oo\n" +
        "                INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\"\n" +
        "                INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record\n" +
        "                            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr\n" +
        "                            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\"\n" +
        "                WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.paidTime\n" +
        "                BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN (6, 7) AND oo.\"orderType\" IN (0, 1)\n" +
        "                AND  oo.\"Channel\" IN (0, 1, 2, 3, 5)  GROUP BY oo.\"Id\") coo ON co.\"Id\" = coo.\"Id\"\n" +
        "     INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\"\n" +
        "     INNER JOIN songshu_cs_goods g ON g.\"Id\" = coi.\"GoodsId\"\n" +
        "     RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + ?3 * INTERVAL '1 second' AS etime\n" +
        "                FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss\n" +
        "                ON (coo.MPaidTime < tss.etime AND coo.MPaidTime >= tss.stime)\n" +
        "    )base GROUP BY base.stime, base.etime ORDER BY base.stime\n" +
        ")grossmargin;", nativeQuery = true)
    List<Object[]> getCrossMarginTrendWithAllPlatform(Timestamp beginTime, Timestamp endTime, Integer interval);

    /**
     * 毛利率趋势（单平台）
     * @param beginTime
     * @param endTime
     * @param interval
     * @param platform
     * @return
     */
    @Query(value = "SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (grossmargin.AfterFoldingPrice)),0) AS goodsGrossMargin FROM\n" +
        "( SELECT SUM(base.\"Quantity\" * base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice, base.stime, base.etime FROM\n" +
        "    (SELECT  coi.\"Quantity\",coi.\"AfterFoldingPrice\",CASE WHEN coi.\"ReferCost\" = 0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END  AS cost,tss.stime,tss.etime\n" +
        "     FROM songshu_cs_order co\n" +
        "     RIGHT JOIN (SELECT  oo.\"Id\", MAX(cpr.paidTime) AS MPaidTime\n" +
        "                FROM songshu_cs_order oo\n" +
        "                INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\"\n" +
        "                INNER JOIN (SELECT pr.\"MergePaymentNo\",pr.\"PaymentModeType\", MAX(pr.\"PaidTime\") AS paidTime FROM (select * from songshu_cs_payment_record\n" +
        "                            WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr\n" +
        "                            GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\"\n" +
        "                WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.paidTime\n" +
        "                BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN (6, 7) AND oo.\"orderType\" IN (0, 1)\n" +
        "                AND  oo.\"Channel\" = ?4  GROUP BY oo.\"Id\") coo ON co.\"Id\" = coo.\"Id\"\n" +
        "     INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\"\n" +
        "     INNER JOIN songshu_cs_goods g ON g.\"Id\" = coi.\"GoodsId\"\n" +
        "     RIGHT JOIN (SELECT  ts.generate_series  AS stime,ts.generate_series + ?3 * INTERVAL '1 second' AS etime\n" +
        "                FROM (SELECT generate_series(?1, ?2, ?3 * INTERVAL '1 second')) ts) tss\n" +
        "                ON (coo.MPaidTime < tss.etime AND coo.MPaidTime >= tss.stime)\n" +
        "    )base GROUP BY base.stime, base.etime ORDER BY base.stime\n" +
        ")grossmargin;", nativeQuery = true)
    List<Object[]> getCrossMarginTrendWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer interval,Integer platform);


}
