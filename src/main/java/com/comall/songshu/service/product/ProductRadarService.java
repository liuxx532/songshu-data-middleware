package com.comall.songshu.service.product;

import com.comall.songshu.repository.product.ProductRadarRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 商品雷达图service
 * Created by huanghaizhou on 2017/5/3.
 */
@Service
public class ProductRadarService {

    @Autowired
    private ProductRadarRepository productRadarRepository;

    public String getProductRadar(String platformName,Integer excludeCategoryId, Timestamp beginTime, Timestamp endTime) {

        int platform = TransferUtil.getPlatform(platformName);

        List<Object[] > productRadarResult;
        List<Object[] > productRadarAddResult = new ArrayList<>();

        if (platform<0){
            productRadarResult = productRadarRepository.getProductRadarWithAllPlatform(beginTime,endTime);
        }else{
            productRadarResult = productRadarRepository.getProductRadarWithSinglePlatform(beginTime,endTime,platform);
        }

        //品类总销售额
        BigDecimal totalAmount = BigDecimal.ZERO;

        if(productRadarResult != null){
            //计算品类总销售额
            for (Object[] objarr: productRadarResult) {
                totalAmount = totalAmount.add((BigDecimal) objarr[1]);
            }
            //加入销售额占比
            for (Object[] objarr: productRadarResult) {
                Object[] objects = Arrays.copyOf(objarr, objarr.length + 1);
                objects[7] = ((BigDecimal) objarr[1]).divide(totalAmount, 2, BigDecimal.ROUND_HALF_UP);
                productRadarAddResult.add(objects);
            }
            //只要前十
            if(productRadarAddResult.size() > 10){
                productRadarAddResult = productRadarAddResult.subList(0,10);
            }
            //如果excludeCategoryId != null,代表需要排除该品类
            if(excludeCategoryId != null){
                Iterator<Object[]> iterator = productRadarAddResult.iterator();
                while(iterator.hasNext()){
                    Object[] next = iterator.next();
                    //6代表ID
                    if(next[6].toString().equals(excludeCategoryId.toString())) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        //TODO 将productRadarAddResult转换成对应数据结构
        return null;
    }
}
