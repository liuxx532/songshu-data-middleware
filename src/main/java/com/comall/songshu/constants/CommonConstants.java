package com.comall.songshu.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用变量
 *
 * @author liushengling
 * @create 2017-05-08-15:43
 **/
public class CommonConstants {

    /**
     * 图片域名
     */
    public static String picUrl = "http://pic10.cdn.3songshu.com:81/assets/upload";

    /**sex性别*/
    /**
     * 男
     */
    public static  String SEX_MALE = "男";

    /**
     * 女
     */
    public static  String SEX_FEMALE = "女";


    /**ageGroup性别组*/
    /**
     * 0-20岁
     */
    public static  String AGE_GROUP_LEVEL_ONE = "0-20岁";
    /**
     * 21-25岁
     */
    public static  String AGE_GROUP_LEVEL_TWO = "21-25岁";
    /**
     * 26-30岁
     */
    public static  String AGE_GROUP_LEVEL_THREE = "26-30岁";
    /**
     * 31-35岁
     */
    public static  String AGE_GROUP_LEVEL_FOUR = "31-35岁";
    /**
     * 36岁+
     */
    public static  String AGE_GROUP_LEVEL_FIVE = "36岁+";

    /**访问深度*/
    /**
     * 访问深度1
     */
    public static final String VISIT_DEEP_LEVEL_ONE = "1";
    /**
     * 访问深度2-5
     */
    public static final String VISIT_DEEP_LEVEL_TWO = "2-5";
    /**
     * 访问深度6-10
     */
    public static final String VISIT_DEEP_LEVEL_THREE = "6-10";
    /**
     * 访问深度11-30
     */
    public static final String VISIT_DEEP_LEVEL_FOUR  = "11-30";
    /**
     * 访问深度31-40
     */
    public static final String VISIT_DEEP_LEVEL_FIVE  = "31-40";
    /**
     * 访问深度>41
     */
    public static final String VISIT_DEEP_LEVEL_SIX  = ">41";


    /**
     * 无用渠道过滤
     */
    public static List<String> channelFilter = new ArrayList<>();
    static {
        channelFilter.add("TEST");
        channelFilter.add("CHANNEL_10");
        channelFilter.add("NEWTEST");
        channelFilter.add("PREPRODUCTION");
        channelFilter.add("PRODUCTION");
        channelFilter.add("PRODUCTIONDEBUG");
        channelFilter.add("WH-TSD001");
        channelFilter.add("WH-TSD002");
        channelFilter.add("WH-TSD003");

    }

}
