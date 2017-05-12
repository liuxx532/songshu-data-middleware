package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.ManufacturerRankRepository;
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
public class ManufacturerRankService {

    @Autowired
    private ManufacturerRankRepository manufacturerRankRepository;

    public String getManufacturerRank(String target, String platformName, Timestamp beginTime, Timestamp endTime){

        int platform = TransferUtil.getPlatform(platformName);

        List<Object[]> manufacturerRank = null;

        if (platform<0) {
            manufacturerRank = manufacturerRankRepository.getManufacturerRankWithAllPlatform(beginTime,endTime);
        }else{
            manufacturerRank = manufacturerRankRepository.getManufacturerRankWithSinglePlatform(beginTime,endTime,platformName);
        }

        return JsonStringBuilder.buildRankJsonString(manufacturerRank);
    }
}
