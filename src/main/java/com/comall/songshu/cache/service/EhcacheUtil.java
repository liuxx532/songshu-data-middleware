package com.comall.songshu.cache.service;

import com.alibaba.fastjson.JSON;
import com.comall.songshu.cache.util.CacheSerializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>本地缓存工具类</br></p>
 * @author  lizhongcai lizhongcai@co-mall.com
 * @company 北京科码先锋软件技术有限公司@版权所有
 * @version
 * @since  2015年12月4日
 */

@Service
public class EhcacheUtil {

	@Autowired
	private EhcacheClient ehcacheClient;


	/**
	 * <p>放入缓存</br></p>
	 * @param key
	 * @param data
	 */
	public void putCache(String key,Object data) {
        ehcacheClient.getCacheServiceByKeySpilt(key).set(key, CacheSerializable.toJsonString(data));
	}

	/**
	 * <p>获取缓存数据</br></p>
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return JSON.parse(ehcacheClient.getCacheServiceByKeySpilt(key).get(key));
	}

    /**
     * <p>删除缓存数据</br></p>
     * @param key
     * @return
     */
    public boolean delete(String key) {
        return ehcacheClient.getCacheServiceByKeySpilt(key).delete(key);
    }

    /**
     * <p>删除缓存块</br></p>
     * @param cacheName
     * @return
     */
    public boolean removeCacheService(String cacheName) {
        return ehcacheClient.removeServiceByCacheName(cacheName);
    }

}
