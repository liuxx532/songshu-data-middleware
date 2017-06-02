package com.comall.songshu.web.rest.util;

import com.comall.songshu.web.rest.vm.TopStat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import springfox.documentation.spring.web.json.Json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;


/**
 * Created by wdc on 2017/4/14.
 */
public class JsonStringBuilder {


    public static String buildCommonJsonString(String target, JSONArray dataPointsArray, String columnName) {
        JSONArray resultArray = new JSONArray();
        JSONObject result = buildCommonJsonObject(target, dataPointsArray,columnName);
        resultArray.put(result);
        return resultArray.toString();
    }

    public static JSONObject buildCommonJsonObject(String target, Object dataPointsArray, String columnName) {
        JSONObject result = new JSONObject();
        try {
            result.put("target",target);
            result.put("datapoints",dataPointsArray);
            result.put("columnName",columnName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
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
                JSONArray dataPointsChainArray = convertChainTrendList2Array(chainTrend,currentTrend);

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

    /**
     * 趋势图（非环比图）
     * @param targetNames
     * @param trends
     * @return
     */
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
     * 将趋势图list对象转换为array(环比数据转换)
     * 环比返回的数据时间轴当前时间时间轴一致
     * @param chainList 环比数据
     * @param currentList 当前数据
     * @return
     */
    public static JSONArray convertChainTrendList2Array(List<Object[]> chainList,List<Object[]> currentList){
        JSONArray arrays = new JSONArray();
        int chainSize = chainList.size();
        int currentSize = currentList.size();
        if(chainSize > 0 && chainSize == currentSize){
            for(int i=0;i<chainSize;i++){
                JSONArray array = new JSONArray();
                Timestamp endTime = (Timestamp) currentList.get(i)[1];
                array.put(chainList.get(i)[2]);
                array.put(endTime.getTime());
                arrays.put(array);
            }
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
     * 构建饼状图
     * @param list
     * @param columnName
     * @return
     */
    public static  String buildPieJsonString(List<Object[]> list,String columnName){
        JSONObject result = new JSONObject();
        List<JSONObject> dataPoints = new LinkedList<>();
        try {
            if(Optional.ofNullable(list)
                .filter(l -> l.size() >0)
                .isPresent()){

                for (Object[] o : list ){
                    JSONObject columnObj = new JSONObject();
                    columnObj.put("label",o[0]);
                    columnObj.put("value",o[1]);
                    dataPoints.add(columnObj);
                }
            }
            result.put("datapoints",dataPoints);
            result.put("columnName",columnName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  result.toString();
    }

    /**
     * 封装横向柱状图josn
     * @param list (list构成 0_name,1_value)
     * @param tagValue 标记值
     * @param needReverse 是否需要反序，目前图标上不支持配置排序
     * @return
     */
    public static  String buildHistogramPanelJsonString(List<Object[]> list,String tagValue,Boolean needReverse){
        JSONObject result = new JSONObject();

        try {
            JSONArray dataArray = new JSONArray();
            if(Optional.ofNullable(list)
                .filter(l -> l.size() >0)
                .isPresent()){
                if (needReverse){
                    Collections.reverse(list);
                }
                for (Object[] o : list ){

                    JSONObject data = new JSONObject();
                    data.put("name",o[0]);
                    data.put("value",o[1]);
                    dataArray.put(data);
                }
            }
            result.put("tagValue",tagValue);
            result.put("items",dataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  result.toString();
    }

    /**
     * 封装表格类型（有title）
     * @param valueList 表内具体内容 valueList中的数据顺序要和title对应的顺序一致
     * @param titleList 表title
     * @param targetName
     * @return
     */
    public static  String buildTableJsonString(List<Object[]> valueList,List<Object[]> titleList,String targetName){

        if(titleList != null && titleList.size()>0 && valueList != null && valueList.size()>0){
            try {
                //标题指标数
                int titleLength = titleList.size();
                long currentMills = System.currentTimeMillis();

                //表头
                Map<String,String> titleMap = new LinkedHashMap<>();
                for (Object[] oTile : titleList){
                    titleMap.put((String) oTile[0],(String)oTile[1]);
                }
                JSONArray titleArray = new JSONArray();

                titleArray.put(titleMap);
                titleArray.put(currentMills);

                //表内具体内容
                List<JSONObject> valueJsonList = new LinkedList<>();
                for (int i=0;i<valueList.size();i++){
                    JSONObject valueJson = new JSONObject();
                    Object[] oValue = valueList.get(i);
                    if(oValue.length != titleLength){
                        break;
                    }
                    for (int j=0;j<titleList.size();j++){
                        valueJson.put((String) titleList.get(j)[0],oValue[j]);
                    }
                    valueJsonList.add(valueJson);
                }
                JSONArray valueArray = new JSONArray();
                valueArray.put(valueJsonList);
                valueArray.put(currentMills);

                //构建主数据源
                JSONArray dataPointArray = new JSONArray();
                dataPointArray.put(valueArray);
                dataPointArray.put(titleArray);

                return buildCommonJsonString(targetName,dataPointArray,"");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return  null;
    }

    /**
     * 封装横向表格类型
     * @param valueList 表内具体内容
     * @param targetName
     * @return
     */
    public static  String buildTransverseTableJsonString(List<Object[]> valueList,String targetName){
        JSONArray result = new JSONArray();
        if(valueList != null && valueList.size()>0){
            JSONArray dataPoints = new JSONArray(valueList);
            JSONObject valueJson = new JSONObject();
            try {
                valueJson.put("datapoints",dataPoints);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            result.put(valueJson);
        }
        return  result.toString();
    }

    /**
     * 封装漏斗图josn
     * @param arrayList 支持多类别漏斗图（如：需要展示访客漏斗图及注册会员漏斗图等，无上限）
     * @param targetArray  漏斗名称
     * @return
     */
    public static  String buildFunnelJsonString(List<Object[]> arrayList,Object[] targetArray){
        List<JSONObject> funnelList = new LinkedList<>();
        if(arrayList != null && arrayList.size() >0 && targetArray != null && targetArray.length >0){
            long currentMills = System.currentTimeMillis();
            //1.校验数据格式是否正确
            //漏斗数
            int funnelCount = 0;
            int targetLength = targetArray.length;
            for (Object[] array : arrayList){
                int length  = array.length;
                if(funnelCount == 0){
                    funnelCount = length;
                }else if(funnelCount != length || targetLength != funnelCount){//数据错误，各类别漏斗图漏斗数不一致
                    break;
                }
            }

            //2.封装数据
            if(funnelCount >0){
                for(int i=0;i<funnelCount;i++){

                    List<JSONArray> dataPointList= new LinkedList<>();

                    for (int j=0;j<arrayList.size();j++){
                        JSONArray point = new JSONArray();
                        point.put(arrayList.get(j)[i]);
                        point.put(currentMills);
                        dataPointList.add(point);
                    }

                    JSONObject dataObject =  buildCommonJsonObject((String) targetArray[i],dataPointList,"");
                    funnelList.add(dataObject);
                }
            }
        }
        return  new JSONArray(funnelList).toString();
    }

}
