package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.VisitDeepDistributionRepository;
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
public class VisitDeepDistributionService {

    @Autowired
    private VisitDeepDistributionRepository visitDeepDistributionRepository;

    public String getVisitDeepDistribution(String target, String platformName, Timestamp beginTime, Timestamp endTime){

        int platform = TransferUtil.getPlatform(platformName);

        List<Object[]> visitDeepDistribution = null;

        if (platform<0) {
            visitDeepDistribution = visitDeepDistributionRepository.getVisitDeepWithAllPlatform(beginTime,endTime);
        }else{
            visitDeepDistribution = visitDeepDistributionRepository.getVisitDeepSinglePlatform(beginTime,endTime,platform);
        }

        return JsonStringBuilder.buildRankJsonString(visitDeepDistribution);
    }
}
