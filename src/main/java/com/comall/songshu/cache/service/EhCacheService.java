package com.comall.songshu.cache.service;

import com.comall.songshu.cache.util.CacheKeys;
import com.comall.songshu.cache.util.EhCacheKeySetting;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * @author WanChuanLai
 * @create 11/29/16.
 * 作用:ehcache缓存
 */
public class EhCacheService {


    private final Logger logger = LoggerFactory.getLogger(EhCacheService.class);

    private static final String SEPARATOR = "#";


    private final Map<String, EhCacheKeySetting> defaultEhCacheKeySettings;

    private final Ehcache ehCache;

    public EhCacheService(Ehcache ehCache) {
        this.ehCache = ehCache;
        this.defaultEhCacheKeySettings = CacheKeys.getEhCacheKeySettingMap();
    }


    public <V extends Serializable> boolean set(String key, V value) {
        //String json = CacheSerializable.toJsonString(value);
        put(key, value);
        return true;
    }

    public <T extends Serializable> T get(String key, Class<T> clazz) {
        final Element element = getByKey(key);
        if (element == null || element.getObjectValue() == null) return null;
        return (T) element.getObjectValue();
    }

    public <T extends Serializable> T get(String key) {
        final Element element = getByKey(key);
        if (element == null || element.getObjectValue() == null) return null;
        return (T) element.getObjectValue();
    }

    /**
     * @param key
     * @param value
     * @param <V>
     * @return
     */
    public <V extends Serializable> boolean setList(String key, List<V> value) {
        put(key, value);
        return false;
    }


    public <T extends Serializable> List<T> getList(String key) {
        final Element element = getByKey(key);
        if (element == null || element.getObjectValue() == null) return null;
        return (List<T>)element.getObjectValue();
    }

    public boolean delete(String key) {
        return deleteByKey(key);
    }



    public boolean removeAll() {
        this.ehCache.removeAll();
        return true;
    }


    /**
     * 作用:put值
     * 1,如果没有设置key 对应的过期设置,则默认缓存块的缓存配置
     * 2,如果设置了key的过期配置,则根据的key缓存配置EhCacheKeySetting
     *
     * @param key
     * @param value
     */
    private void put(String key, Object value) {

    	if (key.indexOf(SEPARATOR) > 0) {
            String prefix = key.split(SEPARATOR)[0];
            EhCacheKeySetting ehCacheKeySetting = defaultEhCacheKeySettings.get(prefix);
            if (ehCacheKeySetting!= null) {
            	if (ehCacheKeySetting.isEternal()) {
                    ehCache.put(new Element(key, value, true));
                } else {
                    ehCache.put(new Element(key, value, ehCacheKeySetting.getTimeToIdleSeconds(), ehCacheKeySetting.getTimeToLiveSeconds()));
                }
            } else {
                logger.error(key + "->key不存在");
            }
        }else {
            logger.error(key + "->规则符不对,应该为");
        }


    }

    private Element getByKey(String key) {
        return ehCache.get(key);
    }

    private boolean deleteByKey(String key) {
        return  ehCache.remove(key);
    }


}
