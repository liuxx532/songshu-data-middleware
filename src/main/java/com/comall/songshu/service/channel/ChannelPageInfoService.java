package com.comall.songshu.service.channel;

import com.comall.songshu.constants.CommonConstants;
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

        //最终返回值
        List<Object[]> channelPageInfoResult = new ArrayList<>();
        //app返回值（ios和android）
        List<Object[]> channelPageInfoAppResult = null;
        //非app返回值（微信和wap）
        List<Object[]> channelPageInfoWebResult = null;

        if (platform<0) {
            channelPageInfoAppResult = channelPageInfoRepository.getChannelPageInfoWithAllAppPlatform(beginTime,endTime);
            channelPageInfoWebResult = channelPageInfoRepository.getChannelRegisterInfoWithAllWebPlatform(beginTime,endTime);
        }else if(platform == TransferUtil.CHANNEL_ANDROID || platform == TransferUtil.CHANNEL_IOS){
            channelPageInfoAppResult = channelPageInfoRepository.getChannelPageInfoWithSingleAppPlatform(beginTime,endTime,os,platform);
        }else if(platform == TransferUtil.CHANNEL_WAP || platform == TransferUtil.CHANNEL_WECHAT){
            channelPageInfoWebResult = channelPageInfoRepository.getChannelRegisterInfoWithSingleWebPlatform(beginTime,endTime,platform);
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

        if(Optional.ofNullable(channelPageInfoAppResult)
            .filter(c -> c.size()>0).isPresent()){
            channelPageInfoResult.addAll(channelPageInfoAppResult);
        }

        if(Optional.ofNullable(channelPageInfoWebResult)
            .filter(c -> c.size()>0).isPresent()){
            channelPageInfoResult.addAll(channelPageInfoWebResult);
        }

        if(Optional.ofNullable(channelPageInfoResult)
            .filter(c -> c.size()>0).isPresent()){
            for(Object[] o : channelPageInfoResult){
                String channelName = (String) o[0];
                if(CommonConstants.channelFilter.contains(channelName)){
                    continue;
                }
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
