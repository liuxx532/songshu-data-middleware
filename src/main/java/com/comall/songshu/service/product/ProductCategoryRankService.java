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
            for (Object[] objarr: productCategoryRankResult) {
                totalAmount = totalAmount.add((BigDecimal) objarr[1]);
            }
            //加入销售额占比
            for (Object[] objarr: productCategoryRankResult) {
                Object[] objects = Arrays.copyOf(objarr,objarr.length);
                objects[1] = ((BigDecimal) objarr[1]).divide(totalAmount, 2, BigDecimal.ROUND_HALF_UP);
                productCategoryRankAddResult.add(objects);
            }
            //只要前十
            if(productCategoryRankAddResult.size() > 10){
                productCategoryRankAddResult = productCategoryRankAddResult.subList(0,10);
            }
        }

        return buildJson(productCategoryRankAddResult,target);
    }

    private String buildJson(List<Object[]> src,String target) {
        JSONArray dataPointsArray = new JSONArray();
        long currentMills = System.currentTimeMillis();
        List titleDataPoint = new LinkedList();
        List dataPoint = new LinkedList();
        List dataPointParent = new LinkedList();
        Map<String,String> titleDateItem = new LinkedHashMap<>();

        //标题
        titleDateItem.put(TitleConstants.CATEGORY_NAME, "品类名称");
        titleDateItem.put(TitleConstants.REVENUE_RATE, "销售额占比");
        titleDataPoint.add(titleDateItem);
        titleDataPoint.add(currentMills);

        //数据
        for (Object[] item : src) {
            Map<String,String> dateItem = new LinkedHashMap<>();
            dateItem.put(TitleConstants.CATEGORY_NAME, item[0].toString());
            dateItem.put(TitleConstants.REVENUE_RATE, item[1].toString());
            dataPoint.add(dateItem);
        }
        dataPointParent.add(dataPoint);
        dataPointParent.add(currentMills);

        dataPointsArray.put(dataPointParent);
        dataPointsArray.put(titleDataPoint);
        //TODO 返回数据拼装
        return JsonStringBuilder.buildCommonJsonString(target,dataPointsArray,"");
    }
}
