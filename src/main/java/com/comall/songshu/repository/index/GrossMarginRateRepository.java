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

    // 获取商品成本
//SQL:
//SELECT SUM(item."ActualRefundMoney")
// FROM songshu_cs_order oo INNER JOIN songshu_cs_order_payable op ON op."OrderId" = oo."Id"
// INNER JOIN songshu_cs_payment_record r ON op."MergePaymentId" = r."MergePaymentNo"
// INNER JOIN songshu_cs_refund_item item ON item."PaymentRecordId" = r."Id"
// WHERE r."PaymentModeType" = 2 AND item. "Status" = 5 AND item."MoneyType" = 1 AND item."RefundType" = 1 AND oo."orderType" IN(0, 1)
// AND item."LastModifyTime" BETWEEN ? AND ? AND oo."Channel" IN(0, 1, 2, 3, 5)


// 获取商品成本

    @Query(value = "SELECT SUM(cg.\"CostPrice\" * coi.\"Quantity\") " +
        "FROM( SELECT DISTINCT co.\"Id\" FROM( SELECT * FROM songshu_cs_payment_record " +
        "WHERE \"PaymentModeType\"= 2 AND \"PaidTime\" BETWEEN ?1 AND ?2)cpr " +
        "LEFT JOIN songshu_cs_order co ON cpr.\"MergePaymentNo\" = co.\"OrderNumber\" " +
        "INNER JOIN songshu_cs_order_payable cop ON co.\"Id\" = cop.\"OrderId\" WHERE cop.\"PaymentStatus\" = 1 " +
        "AND co.\"orderType\" IN(0, 1) AND co.\"OrderStatus\" NOT IN (6,7) AND co.\"Channel\" IN (0, 1, 2, 3, 5) )coo " +
        "INNER JOIN songshu_cs_order_item coi ON coo.\"Id\" = coi.\"OrderId\" " +
        "INNER JOIN songshu_cs_goods cg ON cg.\"Id\" = coi.\"GoodsId\"", nativeQuery = true)
    Double getProductCostWithAllPlatform(Timestamp beginTime, Timestamp endTime);
    @Query(value = "SELECT SUM(cg.\"CostPrice\" * coi.\"Quantity\") " +
        "FROM( SELECT DISTINCT co.\"Id\" FROM( SELECT * FROM songshu_cs_payment_record " +
        "WHERE \"PaymentModeType\"= 2 AND \"PaidTime\" BETWEEN ?2 AND ?3)cpr " +
        "LEFT JOIN songshu_cs_order co ON cpr.\"MergePaymentNo\" = co.\"OrderNumber\" " +
        "INNER JOIN songshu_cs_order_payable cop ON co.\"Id\" = cop.\"OrderId\" WHERE cop.\"PaymentStatus\" = 1 " +
        "AND co.\"orderType\" IN(0, 1) AND co.\"OrderStatus\" NOT IN (6,7) AND co.\"Channel\" =?1 )coo " +
        "INNER JOIN songshu_cs_order_item coi ON coo.\"Id\" = coi.\"OrderId\" " +
        "INNER JOIN songshu_cs_goods cg ON cg.\"Id\" = coi.\"GoodsId\"", nativeQuery = true)
    Double getProductCostWithSinglePlatform(Integer platform, Timestamp beginTime, Timestamp endTime);

//SQL:
    //SELECT( grossmargin.AfterFoldingPrice - grossmargin.referCost)/ grossmargin.AfterFoldingPrice AS goodsGrossMargin
    // FROM ( SELECT SUM(coi."Quantity" * coi."ReferCost")AS referCost, SUM(coi."AfterFoldingPrice")AS AfterFoldingPrice
    // FROM songshu_cs_order co RIGHT JOIN ( SELECT DISTINCT oo."Id" FROM songshu_cs_order oo
    // INNER JOIN songshu_cs_order_payable cop ON oo."Id" = cop."OrderId"
    // INNER JOIN songshu_cs_payment_record cpr ON cpr."MergePaymentNo" = cop."MergePaymentId" WHERE cop."PaymentStatus" = 1
    // AND cpr."PaymentModeType" = 2 AND cpr."PaidTime" BETWEEN '2015-09-01 00:00:00' AND '2016-01-09 00:00:00' AND oo."OrderStatus"
    // NOT IN (6,7) AND oo."orderType" IN(0, 1) AND oo."Channel" IN(0, 1, 2, 3, 5) ) coo ON co."Id" = coo."Id"
    // INNER JOIN songshu_cs_order_item coi ON co."Id" = coi."OrderId" )grossmargin

    @Query(value = "SELECT( grossmargin.AfterFoldingPrice - grossmargin.referCost)/ grossmargin.AfterFoldingPrice AS goodsGrossMargin " +
        "FROM ( SELECT SUM(coi.\"Quantity\" * coi.\"ReferCost\")AS referCost, SUM(coi.\"AfterFoldingPrice\")AS AfterFoldingPrice " +
        "FROM songshu_cs_order co RIGHT JOIN ( SELECT DISTINCT oo.\"Id\" FROM songshu_cs_order oo " +
        "INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "INNER JOIN songshu_cs_payment_record cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.\"PaidTime\" " +
        "BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN (6,7) AND oo.\"orderType\" IN(0, 1) " +
        "AND oo.\"Channel\" IN(0, 1, 2, 3, 5) ) coo ON co.\"Id\" = coo.\"Id\" " +
        "INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\" )grossmargin", nativeQuery = true)
    Double getCrossMarginWithAllPlatform(Timestamp beginTime, Timestamp endTime);

    @Query(value = "SELECT( grossmargin.AfterFoldingPrice - grossmargin.referCost)/ grossmargin.AfterFoldingPrice AS goodsGrossMargin " +
        "FROM ( SELECT SUM(coi.\"Quantity\" * coi.\"ReferCost\")AS referCost, SUM(coi.\"AfterFoldingPrice\")AS AfterFoldingPrice " +
        "FROM songshu_cs_order co RIGHT JOIN ( SELECT DISTINCT oo.\"Id\" FROM songshu_cs_order oo " +
        "INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "INNER JOIN songshu_cs_payment_record cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.\"PaidTime\" " +
        "BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN (6,7) AND oo.\"orderType\" IN(0, 1) " +
        "AND oo.\"Channel\" = ?3 ) coo ON co.\"Id\" = coo.\"Id\" " +
        "INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\" )grossmargin", nativeQuery = true)
    Double getCrossMarginWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer platform);

//SQL:
    // SELECT( grossmargin.AfterFoldingPrice - grossmargin.referCost)/ grossmargin.AfterFoldingPrice AS goodsGrossMargin, grossmargin.stime, grossmargin.etime
    // FROM ( SELECT SUM(coi."Quantity" * coi."ReferCost")AS referCost, SUM(coi."AfterFoldingPrice")AS AfterFoldingPrice, tss.stime, tss.etime
    // FROM songshu_cs_order co RIGHT JOIN( SELECT oo."Id", MAX(cpr."PaidTime")AS MPaidTime FROM songshu_cs_order oo
    // INNER JOIN songshu_cs_order_payable cop ON oo."Id" = cop."OrderId" INNER JOIN songshu_cs_payment_record cpr ON cpr."MergePaymentNo" = cop."MergePaymentId" WHERE cop."PaymentStatus" = 1
    // AND cpr."PaymentModeType" = 2 AND cpr."PaidTime" BETWEEN '2016-01-01 00:00:00' AND '2016-01-09 00:00:00' AND oo."OrderStatus" NOT IN(6, 7)
    // AND oo."orderType" IN(0, 1) AND oo."Channel" IN(0, 1, 2, 3, 5) GROUP BY oo."Id" )coo ON co."Id" = coo."Id"
    // INNER JOIN songshu_cs_order_item coi ON co."Id" = coi."OrderId" RIGHT JOIN( SELECT ts.generate_series AS stime, ts.generate_series + '86400 second' AS etime
    // FROM ( SELECT generate_series( '2016-01-01 00:00:00' :: TIMESTAMP, '2016-01-09 00:00:00' :: TIMESTAMP, 86400 * INTERVAL '1 second' ) )ts )tss
    // ON( coo.MPaidTime < tss.etime AND coo.MPaidTime >= tss.stime ) GROUP BY tss.stime, tss.etime ORDER BY tss.stime )grossmargin

    @Query(value = "SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (grossmargin.AfterFoldingPrice)),0) AS goodsGrossMargin " +
        "FROM ( SELECT SUM(coi.\"Quantity\" * coi.\"ReferCost\")AS referCost, SUM(coi.\"AfterFoldingPrice\")AS AfterFoldingPrice, tss.stime, tss.etime FROM songshu_cs_order co " +
        "RIGHT JOIN( SELECT oo.\"Id\", MAX(cpr.\"PaidTime\")AS MPaidTime FROM songshu_cs_order oo " +
        "INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "INNER JOIN songshu_cs_payment_record cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.\"PaidTime\" " +
        "BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN(6, 7) AND oo.\"orderType\" IN(0, 1) AND oo.\"Channel\" IN(0, 1, 2, 3, 5) " +
        "GROUP BY oo.\"Id\" )coo ON co.\"Id\" = coo.\"Id\" INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\" " +
        "RIGHT JOIN( SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "FROM ( SELECT generate_series( ?1, ?2, ?3 * INTERVAL '1 second' ) )ts )tss ON( coo.MPaidTime < tss.etime AND coo.MPaidTime >= tss.stime ) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime )grossmargin", nativeQuery = true)
    List<Object[]> getCrossMarginTrendWithAllPlatform(Timestamp beginTime, Timestamp endTime, Integer interval);

    @Query(value = "SELECT grossmargin.stime, grossmargin.etime, COALESCE(((grossmargin.AfterFoldingPrice - grossmargin.referCost)/ (grossmargin.AfterFoldingPrice)),0) AS goodsGrossMargin " +
        "FROM ( SELECT SUM(coi.\"Quantity\" * coi.\"ReferCost\")AS referCost, SUM(coi.\"AfterFoldingPrice\")AS AfterFoldingPrice, tss.stime, tss.etime FROM songshu_cs_order co " +
        "RIGHT JOIN( SELECT oo.\"Id\", MAX(cpr.\"PaidTime\")AS MPaidTime FROM songshu_cs_order oo " +
        "INNER JOIN songshu_cs_order_payable cop ON oo.\"Id\" = cop.\"OrderId\" " +
        "INNER JOIN songshu_cs_payment_record cpr ON cpr.\"MergePaymentNo\" = cop.\"MergePaymentId\" " +
        "WHERE cop.\"PaymentStatus\" = 1 AND cpr.\"PaymentModeType\" = 2 AND cpr.\"PaidTime\" " +
        "BETWEEN ?1 AND ?2 AND oo.\"OrderStatus\" NOT IN(6, 7) AND oo.\"orderType\" IN(0, 1) AND oo.\"Channel\" = ?4 " +
        "GROUP BY oo.\"Id\" )coo ON co.\"Id\" = coo.\"Id\" INNER JOIN songshu_cs_order_item coi ON co.\"Id\" = coi.\"OrderId\" " +
        "RIGHT JOIN( SELECT ts.generate_series AS stime, ts.generate_series + ?3 * INTERVAL '1 second' AS etime " +
        "FROM ( SELECT generate_series( ?1, ?2, ?3 * INTERVAL '1 second' ) )ts )tss ON( coo.MPaidTime < tss.etime AND coo.MPaidTime >= tss.stime ) " +
        "GROUP BY tss.stime, tss.etime ORDER BY tss.stime )grossmargin", nativeQuery = true)
    List<Object[]> getCrossMarginTrendWithSinglePlatform(Timestamp beginTime, Timestamp endTime,Integer interval,Integer platform);

    // TODO fix slow sql query

}
