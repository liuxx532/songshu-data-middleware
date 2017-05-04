package com.comall.songshu.web.rest.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.util.Optional;

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


    /**
     * 获取本地时区
     * @param dateTime 不能为null 不能为""
     * @return
     */
    public String formatDateTime(String dateTime) {
        return  Optional.ofNullable(dateTime)
            .map(String::trim)
            .filter( s -> s.length() > 0)
            .map( t -> new DateTime(t))
            .map( d -> d.getMillis())
            .map( m -> new DateTime(m).withZone(DateTimeZone.forID("+00")).toString())
            .orElse(null);
    }

    /**
     * 根据时间区间，获取环比的开始时间
     * @param beginTime 2017-11-11 00:00:00 不能为空 null ""
     * @param endTime 2017-11-11 00:00:00 不能为空 null ""
     * @return
     */
    public String[] getChainIndexDateTime(String beginTime, String endTime) {

        Long bTime = Optional.ofNullable(beginTime)
            .map(String::trim)
            .filter( s -> s.length() >0)
            .map( t -> new DateTime(t))
            .map( d -> d.getMillis())
            .orElseThrow(() ->  new IllegalArgumentException("Invalid datetime input string beginTime: " + beginTime ));

        Long eTime = Optional.ofNullable(endTime)
            .map(String::trim)
            .filter( s -> s.length() >0)
            .map( t -> new DateTime(t))
            .map( d -> d.getMillis())
            .orElseThrow(() ->  new IllegalArgumentException("Invalid datetime input string endTime: " + endTime ));

        Long bwTimes = 2 * bTime - eTime;
        String endTime2 = endTime;

        String chainIndexStartTime = null;
        String chainIndexEndTime = null;
        String eEndWith2 = null;

        final String bEndWith = beginTime.substring(8, 19);
        final String eEndWith = endTime.substring(11, 19);

        if ("23:59:59" == eEndWith) {
            eTime += 1000;
            endTime2 = new DateTime(eTime).withZone(DateTimeZone.forID("+00")).toString();
            eEndWith2 = endTime2.substring(8, 19);
        }

        //如果时间差超过27天，并且时分秒是00:00:00 或 23:59:59   2017-11-11 00:00:00
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
                chainIndexStartTime = year + "-0" + mon + "-01T00:00:00";
            } else {
                chainIndexStartTime = year + "-" + mon + "-01T00:00:00";
            }
            chainIndexEndTime = new DateTime(bTime - 1000).withZone(DateTimeZone.forID("+00")).toString();
        } else {
            //如果不超过27天,直接按时间做减法
            chainIndexStartTime = new DateTime(bwTimes).withZone(DateTimeZone.forID("+00")).toString();
        }
        if (null == chainIndexEndTime)
            chainIndexEndTime = beginTime;

        return new String[] {chainIndexStartTime, chainIndexEndTime};
    }

    /**
     * 计算环比的两个开始时间的差值
     * @param beginTime
     * @param endTime
     * @return
     */
    public long getChainIndexDateTimeMillis(String beginTime, String endTime) {

        String[] timeArr = getChainIndexDateTime(beginTime, endTime);
        Long chainIndexTime =  Optional.ofNullable(timeArr)
            .filter( array -> array.length >0)
            .map( a -> new DateTime(a).getMillis())
            .orElse(null);
        Long bTime = Optional.ofNullable(beginTime)
            .map(String::trim)
            .map( t -> new DateTime(t))
            .map( d -> d.getMillis())
            .orElse(null);

        if(Optional.ofNullable(chainIndexTime).isPresent()
            && Optional.ofNullable(chainIndexTime).isPresent()){
            return bTime - chainIndexTime;
        }
        return 0;
    }


    /**
     * 聚合的时间范围（趋势图时间间隔，时间单位为秒）
     * @param beginTime 不能为null
     * @param endTime 不能为null
     * @param aggCount 大于0
     * @return 如果参数错误返回0
     */
    public  Integer getAggTimeValue(Timestamp beginTime, Timestamp endTime,Integer aggCount) {
        Integer interval = 0;
        if(Optional.ofNullable(beginTime).isPresent()
            && Optional.ofNullable(endTime).isPresent() ){
            long bTime = beginTime.getTime();
            long eTime = endTime.getTime();
            interval = Optional.ofNullable(aggCount)
                .filter( agg -> agg >0)
                .map( l -> (eTime - bTime) / aggCount / 1000)
                .filter( i -> i > 0)
                .map( v -> v.intValue())
                .orElse(0);
        }
        return interval;
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


//    public static void main(String[] a12){
//        Date now = new Date();
//        Timestamp beginTime = new Timestamp(now.getTime());
//        Timestamp endTime = new Timestamp(now.getTime()+6000);
//        Integer count = ServiceUtil.getInstance().getAggTimeValue(beginTime,endTime,2);
//        System.out.println(count);
//
//        String zone = ServiceUtil.getInstance().formatDateTime("   ");
//        System.out.println(zone);
//
//        long l = ServiceUtil.getInstance().getChainIndexDateTimeMillis("2017-01-31 00:00:00","");
//        System.out.println(l);
//    }

}
