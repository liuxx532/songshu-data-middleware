package com.comall.songshu.service.product;

import com.comall.songshu.repository.product.ProductRadarRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public String getProductRadar(String platformName,List<String> excludeCategoryNames, Timestamp beginTime, Timestamp endTime) {

        int platform = TransferUtil.getPlatform(platformName);

        List<Object[]> productRadarResult;
        List<Object[]> productRadarAddResult = new ArrayList<>();

        if (platform < 0) {
            productRadarResult = productRadarRepository.getProductRadarWithAllPlatform(beginTime, endTime);
        } else {
            productRadarResult = productRadarRepository.getProductRadarWithSinglePlatform(beginTime, endTime, platform);
        }
        List<JSONObject> productRadarList = new LinkedList<>();
        if (productRadarResult != null) {
            //只要前十
            if (productRadarResult.size() > 10) {
                productRadarAddResult = productRadarResult.subList(0, 10);
            }else{
                productRadarAddResult = productRadarResult;
            }
            for (Object[] o : productRadarAddResult) {
                String categoryName = (String) o[0];
                JSONObject productRadarObject = new JSONObject();
                if (excludeCategoryNames == null || !excludeCategoryNames.contains(categoryName)) {
                    try {
                        productRadarObject.put("name", categoryName);
                        productRadarObject.put("value", new Object[]{o[1], o[2], o[3], o[4], o[5]});
                        productRadarList.add(productRadarObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return new JSONArray(productRadarList).toString();
    }
}
