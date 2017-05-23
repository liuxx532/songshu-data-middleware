package com.comall.songshu.web.rest.util;

import com.comall.songshu.web.rest.vm.TopStat;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Created by wdc on 2017/4/14.
 */
public class AssembleUtil {

    /**
     * 封装TopStat
     * @param value
     * @param chainValue
     * @return
     */
    public static TopStat assemblerTopStat(Double value, Double chainValue) {

        if (value <=0|| chainValue <=0){
            return new TopStat(value,0.0);

        }else {
            //比例
            double percent =Math.abs(value-chainValue)/chainValue;
            int flag= ServiceUtil.getInstance().getChainIndexFlag(value,chainValue);
            return new TopStat(value, percent, flag);
        }
    }


    /**
     * 用于封装起始时间和环比起始时间
     * @param fromTimeStr
     * @param toTimeStr
     * @return
     */
    public static Map<String,Timestamp> assemblerDateMap(String fromTimeStr, String toTimeStr) {

        //开始时间
        Timestamp beginTime = null;
        //结束时间
        Timestamp endTime = null;
        //环比时间
        Timestamp chainBeginTime = null;
        Timestamp chainEndTime = null;
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        Map<String,Timestamp> dateMap = new HashMap<>();
        if(fromTimeStr != null && toTimeStr != null){
            beginTime = Optional.of(fromTimeStr)
                .map(String::trim)
                .filter(s -> s.length() >0)
                .map(v -> ServiceUtil.getInstance().parseTimestamp(v))
                .orElse(null);
            endTime = Optional.of(toTimeStr)
                .map(String::trim)
                .filter(s -> s.length() >0)
                .map( s -> ServiceUtil.getInstance().parseTimestamp(s))
                .orElse(null);
            //环比时间
            String[] chainCreateTime = ServiceUtil.getInstance().getChainIndexDateTime(fromTimeStr,toTimeStr);
            if(chainCreateTime != null){
                chainBeginTime = Optional.of(chainCreateTime)
                    .filter( array -> array.length >0)
                    .map( a -> a[0])
                    .map(String::trim)
                    .filter(s -> s.length() >0)
                    .map( s -> DateTime.parse(s))
                    .map( d-> Timestamp.valueOf(d.toString(dateTimeFormat)))
                    .orElse(null);
                chainEndTime = Optional.of(chainCreateTime)
                    .filter( array -> array.length >1)
                    .map( a -> a[1])
                    .map(String::trim)
                    .filter(s -> s.length() >0)
                    .map( s -> DateTime.parse(s))
                    .map( d-> Timestamp.valueOf(d.toString(dateTimeFormat)))
                    .orElse(null);
            }
        }
        dateMap.put("beginTime",beginTime);
        dateMap.put("endTime",endTime);
        dateMap.put("chainBeginTime",chainBeginTime);
        dateMap.put("chainEndTime",chainEndTime);
        return dateMap;
    }



}
