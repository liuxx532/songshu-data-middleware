package com.comall.songshu.service.channel;

import com.comall.songshu.constants.TitleConstants;
import com.comall.songshu.repository.channel.ChannelPageInfoRepository;
import com.comall.songshu.web.rest.util.JsonStringBuilder;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 渠道分页信息
 *
 * @author liushengling
 * @create 2017-05-09-10:00
 **/
@Service
public class ChannelPageInfoService {

    @Autowired
    private ChannelPageInfoRepository channelPageInfoRepository;


    public String getChannelPageInfo(String target, String platformName, Timestamp beginTime, Timestamp endTime){
        long currentMills = System.currentTimeMillis();
        int platform = TransferUtil.getPlatform(platformName);
        String os = TransferUtil.getSensorsOS(platform);
        JSONArray channelPageInfoArray =  new JSONArray();
        Integer channelInstallCount;
        List<Object[]> channelPageInfoResult;


        if (platform<0) {
            channelInstallCount = channelPageInfoRepository.getChannelInstallInfoWithAllPlatform(beginTime,endTime);
        }else{
            channelInstallCount = channelPageInfoRepository.getChannelInstallInfoWithSinglePlatform(beginTime,endTime,os);

        }

        if(Optional.ofNullable(channelInstallCount)
            .filter(c -> c>0).isPresent()){
            if (platform<0) {
                channelPageInfoResult = channelPageInfoRepository.getChannelPageInfoWithAllPlatform(beginTime,endTime);
            }else{
                channelPageInfoResult = channelPageInfoRepository.getChannelPageInfoWithSinglePlatform(beginTime,endTime,os,platform);
            }

        }else{
            if (platform<0) {
                channelPageInfoResult = channelPageInfoRepository.getChannelRegisterInfoWithAllPlatform(beginTime,endTime);
            }else{
                channelPageInfoResult = channelPageInfoRepository.getChannelRegisterInfoWithSinglePlatform(beginTime,endTime,platform);
            }
        }


        //封装表头
        JSONArray channelPageInfoTitleArray =  new JSONArray();
        Map<String,String> channelPageInfoTitleMap = new LinkedHashMap<>();
        channelPageInfoTitleMap.put(TitleConstants.CHANNEL_NAME,"渠道名称");
        channelPageInfoTitleMap.put(TitleConstants.INSTALL_COUNT,"下载量");
        channelPageInfoTitleMap.put(TitleConstants.REGISTER_COUNT,"注册用户数");
        channelPageInfoTitleArray.put(channelPageInfoTitleMap);
        channelPageInfoTitleArray.put(currentMills);

        JSONArray dataPointsArray =  new JSONArray();
        JSONArray channelPageInfoDataPointArray =  new JSONArray();
        List<JSONObject> channelPageInfoList = new LinkedList<>();

        if(Optional.ofNullable(channelPageInfoResult)
            .filter(c -> c.size()>0).isPresent()){
            for(Object[] o : channelPageInfoResult){
                JSONObject channelPageInfo;
                try {
                    channelPageInfo = new JSONObject();
                    channelPageInfo.put(TitleConstants.CHANNEL_NAME,o[0]);
                    channelPageInfo.put(TitleConstants.INSTALL_COUNT,o[1]);
                    channelPageInfo.put(TitleConstants.REGISTER_COUNT,o[2]);
                    channelPageInfoList.add(channelPageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        channelPageInfoDataPointArray.put(channelPageInfoList);
        channelPageInfoDataPointArray.put(currentMills);
        dataPointsArray.put(channelPageInfoDataPointArray);
        dataPointsArray.put(channelPageInfoTitleArray);
        long end = System.currentTimeMillis();

        System.out.println("===总耗时==="+(end-currentMills));
        return JsonStringBuilder.buildCommonJsonString(target,dataPointsArray,"");
    }
}
