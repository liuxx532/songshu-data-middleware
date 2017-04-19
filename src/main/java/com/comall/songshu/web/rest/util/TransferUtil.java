package com.comall.songshu.web.rest.util;

/**
 * Created by lgx on 17/4/11.
 */
public  class TransferUtil {

    public static int getGroupType(String pshGroupType){
        if(pshGroupType.equals("singleGroup")) {
            return 1;
        } else if(pshGroupType.equals("multiGroup")) {
            return 0;
        }

        return -1;
    }

    public static String getMidName(String mid){
        String midName = null;
        if(mid.equals("east")) {
            midName = "华东站";
        } else if (mid.equals("south")) {
            midName="华南站";
        }
        else if(mid.equals("north")) {
            midName="华北站";
        }
        return midName;
    }
}
