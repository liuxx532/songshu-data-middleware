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

        if (platform<0){
            productCategoryRankResult = productCategoryRankRepository.getProductCategoryRankWithAllPlatform(beginTime,endTime);
        }else{
            productCategoryRankResult = productCategoryRankRepository.getProductCategoryRankWithSinglePlatform(beginTime,endTime,platform);
        }

        //品类总销售额
        BigDecimal totalAmount = BigDecimal.ZERO;

        List<Object[]> titleList = new LinkedList<>();
        List<Object[]> valueList = new LinkedList<>();

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
                valueList.add(new Object[]{o[0],rankRate});
            }
        }

        //表头信息
        titleList.add(new Object[]{TitleConstants.CATEGORY_NAME,"品类名称"});
        titleList.add(new Object[]{TitleConstants.REVENUE_RATE,"销售额占比"});

        return JsonStringBuilder.buildTableJsonString(valueList,titleList,target);
    }

}
