package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.ChannelGrossMarginRateRepository;
import com.comall.songshu.repository.index.GrossMarginRateRepository;
import com.comall.songshu.web.rest.util.AssembleUtil;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.ServiceUtil;
import com.comall.songshu.web.rest.util.TransferUtil;
import com.comall.songshu.web.rest.vm.TopStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * 渠道页面毛利率
 * Created by liugaoyu on 2017/4/20.
 */
@Service
public class ChannelGrossMarginRateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelGrossMarginRateService.class);

    @Autowired
    private ChannelGrossMarginRateRepository channelGrossMarginRateRepository;


    public String  getChannelGrossMarginRate(String target,String platformName, Timestamp beginTime,Timestamp endTime,Timestamp chainBeginTime,Timestamp chainEndTime) {

        int platform = TransferUtil.getPlatform(platformName);

        //毛利率 = （销售额 - 商品成本）／ 销售额 * 100%
        //注：这里毛利率先不用乘以100%
        Double grossMarginRateResult;
        Double chainGrossMarginRateResult;

        if (platform<0){
              grossMarginRateResult = channelGrossMarginRateRepository.getChannelCrossMarginWithAllPlatform(beginTime,endTime);
              chainGrossMarginRateResult  = channelGrossMarginRateRepository.getChannelCrossMarginWithAllPlatform(chainBeginTime,chainEndTime);
        }else {
              grossMarginRateResult = channelGrossMarginRateRepository.getChannelCrossMarginWithSinglePlatform(beginTime,endTime,platform);
              chainGrossMarginRateResult  = channelGrossMarginRateRepository.getChannelCrossMarginWithSinglePlatform(chainBeginTime,chainEndTime,platform);
        }

        Double grossMarginRate = Optional.ofNullable(grossMarginRateResult).orElse(0.00);
        Double chainGrossMarginRate = Optional.ofNullable(chainGrossMarginRateResult).orElse(0.00);

        //TopStat
        TopStat result= AssembleUtil.assemblerTopStat(grossMarginRate,chainGrossMarginRate);

        return JsonStringBuilder.buildTargetJsonString(target,result,"");


    }




}
