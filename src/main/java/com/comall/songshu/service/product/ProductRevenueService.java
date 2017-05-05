package com.comall.songshu.service.product;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


        JSONArray productRevenueArray =  new JSONArray();

        if(Optional.ofNullable(productRevenueResult)
            .filter(l -> l.size() >0).isPresent()){
            for(Object[] o : productRevenueResult){
                JSONObject productRevenue;
                try {
                    productRevenue = new JSONObject();
                    Integer productId = (Integer)o[0];
                    //加入购物车数
                    Integer addCartTimes;
                    //收藏数
                    Integer collectionCount;
                    //商品页面访客数
                    Integer productPageVisitors;
                    //商品消费用户数
                    Integer productConsumerCount;
                    //退出商品页面用户数
                    Integer productPageLeaveVisitors;
                    if (platform<0) {//全部
                        addCartTimes = productRevenueRepository.getAddCartTimesAllPlatform(beginTime,endTime,productId);
                        collectionCount = productRevenueRepository.getCollectionCountAllPlatform(beginTime,endTime,productId);
                        productPageVisitors = productRevenueRepository.getProductPageVisitorsAllPlatform(beginTime,endTime,productId);
                        productConsumerCount = productRevenueRepository.getProductConsumerCountAllPlatform(beginTime,endTime,productId);
                        productPageLeaveVisitors = productRevenueRepository.getProductPageLeaveVistorsAllPlatform(beginTime,endTime,productId);
                    }else{
                        addCartTimes = productRevenueRepository.getAddCartTimesSinglePlatform(beginTime,endTime,productId,platform);
                        collectionCount = productRevenueRepository.getCollectionCountSinglePlatform(beginTime,endTime,productId,platform);
                        productPageVisitors = productRevenueRepository.getProductPageVisitorsSinglePlatform(beginTime,endTime,productId,platform);
                        productConsumerCount = productRevenueRepository.getProductConsumerCountSinglePlatform(beginTime,endTime,productId,platform);
                        productPageLeaveVisitors = productRevenueRepository.getProductPageLeaveVistorsSinglePlatform(beginTime,endTime,productId,platform);
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

                    productRevenue.put("categoryName",o[1]);
                    productRevenue.put("productName",o[2]);
                    productRevenue.put("revenue",o[3]);
                    productRevenue.put("cost",o[4]);
                    productRevenue.put("grossMarginRate",o[5]);
                    productRevenue.put("addCartTimes",addCartTimes);
                    productRevenue.put("collectionCount",collectionCount);
                    productRevenue.put("paidRate",paidRate);
                    productRevenue.put("exitRate",exitRate);
                    productRevenueArray.put(productRevenue);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return productRevenueArray.toString();
    }
}
