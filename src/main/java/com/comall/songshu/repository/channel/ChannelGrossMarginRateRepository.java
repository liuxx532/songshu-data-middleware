package com.comall.songshu.repository.channel;

import com.comall.songshu.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * 渠道页面毛利率
 * Created by liugaoyu on 2017/4/20.
 */
// 毛利率 = (销售额 - 商品成本） / 销售额 * 100%
public interface ChannelGrossMarginRateRepository extends JpaRepository<Author,Long> {

    /**
     * 全渠道毛利率（全平台）
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END),0.0) AS goodsGrossMargin " +
        "FROM(SELECT SUM(base.\"Quantity\"* base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice " +
        "     FROM(SELECT  coi.\"Quantity\",CASE WHEN COALESCE(coi.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END  AS cost,coi.\"AfterFoldingPrice\" " +
        "          FROM songshu_cs_order_item coi " +
        "          INNER JOIN songshu_cs_goods g ON g.\"Id\" = coi.\"GoodsId\" " +
        "          INNER JOIN (SELECT DISTINCT oo.\"Id\" FROM songshu_cs_order oo " +
        "                        INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop. \"OrderId\" " +
        "                        INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "                                    WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                                    GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "                        WHERE cop.\"PaymentStatus\" = 1 AND oo. \"OrderStatus\" NOT IN (6,7) AND oo. \"orderType\" IN(0, 1) " +
        "                        AND cpr.paidTime BETWEEN ?1 AND ?2  " +
        "                     ) coo ON coi.\"OrderId\" = coo. \"Id\" " +
        "     )base " +
        ")grossmargin", nativeQuery = true)
    Double getChannelCrossMarginWithAllPlatformAllChannel(Timestamp beginTime, Timestamp endTime);

    /**
     * 全渠道毛利率（单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @return
     */
    @Query(value = "SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END),0.0) AS goodsGrossMargin " +
        "FROM(SELECT SUM(base.\"Quantity\"* base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice " +
        "     FROM(SELECT  coi.\"Quantity\",CASE WHEN COALESCE(coi.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END  AS cost,coi.\"AfterFoldingPrice\" " +
        "          FROM songshu_cs_order_item coi " +
        "          INNER JOIN songshu_cs_goods g ON g.\"Id\" = coi.\"GoodsId\" " +
        "          INNER JOIN (SELECT DISTINCT oo.\"Id\" FROM songshu_cs_order oo " +
        "                        INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop. \"OrderId\" " +
        "                        INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "                                    WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                                    GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "                        WHERE cop.\"PaymentStatus\" = 1 AND oo. \"OrderStatus\" NOT IN (6,7) AND oo. \"orderType\" IN(0, 1) " +
        "                        AND cpr.paidTime BETWEEN ?1 AND ?2  AND oo. \"Channel\" = ?3 " +
        "                     ) coo ON coi.\"OrderId\" = coo. \"Id\" " +
        "     )base " +
        ")grossmargin", nativeQuery = true)
    Double getChannelCrossMarginWithSinglePlatformAllChannel(Timestamp beginTime, Timestamp endTime, Integer platform);

    /**
     * 单渠道毛利率（全平台）
     * @param beginTime
     * @param endTime
     * @param channelName
     * @return
     */
    @Query(value = "SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END),0.0) AS goodsGrossMargin " +
        "FROM(SELECT SUM(base.\"Quantity\"* base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice " +
        "     FROM(SELECT  coi.\"Quantity\",CASE WHEN COALESCE(coi.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END  AS cost,coi.\"AfterFoldingPrice\" " +
        "          FROM songshu_cs_order_item coi " +
        "          INNER JOIN songshu_cs_goods g ON g.\"Id\" = coi.\"GoodsId\" " +
        "          INNER JOIN (SELECT DISTINCT oo.\"Id\" " +
        "                      FROM songshu_cs_order oo " +
        "                      INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "                      INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "                                  WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                                  GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "                      LEFT JOIN (SELECT mem.\"id\" AS memberId, CASE " +
        "                                    WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source) " +
        "                                    WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO' " +
        "                                    WHEN mem.\"multipleChannelsId\" = 2 THEN 'APPLESTORE' " +
        "                                    WHEN mem.\"multipleChannelsId\" = 3 THEN 'WEIXIN' " +
        "                                    WHEN mem.\"multipleChannelsId\" = 5 THEN 'WAP' ELSE 'WAP' END AS utm_source " +
        "                                    FROM songshu_cs_member mem " +
        "                                    LEFT JOIN songshu_shence_users u ON u.second_id = mem.\"id\") tsource ON oo.\"MemberId\" = tsource.memberId " +
        "                      WHERE cop.\"PaymentStatus\" = 1 AND oo. \"OrderStatus\" NOT IN (6,7) AND oo. \"orderType\" IN(0, 1) " +
        "                      AND cpr.paidTime BETWEEN ?1 AND ?2  AND tsource.utm_source = ?3 " +
        "                     ) coo  ON coi.\"OrderId\" = coo.\"Id\" " +
        "     )base " +
        ") grossmargin", nativeQuery = true)
    Double getChannelCrossMarginWithAllPlatformSingleChannel(Timestamp beginTime, Timestamp endTime,String channelName);

    /**
     * 单渠道毛利率（单平台）
     * @param beginTime
     * @param endTime
     * @param platform
     * @param channelName
     * @return
     */
    @Query(value = "SELECT COALESCE((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (CASE WHEN grossmargin.AfterFoldingPrice = 0 THEN null ELSE grossmargin.AfterFoldingPrice END),0.0) AS goodsGrossMargin " +
        "FROM(SELECT SUM(base.\"Quantity\"* base.cost)AS referCost, SUM(base.\"AfterFoldingPrice\")AS AfterFoldingPrice " +
        "     FROM(SELECT  coi.\"Quantity\",CASE WHEN COALESCE(coi.\"ReferCost\",0) =0 THEN g.\"CostPrice\" ELSE coi.\"ReferCost\" END  AS cost,coi.\"AfterFoldingPrice\" " +
        "          FROM songshu_cs_order_item coi " +
        "          INNER JOIN songshu_cs_goods g ON g.\"Id\" = coi.\"GoodsId\" " +
        "          INNER JOIN (SELECT DISTINCT oo.\"Id\" " +
        "                      FROM songshu_cs_order oo " +
        "                      INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "                      INNER JOIN (SELECT pr.\"MergePaymentNo\", MAX(pr.\"PaidTime\") AS paidTime FROM (SELECT * FROM songshu_cs_payment_record " +
        "                                  WHERE \"PaymentModeType\" = 2 AND \"PaidTime\" BETWEEN (CAST(?1 AS TIMESTAMP) - INTERVAL '1 D') AND (CAST(?2 AS TIMESTAMP) + INTERVAL '1 D')) pr " +
        "                                  GROUP BY \"MergePaymentNo\", \"PaymentModeType\") cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "                      LEFT JOIN (SELECT mem.\"id\" AS memberId, CASE " +
        "                                    WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NOT NULL THEN upper(u.utm_source) " +
        "                                    WHEN mem.\"multipleChannelsId\" = 1 AND u.utm_source IS NULL THEN 'YINGYONGBAO' " +
        "                                    WHEN mem.\"multipleChannelsId\" = 2 THEN 'APPLESTORE' " +
        "                                    WHEN mem.\"multipleChannelsId\" = 3 THEN 'WEIXIN' " +
        "                                    WHEN mem.\"multipleChannelsId\" = 5 THEN 'WAP' ELSE 'WAP' END AS utm_source " +
        "                                    FROM songshu_cs_member mem " +
        "                                    LEFT JOIN songshu_shence_users u ON u.second_id = mem.\"id\") tsource ON oo.\"MemberId\" = tsource.memberId " +
        "                      WHERE cop.\"PaymentStatus\" = 1 AND oo. \"OrderStatus\" NOT IN (6,7) AND oo. \"orderType\" IN(0, 1) " +
        "                      AND cpr.paidTime BETWEEN ?1 AND ?2  AND oo.\"Channel\" =?3 AND tsource.utm_source = ?4 " +
        "                     ) coo  ON coi.\"OrderId\" = coo.\"Id\" " +
        "     )base " +
        ") grossmargin", nativeQuery = true)
    Double getChannelCrossMarginWithSinglePlatformSingleChannel(Timestamp beginTime, Timestamp endTime, Integer platform,String channelName);


}
