package com.comall.songshu.service.product;

import com.comall.songshu.repository.product.ProductRadarRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.*;

/**
 * 商品雷达图service
 * Created by huanghaizhou on 2017/5/3.
 */
@Service
public class ProductRadarService {

    @Autowired
    private ProductRadarRepository productRadarRepository;

    public String getProductRadar(String platformName, Timestamp beginTime, Timestamp endTime) {

        int platform = TransferUtil.getPlatform(platformName);

        List<Object[]> productRadarResult;

        if (platform < 0) {
            productRadarResult = productRadarRepository.getProductRadarWithAllPlatform(beginTime, endTime);
        } else {
            productRadarResult = productRadarRepository.getProductRadarWithSinglePlatform(beginTime, endTime, platform);
        }
        List<JSONObject> productRadarList = new LinkedList<>();
        if (productRadarResult != null) {

            for (Object[] o : productRadarResult) {
                String categoryName = (String) o[0];
                JSONObject productRadarObject = new JSONObject();
                try {

                    Double grossRate =  new BigDecimal(o[4].toString()).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                    productRadarObject.put("name", categoryName);
                    //销售额，销售数量，商品成本，毛利率，包含商品数
                    productRadarObject.put("value", new Object[]{o[1], o[2], o[3],  grossRate, o[5]});
                    productRadarList.add(productRadarObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return new JSONArray(productRadarList).toString();
    }
}
