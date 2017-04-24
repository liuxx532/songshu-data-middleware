package com.comall.songshu.web.rest.util;

import java.sql.Timestamp;

/**
 * Created by lgx on 17/4/11.
 */
public  class TransferUtil {

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
            case "andriod":
                return 1;
            case "ios":
                return 2;
            case "wechat":
                return 3;
            case "wap":
                return 5;
            case "others":
                return 0 ;
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
    public static String getPlatformName(Integer channelId){
        switch (channelId){
            case 1:
                return "andriod";
            case 2:
                return "ios";
            case 3:
                return "wechat";
            case 5:
                return "wap";
            case 0:
                return "others";
            default:
                return "";

        }

    }

}
