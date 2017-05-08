package com.comall.songshu.service.channel;

import com.comall.songshu.constants.CommonConstants;
import com.comall.songshu.repository.channel.AgeAndSexDistributionRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * 年龄及性别分布
 *
 * @author liushengling
 * @create 2017-05-08-12:16
 **/
@Service
public class AgeAndSexDistributionService {

    @Autowired
    private AgeAndSexDistributionRepository ageAndSexDistributionRepository;

    public String getSexDistribution(String target, String platformName, Timestamp beginTime, Timestamp endTime){

        int platform = TransferUtil.getPlatform(platformName);
        List<Object[]> sexDistributionResult;
        JSONArray sexDistributionArray =  new JSONArray();
        if (platform<0) {
            sexDistributionResult = ageAndSexDistributionRepository.getSexDistributionWithAllPlatform(beginTime,endTime);
        }else{
            sexDistributionResult = ageAndSexDistributionRepository.getSexDistributionSinglePlatform(beginTime,endTime,platform );
        }
        if(Optional.ofNullable(sexDistributionResult)
            .filter(l -> l.size() >0).isPresent()){
            Integer totalCount = 0;
            Integer maleCount = 0;
            Integer femaleCount = 0;

            for(Object[] o :sexDistributionResult){
                String sex = Optional.ofNullable(o[0])
                            .map(value -> (String)value)
                            .map(String::trim)
                            .filter(s -> s.length()>0)
                            .orElse(null);
                Integer count = Optional.ofNullable(o[1])
                                .map(value -> (Integer)value)
                                .orElse(0);
                if(sex != null && count >0){
                    totalCount +=  count;
                    if(sex.equalsIgnoreCase(CommonConstants.SEX_MALE)){
                        maleCount = count;
                    }else {
                        femaleCount = count;
                    }
                }

            }

            Double malePercentage = 0.0;
            Double femalePercentage = 0.0;
            if(totalCount >0){
                malePercentage = new BigDecimal(maleCount).doubleValue()/new BigDecimal(totalCount).doubleValue();
                femalePercentage = new BigDecimal(femaleCount).doubleValue()/new BigDecimal(totalCount).doubleValue();
            }
            try {
                JSONObject maleJson = new JSONObject();
                maleJson.put(CommonConstants.SEX_MALE,malePercentage);
                sexDistributionArray.put(maleJson);
                JSONObject femaleJson = new JSONObject();
                maleJson.put(CommonConstants.SEX_FEMALE,femalePercentage);
                sexDistributionArray.put(femaleJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sexDistributionArray.toString();
    }

    public String getAgeDistribution(String target, String platformName, Timestamp beginTime, Timestamp endTime){

        int platform = TransferUtil.getPlatform(platformName);
        List<Object[]> ageDistributionResult;
        JSONArray ageDistributionArray =  new JSONArray();
        if (platform<0) {
            ageDistributionResult = ageAndSexDistributionRepository.getAgeDistributionWithAllPlatform(beginTime,endTime);
        }else{
            ageDistributionResult = ageAndSexDistributionRepository.getAgeDistributionSinglePlatform(beginTime,endTime,platform );
        }

        if(Optional.ofNullable(ageDistributionResult)
            .filter(l -> l.size() >0).isPresent()){
            Integer totalCount = 0;
            Integer ageGroupOneCount = 0;
            Integer ageGroupTwoCount = 0;
            Integer ageGroupThreeCount = 0;
            Integer ageGroupFourCount = 0;
            Integer ageGroupFiveCount = 0;


            for(Object[] o : ageDistributionResult){
                String ageGroup = Optional.ofNullable(o[0])
                    .map(value -> (String)value)
                    .map(String::trim)
                    .filter(s -> s.length()>0)
                    .orElse(null);
                Integer count = Optional.ofNullable(o[1])
                    .map(value -> (Integer)value)
                    .orElse(0);
                if(ageGroup != null && count >0){
                    totalCount +=  count;
                    if(ageGroup.equalsIgnoreCase(CommonConstants.AGE_GROUP_LEVEL_ONE)){
                        ageGroupOneCount = count;
                    }else if(ageGroup.equalsIgnoreCase(CommonConstants.AGE_GROUP_LEVEL_TWO)){
                        ageGroupTwoCount = count;
                    }else if(ageGroup.equalsIgnoreCase(CommonConstants.AGE_GROUP_LEVEL_THREE)){
                        ageGroupThreeCount = count;
                    }else if(ageGroup.equalsIgnoreCase(CommonConstants.AGE_GROUP_LEVEL_FOUR)){
                        ageGroupFourCount = count;
                    }else if(ageGroup.equalsIgnoreCase(CommonConstants.AGE_GROUP_LEVEL_FIVE)){
                        ageGroupFiveCount = count;
                    }
                }

                Double ageGroupOnePercentage = 0.0;
                Double ageGroupTwoPercentage = 0.0;
                Double ageGroupThreePercentage = 0.0;
                Double ageGroupFourPercentage = 0.0;
                Double ageGroupFivePercentage = 0.0;
                if(totalCount >0){
                    ageGroupOnePercentage = new BigDecimal(ageGroupOneCount).doubleValue()/new BigDecimal(totalCount).doubleValue();
                    ageGroupTwoPercentage = new BigDecimal(ageGroupTwoCount).doubleValue()/new BigDecimal(totalCount).doubleValue();
                    ageGroupThreePercentage = new BigDecimal(ageGroupThreeCount).doubleValue()/new BigDecimal(totalCount).doubleValue();
                    ageGroupFourPercentage = new BigDecimal(ageGroupFourCount).doubleValue()/new BigDecimal(totalCount).doubleValue();
                    ageGroupFivePercentage = new BigDecimal(ageGroupFiveCount).doubleValue()/new BigDecimal(totalCount).doubleValue();
                }
                try {
                    JSONObject ageGroupOneJson = new JSONObject();
                    ageGroupOneJson.put(CommonConstants.AGE_GROUP_LEVEL_ONE,ageGroupOnePercentage);
                    ageDistributionArray.put(ageGroupOneJson);
                    JSONObject ageGroupTwoJson = new JSONObject();
                    ageGroupTwoJson.put(CommonConstants.AGE_GROUP_LEVEL_TWO,ageGroupTwoPercentage);
                    ageDistributionArray.put(ageGroupTwoJson);
                    JSONObject ageGroupThreeJson = new JSONObject();
                    ageGroupThreeJson.put(CommonConstants.AGE_GROUP_LEVEL_THREE,ageGroupThreePercentage);
                    ageDistributionArray.put(ageGroupThreeJson);
                    JSONObject ageGroupFourJson = new JSONObject();
                    ageGroupFourJson.put(CommonConstants.AGE_GROUP_LEVEL_FOUR,ageGroupFourPercentage);
                    ageDistributionArray.put(ageGroupFourJson);
                    JSONObject ageGroupFiveJson = new JSONObject();
                    ageGroupFiveJson.put(CommonConstants.AGE_GROUP_LEVEL_FIVE,ageGroupFivePercentage);
                    ageDistributionArray.put(ageGroupFiveJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return ageDistributionArray.toString();
    }



}
