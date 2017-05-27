package com.comall.songshu.service.channel;

import com.comall.songshu.constants.CommonConstants;
import com.comall.songshu.repository.channel.VisitDeepDistributionRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by huanghaizhou on 2017/5/8.
 */
@Service
public class VisitDeepDistributionService {

    @Autowired
    private VisitDeepDistributionRepository visitDeepDistributionRepository;

    public String getVisitDeepDistribution(String target, String platformName, Timestamp beginTime, Timestamp endTime){

        int platform = TransferUtil.getPlatform(platformName);

        List<Object[]> visitDeepDistribution;
        List<Object[]> visitDeepDistributionResult = new LinkedList<>();

        if (platform<0) {
            visitDeepDistribution = visitDeepDistributionRepository.getVisitDeepWithAllPlatform(beginTime,endTime);
        }else{
            visitDeepDistribution = visitDeepDistributionRepository.getVisitDeepSinglePlatform(beginTime,endTime,platformName);
        }


        Integer levelOne = 0;
        Integer levelTwo = 0;
        Integer levelThree = 0;
        Integer levelFour = 0;
        Integer levelFive = 0;
        Integer levelSix = 0;

        if(visitDeepDistribution != null){
            for (Object[] item: visitDeepDistribution) {
                switch (item[0].toString()) {
                    case CommonConstants.VISIT_DEEP_LEVEL_ONE :
                        levelOne = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_DEEP_LEVEL_TWO :
                        levelTwo = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_DEEP_LEVEL_THREE :
                        levelThree = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_DEEP_LEVEL_FOUR :
                        levelFour = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_DEEP_LEVEL_FIVE :
                        levelFive = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_DEEP_LEVEL_SIX :
                        levelSix = new Integer(item[1].toString());
                        break;
                }
            }
        }

        visitDeepDistributionResult.add(new Object[]{CommonConstants.VISIT_DEEP_LEVEL_ONE,levelOne});
        visitDeepDistributionResult.add(new Object[]{CommonConstants.VISIT_DEEP_LEVEL_TWO,levelTwo});
        visitDeepDistributionResult.add(new Object[]{CommonConstants.VISIT_DEEP_LEVEL_THREE,levelThree});
        visitDeepDistributionResult.add(new Object[]{CommonConstants.VISIT_DEEP_LEVEL_FOUR,levelFour});
        visitDeepDistributionResult.add(new Object[]{CommonConstants.VISIT_DEEP_LEVEL_FIVE,levelFive});
        visitDeepDistributionResult.add(new Object[]{CommonConstants.VISIT_DEEP_LEVEL_SIX,levelSix});

        return JsonStringBuilder.buildRankJsonString(visitDeepDistributionResult);
    }
}
