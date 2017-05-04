package com.comall.songshu.service.product;

import com.comall.songshu.repository.product.ProductLinkedSalesRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关联商品销售统计
 *
 * @author liushengling
 * @create 2017-05-02-18:18
 **/
@Service
public class ProductLinkedSalesService {

    @Autowired
    private ProductLinkedSalesRepository productLinkedSalesRepository;

    public String getProductLinkedSales(String target, String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime,Integer topCount) {


        int platform = TransferUtil.getPlatform(platformName);
        //存放排面靠前几的商品集合（父集）
        List<Object[]> productSalesResult;
        //存放关联商品集合（子集）
        Map<Integer,List<Object[]>> linkedSalesMap = new HashMap<>();
        if (platform<0){//全部
            productSalesResult =  productLinkedSalesRepository.getProductSalesAllPlatform(beginTime,endTime,topCount);
            if(productSalesResult != null){
                for(Object[] o : productSalesResult){
                    Integer productId = (Integer) o[0];
                    List<Object[]> productLinkedSalesResult = productLinkedSalesRepository.getProductLinkedSalesAllPlatform(beginTime,endTime,topCount,productId);
                    linkedSalesMap.put(productId,productLinkedSalesResult);
                }
            }
        }else {
            productSalesResult =  productLinkedSalesRepository.getProductSalesSinglePlatform(beginTime,endTime,platform,topCount);
            if(productSalesResult != null){
                for(Object[] o : productSalesResult){
                    Integer productId = (Integer) o[0];
                    List<Object[]> productLinkedSalesResult = productLinkedSalesRepository.getProductLinkedSalesSinglePlatform(beginTime,endTime,topCount,topCount,productId);
                    linkedSalesMap.put(productId,productLinkedSalesResult);
                }
            }
        }

        return null;
    }
}
