package com.comall.songshu.web.rest.util;

import com.comall.songshu.web.rest.vm.TopStat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import springfox.documentation.spring.web.json.Json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Created by wdc on 2017/4/14.
 */
public class JsonStringBuilder {


    public static String buildCommonJsonString(String target, JSONArray dataPointsArray, String columnName) {
        JSONArray resultArray = new JSONArray();
        try {
            JSONObject result = new JSONObject();
            result.put("target",target);
            result.put("datapoints",dataPointsArray);
            result.put("columnName",columnName);
            resultArray.put(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultArray.toString();
    }



    /**
     * 构建index首页头部指标
     * @param target
     * @param topStat
     * @param columnName
     * @return
     * [{"dataPoints":[[{"firstValue":1,"flag":3,"ringsRation":2},1493775547197]],"target":"aaaa","columnName":""}]
     */
    public static String buildTargetJsonString(String target, TopStat topStat, String columnName) {
        JSONArray resultArray = new JSONArray();
        try {
            JSONObject dataPoint = new JSONObject();
            dataPoint.put("firstValue",topStat.getFirstValue());
            dataPoint.put("ringsRation",topStat.getRingsRation());
            dataPoint.put("flag",topStat.getFlag());

            JSONArray dataPointArray = new JSONArray();
            dataPointArray.put(dataPoint);
            dataPointArray.put(System.currentTimeMillis());

            JSONArray dataPointsArray = new JSONArray();
            dataPointsArray.put(dataPointArray);

            JSONObject result = new JSONObject();
            result.put("target",target);
            result.put("datapoints",dataPointsArray);
            result.put("columnName",columnName);
            resultArray.put(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultArray.toString();
    }

    /**
     * 构建环比数据
     * @param currentTrend
     * @param chainTrend
     * @return
     * [{"dataPoints":[[1,1483200000000],[2,1485878400000]],"target":"当前","columnName":""},{"dataPoints":[[3,1488297600000],[4,1490976000000]],"target":"环比","columnName":""}]
     */
    public static  String buildTrendJsonString(List<Object[]> currentTrend,List<Object[]> chainTrend) {

        if (currentTrend != null && chainTrend != null) {
            JSONArray resultArray = new JSONArray();
            try {
                JSONArray dataPointsCurrentArray = convertTrendList2Array(currentTrend);
                JSONArray dataPointsChainArray = convertTrendList2Array(chainTrend);

                JSONObject currentResult = new JSONObject();
                currentResult.put("target","当前");
                currentResult.put("datapoints",dataPointsCurrentArray);
                currentResult.put("columnName","");
                resultArray.put(currentResult);

                JSONObject chainResult = new JSONObject();
                chainResult.put("target","环比");
                chainResult.put("datapoints",dataPointsChainArray);
                chainResult.put("columnName","");
                resultArray.put(chainResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultArray.toString();
        }

        return null;
    }

    public static  String buildTrendJsonString(List<String> targetNames,List<Object[]>... trends) {

        if (trends != null && targetNames != null && (targetNames.size() == trends.length)) {
            JSONArray resultArray = new JSONArray();
            try {
                for (int i = 0; i < trends.length; i++) {
                    JSONArray dataPointsArray = convertTrendList2Array(trends[i]);

                    JSONObject currentResult = new JSONObject();
                    currentResult.put("target",targetNames.get(i));
                    currentResult.put("datapoints",dataPointsArray);
                    currentResult.put("columnName","");
                    resultArray.put(currentResult);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultArray.toString();
        }

        return null;
    }

    /**
     * 将趋势图list对象转换为array
     * @param objList
     * @return
     */
    public static JSONArray convertTrendList2Array(List<Object[]> objList){
        JSONArray arrays = new JSONArray();
        for (Object[] objects : objList) {
            JSONArray array = new JSONArray();
            Timestamp endTime = (Timestamp) objects[1];
            array.put(objects[2]);
            array.put(endTime.getTime());
            arrays.put(array);
        }
        return arrays;
    }

    /**
     * 构建环比数据(LongType)
     * @param currentTrend
     * @param chainTrend
     * @return
     * [{"dataPoints":[[1,1483200000000],[2,1485878400000]],"target":"当前","columnName":""},{"dataPoints":[[3,1488297600000],[4,1490976000000]],"target":"环比","columnName":""}]
     */
    public static  String buildTrendJsonStringForLongType(List<Object[]> currentTrend,List<Object[]> chainTrend) {

        if (currentTrend != null && chainTrend != null) {
            JSONArray resultArray = new JSONArray();
            try {
                JSONArray dataPointsCurrentArray = convertTrendList2ArrayForLongType(currentTrend);
                JSONArray dataPointsChainArray = convertTrendList2ArrayForLongType(chainTrend);

                JSONObject currentResult = new JSONObject();
                currentResult.put("target","当前");
                currentResult.put(" datapoints",dataPointsCurrentArray);
                currentResult.put("columnName","");
                resultArray.put(currentResult);

                JSONObject chainResult = new JSONObject();
                chainResult.put("target","环比");
                chainResult.put("datapoints",dataPointsChainArray);
                chainResult.put("columnName","");
                resultArray.put(chainResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultArray.toString();
        }

        return null;
    }

    /**
     * 将趋势图list对象转换为array(Long)
     * @param objList
     * @return
     */
    public static JSONArray convertTrendList2ArrayForLongType(List<Object[]> objList){
        JSONArray arrays = new JSONArray();
        for (Object[] objects : objList) {
            JSONArray array = new JSONArray();
            Timestamp endTime = (Timestamp) objects[1];
            BigInteger bigInteger = Optional.ofNullable((BigInteger) objects[2]).orElse(BigInteger.ZERO);
            array.put(bigInteger);
            array.put(endTime.getTime());
            arrays.put(array);
        }
        return arrays;
    }

    /**
     * 构建排行榜数据
     * @param rank
     * @return
     * [{"dataPoints":[[333,1493780277417]],"columnName":""},{"dataPoints":[[444,1493780277417]],"columnName":""}]
     */
    public static  String buildRankJsonString(List<Object[]> rank) {
        JSONArray resultArray = new JSONArray();
        if (rank != null && rank.size() > 0) {
            for (Object[] r : rank) {
                try {
                    String name = (String) r[0];
                    BigDecimal amount = Optional.ofNullable(new BigDecimal(r[1].toString())).orElse(BigDecimal.ZERO);
                    JSONArray dataPointArray = new JSONArray();
                    dataPointArray.put(amount);
                    dataPointArray.put(System.currentTimeMillis());
                    JSONArray dataPointsArray = new JSONArray();
                    dataPointsArray.put(dataPointArray);

                    JSONObject result = new JSONObject();
                    result.put("target",name);
                    result.put("datapoints",dataPointsArray);
                    result.put("columnName","");
                    resultArray.put(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultArray.toString();
    }

    /**
     * 首单，非首单占比
     * @param order
     * @param notOrder
     * @return
     * [{"dataPoints":[[1.11,1493780788540]],"target":"首单","columnName":""},{"dataPoints":[[2.22,1493780788550]],"target":"非首单","columnName":""}]
     */
    public static String buildOrderedConsumerCountJsonString(Double order, Double notOrder) {
        JSONArray resultArray = new JSONArray();
        try {

            JSONArray firstDataPointArray = new JSONArray();
            firstDataPointArray.put(order);
            firstDataPointArray.put(System.currentTimeMillis());
            JSONArray firstDataPointsArray = new JSONArray();
            firstDataPointsArray.put(firstDataPointArray);

            JSONObject firstResult = new JSONObject();
            firstResult.put("target","首单");
            firstResult.put("datapoints",firstDataPointsArray);
            firstResult.put("columnName","");
            resultArray.put(firstResult);


            JSONArray notFirstDataPointArray = new JSONArray();
            notFirstDataPointArray.put(notOrder);
            notFirstDataPointArray.put(System.currentTimeMillis());
            JSONArray notFirstDataPointsArray = new JSONArray();
            notFirstDataPointsArray.put(notFirstDataPointArray);

            JSONObject notFirstResult = new JSONObject();
            notFirstResult.put("target","非首单");
            notFirstResult.put("datapoints",notFirstDataPointsArray);
            notFirstResult.put("columnName","");
            resultArray.put(notFirstResult);
            return resultArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "[]";
    }


    //

    /**
     * 各渠道注册用户占比
     * @param list
     * @return
     * [{"dataPoints":[[1,1493783848008]],"target":"安卓","columnName":""},{"dataPoints":[[2,1493783848010]],"target":"IOS","columnName":""},{"dataPoints":[[3,1493783848010]],"target":"微信","columnName":""}]
     */
    public static  String buildPieJsonString(List<Object[]> list){
        JSONArray resultArray = new JSONArray();
        if(Optional.ofNullable(list)
            .filter(l -> l.size() >0)
            .isPresent()){

            for (Object[] o : list ){

                String targetName = (String)o[0];

                if(targetName != null){
                    try {
                        JSONArray dataPointArray = new JSONArray();
                        dataPointArray.put(o[1]);
                        dataPointArray.put(System.currentTimeMillis());
                        JSONArray dataPointsArray = new JSONArray();
                        dataPointsArray.put(dataPointArray);
                        JSONObject result = new JSONObject();
                        result.put("target",targetName);
                        result.put("datapoints",dataPointsArray);
                        result.put("columnName","");
                        resultArray.put(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return  resultArray.toString();
    }
}
