package com.comall.songshu.cache.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WanChuanLai
 * @create 11/29/16.
 * 作用:所有获取key 的地方
 */
public class CacheKeys {

    private static Map<String, EhCacheKeySetting> ehCacheKeySettingMap = null;
    private static final String ehcacheSettingPath = "classpath:ehcache/ehcacheKeySettings.data";

    private String key;
    private EhCacheKeySetting ehCacheKeySetting;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public EhCacheKeySetting getEhCacheKeySetting() {
        return ehCacheKeySetting;
    }

    public void setEhCacheKeySetting(EhCacheKeySetting ehCacheKeySetting) {
        this.ehCacheKeySetting = ehCacheKeySetting;
    }


    /**
     * 查询所有的ehcache 缓存
     *
     * @return
     */
    public static Map<String, EhCacheKeySetting> getEhCacheKeySettingMap() {
        if (ehCacheKeySettingMap == null) {
            ehCacheKeySettingMap = new HashMap<>();
            //从配置文件中读取
            String content = "";
            try {
                content = getContent(ehcacheSettingPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!content.isEmpty()) {
                List<CacheKeys> ehCacheKeysList = JSONObject.parseArray(content, CacheKeys.class);
                if (ehCacheKeysList != null && ehCacheKeysList.size()>0) {
                    for (CacheKeys ehCacheKeys : ehCacheKeysList) {
                        if (!ehCacheKeySettingMap.containsKey(ehCacheKeys.getKey())) {
                            ehCacheKeySettingMap.put(ehCacheKeys.getKey(), ehCacheKeys.getEhCacheKeySetting());
                        }
                    }
                }
            }
        }
        return ehCacheKeySettingMap;
    }

    public static String getContent(String path) throws Exception {

        URL uri = ResourceUtils.getURL(path);

        UrlResource pathResource = new UrlResource(uri);
        EncodedResource er = new EncodedResource(pathResource, "utf-8");
        String content;
        content = FileCopyUtils.copyToString(er.getReader());
        return content;
    }
}
