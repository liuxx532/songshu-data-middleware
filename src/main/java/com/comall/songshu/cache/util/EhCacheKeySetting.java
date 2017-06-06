package com.comall.songshu.cache.util;

/**
 * @author WanChuanLai
 * @create 11/29/16.
 * 作用:ehcache 对应key的设置
 */
public class EhCacheKeySetting {

    /**
     * 作用:对象在cache中的最大闲置时间
     */
    private int timeToIdleSeconds = 0;
    /**
     * 作用:对象在cache中的最大存活时间
     */
    private int timeToLiveSeconds = 0;
    /**
     * 作用:当配置了 eternal ，那么TTI和TTL这两个配置将被覆盖，对象会永恒存在cache中，永远不会过期
     */
    private boolean eternal = false;

    public int getTimeToIdleSeconds() {
        return timeToIdleSeconds;
    }

    public void setTimeToIdleSeconds(int timeToIdleSeconds) {
        this.timeToIdleSeconds = timeToIdleSeconds;
    }

    public int getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    public void setTimeToLiveSeconds(int timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    public boolean isEternal() {
        return eternal;
    }

    public void setEternal(boolean eternal) {
        this.eternal = eternal;
    }



}
