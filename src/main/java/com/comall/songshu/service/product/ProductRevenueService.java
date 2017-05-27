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

        List<Object[]> titleList = new LinkedList<>();
        List<Object[]> valueList = new LinkedList<>();

        if(Optional.ofNullable(productRevenueResult)
            .filter(l -> l.size() >0).isPresent()){

            int rank = 1;
            for(Object[] o : productRevenueResult){
                Integer productId = (Integer)o[6];
                String productCode = (String)o[7];
                String indexProductLike ="#/tabs/index/productInfo?productId="+productId+"%";
                String categoriesProductLike ="#/tabs/categories/productInfo?productId="+productId+"%";
                String cartLike ="#/tabs/cart/productInfo?productId="+productId+"%";
                String userProductLike ="#/tabs/user/productInfo?productId="+productId+"%";
                String integralProductLike ="#/tabs/integral/integralInfo?id="+productId+"&type=0%";

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
                    addCartTimes = productRevenueRepository.getAddCartTimesAllPlatform(beginTime,endTime,productCode);
                    collectionCount = productRevenueRepository.getCollectionCountAllPlatform(beginTime,endTime,productCode);
                    productPageVisitors = productRevenueRepository.getProductPageVisitorsAllPlatform(beginTime,endTime
                        ,indexProductLike,categoriesProductLike,cartLike,userProductLike,integralProductLike);
                    productConsumerCount = productRevenueRepository.getProductConsumerCountAllPlatform(beginTime,endTime,productId);
                    productPageLeaveVisitors = productRevenueRepository.getProductPageLeaveVisitorsAllPlatform(beginTime,endTime
                        ,indexProductLike,categoriesProductLike,cartLike,userProductLike,integralProductLike);
                }else{
                    addCartTimes = productRevenueRepository.getAddCartTimesSinglePlatform(beginTime,endTime,productCode,platformName);
                    collectionCount = productRevenueRepository.getCollectionCountSinglePlatform(beginTime,endTime,productCode,platformName);
                    productPageVisitors = productRevenueRepository.getProductPageVisitorsSinglePlatform(beginTime,endTime,platformName
                        ,indexProductLike,categoriesProductLike,cartLike,userProductLike,integralProductLike);
                    productConsumerCount = productRevenueRepository.getProductConsumerCountSinglePlatform(beginTime,endTime,productId,platform);
                    productPageLeaveVisitors = productRevenueRepository.getProductPageLeaveVisitorsSinglePlatform(beginTime,endTime,platformName
                        ,indexProductLike,categoriesProductLike,cartLike,userProductLike,integralProductLike);
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

                valueList.add(new Object[]{rank,o[0],o[1],o[2],o[3],o[4],o[5],productPageVisitors,addCartTimes,collectionCount,paidRate,exitRate});
                rank++;
            }

        }

        //表头信息
        titleList.add(new Object[]{TitleConstants.RANK,"排名"});
        titleList.add(new Object[]{TitleConstants.CATEGORY_NAME,"品类"});
        titleList.add(new Object[]{TitleConstants.PRODUCT_NAME,"商品名称"});
        titleList.add(new Object[]{TitleConstants.REVENUE,"销售额"});
        titleList.add(new Object[]{TitleConstants.ORDER_COUNT,"订单量"});
        titleList.add(new Object[]{TitleConstants.GOODS_COST,"商品成本"});
        titleList.add(new Object[]{TitleConstants.GROSSMARIN_RATE,"毛利率"});
        titleList.add(new Object[]{TitleConstants.UNIQUE_VISITOR,"访客数"});
        titleList.add(new Object[]{TitleConstants.ADD2CART_TIMES,"加购次数"});
        titleList.add(new Object[]{TitleConstants.COLLECTION_COUNT,"收藏量"});
        titleList.add(new Object[]{TitleConstants.PAID_RATE,"付费率"});
        titleList.add(new Object[]{TitleConstants.EXIT_RATE,"退出率"});


        return JsonStringBuilder.buildTableJsonString(valueList,titleList,target);
    }
}
