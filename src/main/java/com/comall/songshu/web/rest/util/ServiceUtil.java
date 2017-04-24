package com.comall.songshu.web.rest.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;

/**
 * Created by liugaoyu on 2017/4/24.
 */
public class ServiceUtil {


    private static final ServiceUtil INSTANCE = new ServiceUtil();

    private ServiceUtil(){
        // Empty
    }

    public static ServiceUtil getInstance() {
        return INSTANCE;
    }

    // 使用本地时区
    public String formatDateTime(String dateTime) {
        return new DateTime(new DateTime(dateTime).getMillis()).withZone(DateTimeZone.forID("+08")).toString();
    }

    //根据时间区间，获取环比的开始时间
    public String[] getChainIndexDateTime(String beginTime, String endTime) {
        if (null == beginTime || null == endTime || "" == beginTime || "" == endTime)
            throw new IllegalArgumentException("Invalid datetime input string: " + beginTime + ", " + endTime);

        final Long bTime = new DateTime(beginTime).getMillis();
        Long eTime = new DateTime(endTime).getMillis();

        final Long bwTimes = 2 * bTime - eTime;
        String endTime2 = endTime;

        String chainIndexStartTime = null;
        String chainIndexEndTime = null;
        String eEndWith2 = null;

        final String bEndWith = beginTime.substring(8, 19);
        final String eEndWith = endTime.substring(11, 19);

        if ("23:59:59" == eEndWith) {
            eTime += 1000;
            endTime2 = new DateTime(eTime).withZone(DateTimeZone.forID("+08")).toString();
            eEndWith2 = endTime2.substring(8, 19);
        }

        //如果时间差超过27天，并且时分秒是00:00:00 或 23:59:59
        if ((eTime - bTime) > (27 * 24 * 3600 * 1000L) && "01T00:00:00" == bEndWith && "01T00:00:00" == eEndWith2) {
            final String bStartWith = beginTime.substring(0, 10);
            final String eStartWith = endTime2.substring(0, 10);

            final String[] bArray = bStartWith.split("-");
            final String[] eArray = eStartWith.split("-");

            int bYear = Integer.valueOf(bArray[0]);
            int eYear = Integer.valueOf(eArray[0]);
            int bMon = Integer.valueOf(bArray[1]);
            int eMon = Integer.valueOf(eArray[1]);
            int cMon = 0;

            if (bYear == eYear) {
                cMon = eMon - bMon;
            } else {
                cMon = (eYear - bYear) * 12 + eMon - bMon;
            }

            int year = bYear - cMon / 12;
            int mon = bMon - cMon % 12;
            if (mon < 1) {
                mon += 12;
                year -= 1;
            }
            if (mon < 10) {
                // 根据数据库中的日期格式做调整
                // TODO
                chainIndexStartTime = year + "-0" + mon + "-01T00:00:00.000+08:00";
            } else {
                chainIndexStartTime = year + "-" + mon + "-01T00:00:00.000+08:00";
            }
            chainIndexEndTime = new DateTime(bTime - 1000).withZone(DateTimeZone.forID("+08")).toString();
        } else {
            //如果不超过27天,直接按时间做减法
            chainIndexStartTime = new DateTime(bwTimes).withZone(DateTimeZone.forID("+08")).toString();
        }
        if (null == chainIndexEndTime)
            chainIndexEndTime = beginTime;

        return new String[] {chainIndexStartTime, chainIndexEndTime};
    }

    //计算环比的两个开始时间的差值
    public long getChainIndexDateTimeMillis(String beginTime, String endTime) {
        String[] timeArr = getChainIndexDateTime(beginTime, endTime);
        long bTime = new DateTime(beginTime).getMillis();
        long chainIndexTime = new DateTime(timeArr[0]).getMillis();

        return bTime - chainIndexTime;
    }

    //聚合的时间范围
    public long getAggTimeValue(String beginTime, String endTime) {
        long bTime = new DateTime(beginTime).getMillis();
        long eTime = new DateTime(endTime).getMillis();

        return (eTime - bTime) / 90;
    }

    public int getChainIndexFlag(double current, double chainIndex) {
        int result = 0;

        if (current > chainIndex) {
            result = 1;
        } else if (current < chainIndex){
            result = -1;
        }

        return result;
    }

    public Timestamp parseTimestamp(String strDatetime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String parsedStrDatetime = DateTime.parse(strDatetime).toString(dateTimeFormatter);
        return Timestamp.valueOf(parsedStrDatetime);
    }

}
