package com.comall.songshu.service.product;

import com.comall.songshu.constants.CommonConstants;
import com.comall.songshu.repository.product.ProductLinkedSalesRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

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

    public String getProductLinkedSales(String target, String platformName, Timestamp beginTime, Timestamp endTime, Integer topCount) {


        int platform = TransferUtil.getPlatform(platformName);
        //存放排面靠前几的商品集合（父集）
        List<Object[]> productSalesResult;
        List<Object[]> productLinkedSalesResult;
        if (platform < 0) {//全部
            productSalesResult = productLinkedSalesRepository.getProductSalesAllPlatform(beginTime, endTime, topCount);
        } else {
            productSalesResult = productLinkedSalesRepository.getProductSalesSinglePlatform(beginTime, endTime, platform, topCount);
        }

        List<JSONObject> productLinkedList = new LinkedList<>();
        if (productSalesResult != null) {

            for (Object[] o : productSalesResult) {
                JSONObject productLinkedJsonObject = new JSONObject();
                List<JSONObject> singleProductLinkedList = new LinkedList<>();
                singleProductLinkedList.add(assembleJSONObject(o));
                Integer productId = (Integer) o[0];
                if (platform < 0) {//全部
                    productLinkedSalesResult = productLinkedSalesRepository.getProductLinkedSalesAllPlatform(beginTime, endTime, topCount, productId);
                } else {
                    productLinkedSalesResult = productLinkedSalesRepository.getProductLinkedSalesAllPlatform(beginTime, endTime, topCount, productId);
                }
                if (productLinkedSalesResult != null) {
                    for (Object[] ol : productLinkedSalesResult) {
                        singleProductLinkedList.add(assembleJSONObject(ol));
                    }
                }
                try {
                    productLinkedJsonObject.put("arrayData", singleProductLinkedList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                productLinkedList.add(productLinkedJsonObject);
            }
        }

        return new JSONArray(productLinkedList).toString();
    }


    private JSONObject assembleJSONObject(Object[] o){
        JSONObject jsonObject = null;
        if(Optional.ofNullable(o)
            .filter(l -> l.length >=3)
            .isPresent()){
            Integer productId = (Integer) o[0];
            jsonObject = new JSONObject();
            String picUrl = productLinkedSalesRepository.getProductPicUrl(productId);
            try {
                jsonObject.put("name",o[1]);
                jsonObject.put("img", CommonConstants.picUrl+picUrl);
                jsonObject.put("sales",o[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }
}
