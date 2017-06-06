package com.comall.songshu.cache.service;


import com.comall.songshu.cache.util.EhCacheKey;
import org.springframework.stereotype.Service;

/**
 * @author WanChuanLai
 * @create 11/29/16.
 * 作用:本地缓存
 * 这里可以注入任何缓存
 */
@Service
public class EhcacheClient {

    private final EhCacheProvider cacheProvider;

    public EhcacheClient(EhCacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }


    /**
     * 作用:根据cachename获取缓存块服务
     *
     * @param cacheName
     * @return
     */
    public EhCacheService getCacheService(final String cacheName) {
        return this.cacheProvider.buildCache(cacheName);

    }

    public boolean removeServiceByCacheName(String cacheName) {
        return this.cacheProvider.removeCache(cacheName);
    }




    /**
     * 根据分隔符前面的key名 获取缓存块服务
     * 比如key=region#1,这里的keyName=region#
     *
     * @param keyName 传入的是分隔符前面的值+分隔符
     * @return
     */
    public EhCacheService getCacheServiceByKey(final String keyName) {
        return this.cacheProvider.buildCache(EhCacheKey.keyCacheNameMap.get(keyName));
    }

    /**
     * 根据分隔符前面的key名 获取缓存块服务
     * 比如key=region#1,这里的keyName=region#1
     *
     * @param keyName key 的全写
     * @return
     */
    public EhCacheService getCacheServiceByKeySpilt(final String keyName) {
        if (keyName.contains(EhCacheKey.KEYSPILT)) {
            String cacheName = EhCacheKey.keyCacheNameMap.get(keyName.split(EhCacheKey.KEYSPILT)[0] + EhCacheKey.KEYSPILT);
            return this.cacheProvider.buildCache(cacheName);
        }
        return null;
    }

}
