package com.comall.songshu.service.channel;

import com.comall.songshu.constants.CommonConstants;
import com.comall.songshu.repository.channel.VisitTimeDistributionRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

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

        List<Object[]> visitTimeDistribution;
        List<Object[]> visitTimeDistributionResult = new LinkedList<>();

        if (platform<0) {
            visitTimeDistribution = visitTimeDistributionRepository.getVisitTimeDistributionWithAllPlatform(beginTime,endTime);
        }else{
            visitTimeDistribution = visitTimeDistributionRepository.getVisitTimeDistributionSinglePlatform(beginTime,endTime,platformName);
        }

        Integer levelOne = 0;
        Integer levelTwo = 0;
        Integer levelThree = 0;
        Integer levelFour = 0;
        Integer levelFive = 0;
        Integer levelSix = 0;

        if(visitTimeDistribution != null){
            for (Object[] item: visitTimeDistribution) {
                switch (item[0].toString()) {
                    case CommonConstants.VISIT_TIME_LEVEL_ONE :
                        levelOne = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_TIME_LEVEL_TWO :
                        levelTwo = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_TIME_LEVEL_THREE :
                        levelThree = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_TIME_LEVEL_FOUR :
                        levelFour = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_TIME_LEVEL_FIVE :
                        levelFive = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_TIME_LEVEL_SIX :
                        levelSix = new Integer(item[1].toString());
                        break;
                }
            }
        }

        visitTimeDistributionResult.add(new Object[]{CommonConstants.VISIT_TIME_LEVEL_ONE,levelOne});
        visitTimeDistributionResult.add(new Object[]{CommonConstants.VISIT_TIME_LEVEL_TWO,levelTwo});
        visitTimeDistributionResult.add(new Object[]{CommonConstants.VISIT_TIME_LEVEL_THREE,levelThree});
        visitTimeDistributionResult.add(new Object[]{CommonConstants.VISIT_TIME_LEVEL_FOUR,levelFour});
        visitTimeDistributionResult.add(new Object[]{CommonConstants.VISIT_TIME_LEVEL_FIVE,levelFive});
        visitTimeDistributionResult.add(new Object[]{CommonConstants.VISIT_TIME_LEVEL_SIX,levelSix});

        return JsonStringBuilder.buildRankJsonString(visitTimeDistributionResult,"访问时长分布");
    }

}
