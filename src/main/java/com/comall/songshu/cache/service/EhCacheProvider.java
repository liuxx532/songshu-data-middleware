package com.comall.songshu.cache.service;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.management.ManagementService;
import org.springframework.stereotype.Service;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author WanChuanLai
 * @create 1/4/17.
 * ehcache 提供者
 */
@Service
public class EhCacheProvider{

    public static String KEY_EHCACHE_CONFIG_XML = "/ehcache/ehcache.xml";

    private static CacheManager manager;
    private static final ConcurrentHashMap<String, EhCacheService> ehcacheConcurrentHashMap = new ConcurrentHashMap<>();
    protected final ReentrantLock lock = new ReentrantLock(true);

    private static boolean isLogJvm = false;


    public EhCacheService buildCache(String cacheName) {
        EhCacheService ehCacheService = ehcacheConcurrentHashMap.get(cacheName);
        if (ehCacheService == null) {
            lock.lock();
            try {
                ehCacheService = ehcacheConcurrentHashMap.get(cacheName);
                if (ehCacheService == null) {
                    if (manager == null) {
                        URL url = this.getClass().getResource(KEY_EHCACHE_CONFIG_XML);
                        manager = new CacheManager(url);
                        if (isLogJvm) {
                            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
                            ManagementService.registerMBeans(manager, mBeanServer, false, false, false, true);
                        }

                    }
                    Ehcache ehcache = manager.getEhcache(cacheName);
                    if (ehcache == null) {
                        manager.addCache(cacheName);
                        ehcache = manager.getEhcache(cacheName);
                    }
                    ehCacheService = new EhCacheService(ehcache);
                    ehcacheConcurrentHashMap.put(cacheName, ehCacheService);
                }

            } finally {
                lock.unlock();
            }

        }

        return ehCacheService;
    }

    public boolean removeCache(String cacheName) {
        lock.lock();
        try {
            ehcacheConcurrentHashMap.remove(cacheName);
            manager.removeCache(cacheName);
        } finally {
            lock.unlock();
        }
        return true;
    }


}
