package com.comall.songshu.service.product;

import com.comall.songshu.repository.product.ProductRevenueRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * 商品销售
 *
 * @author liushengling
 * @create 2017-05-02-18:18
 **/
public class ProductRevenueService {

    @Autowired
    private ProductRevenueRepository productRevenueRepository;

    public String getProductRevenue(String target, String platformName, Timestamp beginTime, Timestamp endTime, Timestamp chainBeginTime, Timestamp chainEndTime,Integer topCount) {


        int platform = TransferUtil.getPlatform(platformName);
        List<Object[] > productRevenueResult;

        if (platform<0){//全部
            productRevenueResult = productRevenueRepository.getProductRevenueAllPlatform(beginTime,endTime,topCount);
        }else {
            productRevenueResult = productRevenueRepository.getProductRevenueSinglePlatform(beginTime,endTime,platform,topCount);
        }

        List<Object[] > productRevenue = Optional.ofNullable(productRevenueResult).orElse(null);

        return productRevenue.toString();
    }
}
