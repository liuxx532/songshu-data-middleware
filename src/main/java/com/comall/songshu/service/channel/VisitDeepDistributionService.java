package com.comall.songshu.service.channel;

import com.comall.songshu.repository.channel.VisitDeepDistributionRepository;
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

        List<Object[]> visitDeep = null;

        if (platform<0) {
            visitDeep = visitDeepDistributionRepository.getVisitDeepWithAllPlatform(beginTime,endTime);
        }else{
            visitDeep = visitDeepDistributionRepository.getVisitDeepSinglePlatform(beginTime,endTime,platform);
        }

        //TODO 组装数据
        return null;
    }
}
