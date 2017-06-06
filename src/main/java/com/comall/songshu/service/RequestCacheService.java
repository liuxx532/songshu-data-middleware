package com.comall.songshu.service;

import com.comall.songshu.cache.service.EhcacheUtil;
import com.comall.songshu.cache.util.EhCacheKey;
import com.comall.songshu.web.rest.util.ServiceUtil;
import liquibase.util.MD5Util;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 请求缓存
 *
 * @author liushengling
 * @create 2017-06-05-18:55
 **/
@Service
public class RequestCacheService {

    @Autowired
    private EhcacheUtil ehcacheUtil;

    public String getRequestCache(HttpServletRequest request,String requestBody) throws Exception {
        String result = null;
        String key = getRequestCacheKey(request,requestBody);
        if(key != null){
            result = (String) ehcacheUtil.get(key);
        }
        return result;
    }

    public void putRequestCache(HttpServletRequest request,String requestBody,String result) throws Exception {
        String key = getRequestCacheKey(request,requestBody);
        if(key != null){
            ehcacheUtil.putCache(key,result);
        }
    }

    public boolean removeAllRequestCache(String cacheName){
       return ehcacheUtil.removeCacheService(cacheName);
    }

    private String getRequestCacheKey(HttpServletRequest request,String requestBody) throws Exception{
        String key = null;
        String uri = request.getRequestURI();
        JSONObject obj = new JSONObject(requestBody);
        JSONObject range = (JSONObject) obj.get("range");

        if (range != null && uri != null && !uri.trim().equals("")) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -2);
            String endStr = (String) range.get("to");
            Timestamp endTime = ServiceUtil.getInstance().parseUTCTimestamp(endStr);
            if (endTime != null && endTime.getTime() < calendar.getTimeInMillis()) {
                String[] uriArray = uri.split("/");
                key = uriArray[1] + EhCacheKey.KEYSPILT + MD5Util.computeMD5(requestBody);
            }
        }
        return key;
    }
}
