package com.comall.songshu.service.member;

import com.comall.songshu.repository.member.ChannelRegisterMemberRepository;
import com.comall.songshu.web.rest.util.TransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * 用户注册数据
 *
 * @author liushengling
 * @create 2017-05-04-16:38
 **/
@Service
public class ChannelRegisterMemberService {

    @Autowired
    private ChannelRegisterMemberRepository channelRegisterMemberRepository;


    public String getChannelMemberRegisterCount(String target, String platformName, Timestamp beginTime, Timestamp endTime, Integer topCount) {


        int platform = TransferUtil.getPlatform(platformName);
        List<Object[] > channelMemberRegisterCountResult;

        if (platform<0){//全部
            channelMemberRegisterCountResult = channelRegisterMemberRepository.getChannelMemberRegisterCountAllPlatform(beginTime,endTime,topCount);
        }else {
            channelMemberRegisterCountResult = channelRegisterMemberRepository.getChannelMemberRegisterCountSinglePlatform(beginTime,endTime,platform,topCount);
        }

        List<Object[] > channelMemberRegisterCount = Optional.ofNullable(channelMemberRegisterCountResult).orElse(null);
        //TODO 返回数据拼装
        return channelMemberRegisterCount.toString();
    }
}
