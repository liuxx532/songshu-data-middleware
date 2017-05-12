package com.comall.songshu.web.rest.util;

import java.sql.Timestamp;

/**
 * Created by lgx on 17/4/11.
 */
public  class TransferUtil {

    /**
     * 安卓
     */
    public static int CHANNEL_ANDROID = 1;
    /**
     * IOS
     */
    public static int CHANNEL_IOS = 2;
    /**
     * 微信
     */
    public static int CHANNEL_WECHAT = 3;
    /**
     * 移动端
     */
    public static int CHANNEL_WAP = 5;
    /**
     * 其他
     */
    public static int CHANNEL_OTHERS = 0;

    /**
     *
     * @param platName
     * @return
     *
     * 前台：微信，安卓，IOS，其他，WAP，全部
     * 1:安卓 2:IOS 3:微信 5:WAP 0:其他 -1:全部
     */
  public static Integer getPlatform(String platName){
        switch (platName){
            case "android":
                return CHANNEL_ANDROID;
            case "ios":
                return CHANNEL_IOS;
            case "weixin":
                return CHANNEL_WECHAT;
            case "wap":
                return CHANNEL_WAP;
            case "-":
                return CHANNEL_OTHERS ;
            default:
                return -1;

        }

  }

    /**
     *
     * @param channelId
     * @return
     *
     * 前台：微信，安卓，IOS，其他，WAP，全部
     * 1:安卓 2:IOS 3:微信 5:WAP 0:其他 -1:全部
     */
    public static String getPlatFormName(Integer channelId){
        String platFormName = "";
        if(channelId != null){
            if(channelId == CHANNEL_ANDROID){
                platFormName = "安卓";
            }else if(channelId == CHANNEL_IOS){
                platFormName = "IOS";
            }else if(channelId == CHANNEL_WECHAT){
                platFormName = "微信";
            }else if(channelId == CHANNEL_WAP){
                platFormName = "WAP";
            }else if(channelId == CHANNEL_OTHERS){
                platFormName = "其他";
            }
        }
        return platFormName;
    }


    /**
     * 根据平台id获取神策os值
     * @param platForm
     * @return
     */
    public static String getSensorsOS(Integer platForm){
        String sensorsOS = null;
        if(platForm != null){
            if(platForm == CHANNEL_ANDROID){
                sensorsOS = "Android";
            }else if(platForm == CHANNEL_IOS){
                sensorsOS = "iOS";
            }else if(platForm == CHANNEL_WECHAT){
                sensorsOS = "weixin";
            }else if(platForm == CHANNEL_WAP){
                sensorsOS = "wap";
            }
        }
        return sensorsOS;
    }
}
