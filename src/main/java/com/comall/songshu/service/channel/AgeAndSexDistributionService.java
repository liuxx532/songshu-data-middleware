package com.comall.songshu.service.channel;

import com.comall.songshu.constants.CommonConstants;
import com.comall.songshu.repository.channel.AgeAndSexDistributionRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
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
        List<Object[]> sexGroupList = new LinkedList<>();
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
                        maleCount = maleCount.add(count);
                    }else {
                        femaleCount = femaleCount.add(count);
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


                sexGroupList.add(new Object[]{CommonConstants.SEX_MALE,maleCount.intValue()});
                sexGroupList.add(new Object[]{CommonConstants.SEX_FEMALE,femaleCount.intValue()});

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return JsonStringBuilder.buildPieJsonString(sexGroupList,"性别分布");
    }

    public String getAgeDistribution(String target, String platformName, Timestamp beginTime, Timestamp endTime){

        int platform = TransferUtil.getPlatform(platformName);
        List<Object[]> ageDistributionResult;
        if (platform<0) {
            ageDistributionResult = ageAndSexDistributionRepository.getAgeDistributionWithAllPlatform(beginTime,endTime);
        }else{
            ageDistributionResult = ageAndSexDistributionRepository.getAgeDistributionSinglePlatform(beginTime,endTime,platform );
        }

        List<Object[]> ageGroupList = new LinkedList<>();

        if(Optional.ofNullable(ageDistributionResult)
            .filter(l -> l.size() >0).isPresent()){
            BigDecimal totalCount = BigDecimal.ZERO;

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
                    ageGroupList.add(new Object[]{ageGroup,count.intValue()});
                }
            }
        }

        return JsonStringBuilder.buildPieJsonString(ageGroupList,"年龄分布");
    }



}
