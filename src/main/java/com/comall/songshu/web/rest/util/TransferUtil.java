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
     * 数据库：如果是4代表其他，-1代表全部
     */
  public static Integer getPlatform(String platName){
        switch (platName){
            case "wechat"://微信
                return 3;
            case "andriod":
                return 1;
            case "ios":
                return 2;
            case "others":
                return 4 ;
            case "wap":
                return 5;
            case "all":
                return 0;
            default:
                return -1;//异常

        }
  }

}
