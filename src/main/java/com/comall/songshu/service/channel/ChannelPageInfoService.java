package com.comall.songshu.service.channel;

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
import java.util.List;
import java.util.Optional;

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

        JSONArray channelPageInfoArray =  new JSONArray();
        List<Object[]> channelDownloadResult;

        if (platform<0) {
            channelDownloadResult = channelPageInfoRepository.getChannelDownLoadCountWithAllPlatform(beginTime,endTime);
        }else{
            channelDownloadResult = channelPageInfoRepository.getChannelDownLoadCountWithSinglePlatform(platform,beginTime,endTime);
        }

        if(Optional.ofNullable(channelDownloadResult)
            .filter(c -> c.size()>0).isPresent()){
            for(Object[] o : channelDownloadResult){
                String channelName = (String) o[0];
                Integer memberRegisterCount = 0;
                Integer downLoadCount = Optional.ofNullable(o[1])
                    .map(obj -> obj.toString())
                    .map(BigDecimal::new)
                    .map(b -> b.intValue())
                    .orElse(0);
                JSONObject channelInfoObject = new JSONObject();
                if(platform<0){
                    memberRegisterCount = channelPageInfoRepository.getChannelMemberRegisterCountWithAllPlatform(beginTime,endTime,channelName);
                }else{
                    memberRegisterCount = channelPageInfoRepository.getChannelMemberRegisterCountWithSinglePlatform(platform,beginTime,endTime,channelName);
                }
                try {
                    channelInfoObject.put("channelName",channelName);
                    channelInfoObject.put("downLoadCount",downLoadCount);
                    channelInfoObject.put("registerCount",memberRegisterCount);
                    channelPageInfoArray.put(channelInfoObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return channelPageInfoArray.toString();
    }
}
