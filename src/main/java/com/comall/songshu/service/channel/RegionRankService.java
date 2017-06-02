package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.RegionRankRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by huanghaizhou on 2017/5/8.
 */
@Service
public class RegionRankService {

    @Autowired
    private RegionRankRepository regionRankRepository;

    public String getRegionRank(String target, String platformName, Timestamp beginTime, Timestamp endTime,Integer topCount){

        int platform = TransferUtil.getPlatform(platformName);

        List<Object[]> regionRank;

        if (platform<0) {
            regionRank = regionRankRepository.getRegionRankWithAllPlatform(beginTime,endTime,topCount);
        }else{
            regionRank = regionRankRepository.getRegionRankWithSinglePlatform(beginTime,endTime,platformName,topCount);
        }

        return JsonStringBuilder.buildHistogramPanelJsonString(regionRank,"地区分布",true);
    }
}
