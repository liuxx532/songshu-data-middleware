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
import java.math.BigInteger;
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
            BigDecimal totalCount = BigDecimal.ZERO;
            BigDecimal maleCount = BigDecimal.ZERO;
            BigDecimal femaleCount = BigDecimal.ZERO;

            for(Object[] o :sexDistributionResult){
                String sex = Optional.ofNullable(o[0])
                            .map(value -> (String)value)
                            .map(String::trim)
                            .filter(s -> s.length()>0)
                            .orElse(null);
                BigDecimal count = Optional.ofNullable(o[1])
                            .map(obj -> obj.toString())
                            .map(BigDecimal::new)
                            .orElse(BigDecimal.ZERO);

                if(sex != null){
                    totalCount =  totalCount.add(count);
                    if(sex.equalsIgnoreCase(CommonConstants.SEX_MALE)){
                        maleCount = count;
                    }else {
                        femaleCount = count;
                    }
                }

            }

            Double malePercentage = 0.0;
            Double femalePercentage = 0.0;
            if(totalCount.doubleValue() >0){
                malePercentage = maleCount.doubleValue()/totalCount.doubleValue();
                femalePercentage = femaleCount.doubleValue()/totalCount.doubleValue();
            }
            try {
                JSONObject maleObject = new JSONObject();
                maleObject.put(CommonConstants.SEX_MALE,malePercentage);
                JSONObject femaleObject = new JSONObject();
                femaleObject.put(CommonConstants.SEX_FEMALE,femalePercentage);
                sexDistributionArray.put(maleObject);
                sexDistributionArray.put(femaleObject);
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
            BigDecimal totalCount = BigDecimal.ZERO;
            BigDecimal ageGroupOneCount = BigDecimal.ZERO;
            BigDecimal ageGroupTwoCount = BigDecimal.ZERO;
            BigDecimal ageGroupThreeCount = BigDecimal.ZERO;
            BigDecimal ageGroupFourCount = BigDecimal.ZERO;
            BigDecimal ageGroupFiveCount = BigDecimal.ZERO;


            for(Object[] o : ageDistributionResult){
                String ageGroup = Optional.ofNullable(o[0])
                    .map(value -> (String)value)
                    .map(String::trim)
                    .filter(s -> s.length()>0)
                    .orElse(null);
                BigDecimal count = Optional.ofNullable(o[1])
                    .map(obj -> obj.toString())
                    .map(BigDecimal::new)
                    .orElse(BigDecimal.ZERO);

                if(ageGroup != null){
                    totalCount =  totalCount.add(count);
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
            }

            Double ageGroupOnePercentage = 0.0;
            Double ageGroupTwoPercentage = 0.0;
            Double ageGroupThreePercentage = 0.0;
            Double ageGroupFourPercentage = 0.0;
            Double ageGroupFivePercentage = 0.0;
            if(totalCount.doubleValue() >0){
                ageGroupOnePercentage = ageGroupOneCount.doubleValue()/totalCount.doubleValue();
                ageGroupTwoPercentage = ageGroupTwoCount.doubleValue()/totalCount.doubleValue();
                ageGroupThreePercentage = ageGroupThreeCount.doubleValue()/totalCount.doubleValue();
                ageGroupFourPercentage = ageGroupFourCount.doubleValue()/totalCount.doubleValue();
                ageGroupFivePercentage = ageGroupFiveCount.doubleValue()/totalCount.doubleValue();
            }
            try {
                JSONObject ageGroupOneObject = new JSONObject();
                ageGroupOneObject.put(CommonConstants.AGE_GROUP_LEVEL_ONE,ageGroupOnePercentage);
                JSONObject ageGroupTwoObject = new JSONObject();
                ageGroupTwoObject.put(CommonConstants.AGE_GROUP_LEVEL_TWO,ageGroupTwoPercentage);
                JSONObject ageGroupThreeObject = new JSONObject();
                ageGroupThreeObject.put(CommonConstants.AGE_GROUP_LEVEL_THREE,ageGroupThreePercentage);
                JSONObject ageGroupFourObject = new JSONObject();
                ageGroupFourObject.put(CommonConstants.AGE_GROUP_LEVEL_FOUR,ageGroupFourPercentage);
                JSONObject ageGroupFiveObject = new JSONObject();
                ageGroupFiveObject.put(CommonConstants.AGE_GROUP_LEVEL_FIVE,ageGroupFivePercentage);
                ageDistributionArray.put(ageGroupOneObject);
                ageDistributionArray.put(ageGroupTwoObject);
                ageDistributionArray.put(ageGroupThreeObject);
                ageDistributionArray.put(ageGroupFourObject);
                ageDistributionArray.put(ageGroupFiveObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ageDistributionArray.toString();
    }



}
