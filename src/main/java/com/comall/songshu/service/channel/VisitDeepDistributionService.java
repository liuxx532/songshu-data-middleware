package com.comall.songshu.service.channel;

import com.comall.songshu.constants.CommonConstants;
import com.comall.songshu.repository.channel.VisitDeepDistributionRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Object[]> visitDeepDistributionResult = new ArrayList<>();

        if (platform<0) {
            visitDeepDistribution = visitDeepDistributionRepository.getVisitDeepWithAllPlatform(beginTime,endTime);
        }else{
            visitDeepDistribution = visitDeepDistributionRepository.getVisitDeepSinglePlatform(beginTime,endTime,platformName);
        }

        Set allTitle = new HashSet<>();
        allTitle.add(CommonConstants.VISIT_DEEP_LEVEL_ONE);
        allTitle.add(CommonConstants.VISIT_DEEP_LEVEL_TWO);
        allTitle.add(CommonConstants.VISIT_DEEP_LEVEL_THREE);
        allTitle.add(CommonConstants.VISIT_DEEP_LEVEL_FOUR);
        allTitle.add(CommonConstants.VISIT_DEEP_LEVEL_FIVE);
        allTitle.add(CommonConstants.VISIT_DEEP_LEVEL_SIX);

        Integer levelOne = null;
        Integer levelTwo = null;
        Integer levelThree = null;
        Integer levelFour = null;
        Integer levelFive = null;
        Integer levelSix = null;

//        List<Integer> levelList = new ArrayList<>();

        if(visitDeepDistribution != null){
            for (Object[] item: visitDeepDistribution) {
                switch (item[0].toString()) {
                    case CommonConstants.VISIT_DEEP_LEVEL_ONE :
                        allTitle.remove(CommonConstants.VISIT_DEEP_LEVEL_ONE);
                        levelOne = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_DEEP_LEVEL_TWO :
                        allTitle.remove(CommonConstants.VISIT_DEEP_LEVEL_TWO);
                        levelTwo = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_DEEP_LEVEL_THREE :
                        allTitle.remove(CommonConstants.VISIT_DEEP_LEVEL_THREE);
                        levelThree = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_DEEP_LEVEL_FOUR :
                        allTitle.remove(CommonConstants.VISIT_DEEP_LEVEL_FOUR);
                        levelFour = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_DEEP_LEVEL_FIVE :
                        allTitle.remove(CommonConstants.VISIT_DEEP_LEVEL_FIVE);
                        levelFive = new Integer(item[1].toString());
                        break;
                    case CommonConstants.VISIT_DEEP_LEVEL_SIX :
                        allTitle.remove(CommonConstants.VISIT_DEEP_LEVEL_SIX);
                        levelSix = new Integer(item[1].toString());
                        break;
                }
            }
        }
        if(levelOne == null) levelOne = 0;
        if(levelTwo == null) levelTwo = 0;
        if(levelThree == null) levelThree = 0;
        if(levelFour == null) levelFour = 0;
        if(levelFive == null) levelFive = 0;
        if(levelSix == null) levelSix = 0;

        Object[] oOne = new Object[2];
        oOne[0] = CommonConstants.VISIT_DEEP_LEVEL_ONE;
        oOne[1] = levelOne;
        Object[] oTwo = new Object[2];
        oTwo[0] = CommonConstants.VISIT_DEEP_LEVEL_TWO;
        oTwo[1] = levelTwo;
        Object[] oThree = new Object[2];
        oThree[0] = CommonConstants.VISIT_DEEP_LEVEL_THREE;
        oThree[1] = levelThree;
        Object[] oFour = new Object[2];
        oFour[0] = CommonConstants.VISIT_DEEP_LEVEL_FOUR;
        oFour[1] = levelFour;
        Object[] oFive = new Object[2];
        oFive[0] = CommonConstants.VISIT_DEEP_LEVEL_FIVE;
        oFive[1] = levelFive;
        Object[] oSix = new Object[2];
        oSix[0] = CommonConstants.VISIT_DEEP_LEVEL_SIX;
        oSix[1] = levelSix;

        visitDeepDistributionResult.add(oOne);
        visitDeepDistributionResult.add(oTwo);
        visitDeepDistributionResult.add(oThree);
        visitDeepDistributionResult.add(oFour);
        visitDeepDistributionResult.add(oFive);
        visitDeepDistributionResult.add(oSix);

        return JsonStringBuilder.buildRankJsonString(visitDeepDistributionResult);
    }
}
