package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.VisitTimeDistributionRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 访问时长分布Service
 * Created by huanghaizhou on 2017/5/8.
 */
@Service
public class VisitTimeDistributionService {

    @Autowired
    private VisitTimeDistributionRepository visitTimeDistributionRepository;

    public String getVisitTimeDistribution(String target, String platformName, Timestamp beginTime, Timestamp endTime){

        int platform = TransferUtil.getPlatform(platformName);

        if (platform<0) {
            visitTimeDistributionRepository.getVisitTimeDistributionWithAllPlatform(beginTime,endTime);
        }else{

        }

        return null;
    }
}
