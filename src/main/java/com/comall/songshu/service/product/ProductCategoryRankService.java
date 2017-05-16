package com.comall.songshu.service.product;

import com.comall.songshu.constants.TitleConstants;
import com.comall.songshu.repository.product.ProductCategoryRankRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
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
 * 商品品类销售占比排行
 * Created by huanghaizhou on 2017/5/11.
 */
@Service
public class ProductCategoryRankService {

    @Autowired
    private ProductCategoryRankRepository productCategoryRankRepository;

    public String getProductCategoryRank(String target,String platformName, Timestamp beginTime, Timestamp endTime) {

        int platform = TransferUtil.getPlatform(platformName);

        List<Object[] > productCategoryRankResult;
        List<Object[] > productCategoryRankAddResult = new ArrayList<>();//[[name,rate],[name,rate]]

        if (platform<0){
            productCategoryRankResult = productCategoryRankRepository.getProductCategoryRankWithAllPlatform(beginTime,endTime);
        }else{
            productCategoryRankResult = productCategoryRankRepository.getProductCategoryRankWithSinglePlatform(beginTime,endTime,platform);
        }

        //品类总销售额
        BigDecimal totalAmount = BigDecimal.ZERO;

        if(productCategoryRankResult != null){
            //计算品类总销售额
            for (Object[] o: productCategoryRankResult) {
                totalAmount = totalAmount.add((BigDecimal) o[1]);
            }
            //加入销售额占比
            for (Object[] o: productCategoryRankResult) {
                Double rankRate = 0.0;
                if(totalAmount.doubleValue() >0){
                    rankRate = ((BigDecimal) o[1]).doubleValue()/totalAmount.doubleValue();
                }
                productCategoryRankAddResult.add(new Object[]{o[0],rankRate});
            }
            //只要前十
//            if(productCategoryRankAddResult.size() > 10){
//                productCategoryRankAddResult = productCategoryRankAddResult.subList(0,10);
//            }
        }

        return buildJson(productCategoryRankAddResult,target);
    }

    private String buildJson(List<Object[]> src,String target) {
        JSONArray dataPointsArray = new JSONArray();
        long currentMills = System.currentTimeMillis();
        JSONArray titleDataPoint = new JSONArray();
        List dataPoint = new LinkedList();
        JSONArray dataPointParent = new JSONArray();
        Map<String,String> titleDateItem = new LinkedHashMap<>();

        //标题
        titleDateItem.put(TitleConstants.CATEGORY_NAME, "品类名称");
        titleDateItem.put(TitleConstants.REVENUE_RATE, "销售额占比");
        titleDataPoint.put(titleDateItem);
        titleDataPoint.put(currentMills);

        //数据
        for (Object[] item : src) {
            JSONObject productCategoryData = new JSONObject();
            try {
                productCategoryData.put(TitleConstants.CATEGORY_NAME, item[0]);
                productCategoryData.put(TitleConstants.REVENUE_RATE, item[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataPoint.add(productCategoryData);
        }
        dataPointParent.put(dataPoint);
        dataPointParent.put(currentMills);

        dataPointsArray.put(dataPointParent);
        dataPointsArray.put(titleDataPoint);

        return JsonStringBuilder.buildCommonJsonString(target,dataPointsArray,"");
    }
}
