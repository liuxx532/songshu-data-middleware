package com.comall.songshu.service.product;

import com.comall.songshu.constants.TitleConstants;
import com.comall.songshu.repository.product.ProductRevenueRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.exceptions.Exceptions;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 商品销售
 *
 * @author liushengling
 * @create 2017-05-02-18:18
 **/
@Service
public class ProductRevenueService {

    @Autowired
    private ProductRevenueRepository productRevenueRepository;

    public String getProductRevenue(String target, String platformName, Timestamp beginTime, Timestamp endTime,Integer topCount){


        int platform = TransferUtil.getPlatform(platformName);
        List<Object[] > productRevenueResult;

        if (platform<0){//全部
            productRevenueResult = productRevenueRepository.getProductRevenueAllPlatform(beginTime,endTime,topCount);
        }else {
            productRevenueResult = productRevenueRepository.getProductRevenueSinglePlatform(beginTime,endTime,platform,topCount);
        }



        JSONArray dataPointsArray =  new JSONArray();
        //TODO REPOSITORY层神策相关sql编写
        if(Optional.ofNullable(productRevenueResult)
            .filter(l -> l.size() >0).isPresent()){
            //封装表头
            JSONArray productRevenueTitleArray =  new JSONArray();
            long currentMills = System.currentTimeMillis();
            Map<String,String> productRevenueTitleMap = new LinkedHashMap<>();
            productRevenueTitleMap.put(TitleConstants.RANK,"排名");
            productRevenueTitleMap.put(TitleConstants.CATEGORY_NAME,"品类");
            productRevenueTitleMap.put(TitleConstants.PRODUCT_NAME,"商品名称");
            productRevenueTitleMap.put(TitleConstants.REVENUE,"销售额");
            productRevenueTitleMap.put(TitleConstants.ORDER_COUNT,"订单量");
            productRevenueTitleMap.put(TitleConstants.GOODS_COST,"商品成本");
            productRevenueTitleMap.put(TitleConstants.GROSSMARIN_RATE,"毛利率");
            productRevenueTitleMap.put(TitleConstants.UNIQUE_VISITOR,"访客数");
            productRevenueTitleMap.put(TitleConstants.ADD2CART_TIMES,"加购次数");
            productRevenueTitleMap.put(TitleConstants.COLLECTION_COUNT,"收藏量");
            productRevenueTitleMap.put(TitleConstants.PAID_RATE,"付费率");
            productRevenueTitleMap.put(TitleConstants.EXIT_RATE,"退出率");
            productRevenueTitleArray.put(productRevenueTitleMap);
            productRevenueTitleArray.put(currentMills);



            JSONArray productRevenueDataPointArray =  new JSONArray();
            List<JSONObject> productRevenueList = new LinkedList<>();

            int rank = 1;
            for(Object[] o : productRevenueResult){
                JSONObject productRevenue;
                try {
                    productRevenue = new JSONObject();
                    Integer productId = (Integer)o[0];
                    Integer goodsId = (Integer)o[1];
                    String productCode = (String)o[2];
                    String productIdLike ="%productId="+productId+"%";
                    String goodsIdLike ="%goodsId="+goodsId+"%";
                    //加入购物车数
                    Integer addCartTimes = 1;
                    //收藏数
                    Integer collectionCount = 2;
                    //商品页面访客数
                    Integer productPageVisitors = 3;
                    //商品消费用户数
                    Integer productConsumerCount = 4;
                    //退出商品页面用户数
                    Integer productPageLeaveVisitors = 5;
                    if (platform<0) {//全部
//                        addCartTimes = productRevenueRepository.getAddCartTimesAllPlatform(beginTime,endTime,productCode);
//                        collectionCount = productRevenueRepository.getCollectionCountAllPlatform(beginTime,endTime,productCode);
//                        productPageVisitors = productRevenueRepository.getProductPageVisitorsAllPlatform(beginTime,endTime,productIdLike,goodsIdLike);
//                        productConsumerCount = productRevenueRepository.getProductConsumerCountAllPlatform(beginTime,endTime,productId);
//                        productPageLeaveVisitors = productRevenueRepository.getProductPageLeaveVisitorsAllPlatform(beginTime,endTime,productIdLike,goodsIdLike);
                    }else{
//                        addCartTimes = productRevenueRepository.getAddCartTimesSinglePlatform(beginTime,endTime,productCode,platformName);
//                        collectionCount = productRevenueRepository.getCollectionCountSinglePlatform(beginTime,endTime,productCode,platformName);
//                        productPageVisitors = productRevenueRepository.getProductPageVisitorsSinglePlatform(beginTime,endTime,productIdLike,goodsIdLike,platformName);
//                        productConsumerCount = productRevenueRepository.getProductConsumerCountSinglePlatform(beginTime,endTime,productId,platform);
//                        productPageLeaveVisitors = productRevenueRepository.getProductPageLeaveVisitorsSinglePlatform(beginTime,endTime,productIdLike,goodsIdLike,platformName);
                    }


                    //付费率
                    Double paidRate = Optional.ofNullable(productPageVisitors)
                        .filter(p -> p>0)
                        .map(value ->new BigDecimal(productConsumerCount).doubleValue()/new BigDecimal(value).doubleValue())
                        .orElse(0.0);
                    //退出率
                    Double exitRate = Optional.ofNullable(productPageVisitors)
                        .filter(p -> p>0)
                        .map(value ->new BigDecimal(productPageLeaveVisitors).doubleValue()/new BigDecimal(value).doubleValue())
                        .orElse(0.0);

                    productRevenue.put(TitleConstants.RANK,rank);
                    productRevenue.put(TitleConstants.CATEGORY_NAME,o[3]);
                    productRevenue.put(TitleConstants.PRODUCT_NAME,o[4]);
                    productRevenue.put(TitleConstants.REVENUE,o[5]);
                    productRevenue.put(TitleConstants.GOODS_COST,o[6]);
                    productRevenue.put(TitleConstants.ORDER_COUNT,o[7]);
                    productRevenue.put(TitleConstants.GROSSMARIN_RATE,o[8]);
                    productRevenue.put(TitleConstants.UNIQUE_VISITOR,productPageVisitors);
                    productRevenue.put(TitleConstants.ADD2CART_TIMES,addCartTimes);
                    productRevenue.put(TitleConstants.COLLECTION_COUNT,collectionCount);
                    productRevenue.put(TitleConstants.PAID_RATE,paidRate);
                    productRevenue.put(TitleConstants.EXIT_RATE,exitRate);
                    productRevenueList.add(productRevenue);
                    rank++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            productRevenueDataPointArray.put(productRevenueList);
            productRevenueDataPointArray.put(currentMills);
            dataPointsArray.put(productRevenueDataPointArray);
            dataPointsArray.put(productRevenueTitleArray);
        }
        //TODO 返回数据拼装
        return JsonStringBuilder.buildCommonJsonString(target,dataPointsArray,"");
    }
}
