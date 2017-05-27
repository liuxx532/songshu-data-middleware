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

        int platform = TransferUtil.getPlatform(platformName);
        String os = TransferUtil.getSensorsOS(platform);

        //最终返回值
        List<Object[]> channelPageInfoResult = new ArrayList<>();
        //app返回值（ios和android）
        List<Object[]> channelPageInfoAppResult = null;
        //非app返回值（微信和wap）
        List<Object[]> channelPageInfoWebResult = null;

        List<Object[]> titleList = new LinkedList<>();
        List<Object[]> valueList = new LinkedList<>();

        if (platform<0) {
            channelPageInfoAppResult = channelPageInfoRepository.getChannelPageInfoWithAllAppPlatform(beginTime,endTime);
            channelPageInfoWebResult = channelPageInfoRepository.getChannelRegisterInfoWithAllWebPlatform(beginTime,endTime);
        }else if(platform == TransferUtil.CHANNEL_ANDROID || platform == TransferUtil.CHANNEL_IOS){
            channelPageInfoAppResult = channelPageInfoRepository.getChannelPageInfoWithSingleAppPlatform(beginTime,endTime,os,platform);
        }else if(platform == TransferUtil.CHANNEL_WAP || platform == TransferUtil.CHANNEL_WECHAT){
            channelPageInfoWebResult = channelPageInfoRepository.getChannelRegisterInfoWithSingleWebPlatform(beginTime,endTime,platform);
        }


        if(channelPageInfoAppResult != null && channelPageInfoAppResult.size() >0){
            channelPageInfoResult.addAll(channelPageInfoAppResult);
        }

        if(channelPageInfoWebResult != null && channelPageInfoWebResult.size() >0){
            channelPageInfoResult.addAll(channelPageInfoWebResult);
        }

        if(channelPageInfoResult.size()>0){
            for(Object[] o : channelPageInfoResult){
                String channelName = (String) o[0];
                if(CommonConstants.channelFilter.contains(channelName)){
                    continue;
                }
                valueList.add(new Object[]{o[0],o[1],o[2]});
            }
        }

        //表头信息
        titleList.add(new Object[]{TitleConstants.CHANNEL_NAME,"渠道名称"});
        titleList.add(new Object[]{TitleConstants.INSTALL_COUNT,"下载量"});
        titleList.add(new Object[]{TitleConstants.REGISTER_COUNT,"注册用户数"});

        return JsonStringBuilder.buildTableJsonString(valueList,titleList,target);
    }
}
