package com.comall.songshu.cache.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>本地缓存key常量</br></p>
 * @author liushengling Email:liushengling@co-mall.com
 * @company 北京科码先锋软件技术有限公司@版权所有
 * @version
 * @since 2016年9月22日上午9:43:43
 */
public class EhCacheKey {


	/**
	 * key的连接符
	 */
	public static final String KEYSPILT="#";
	/**
	 * 下划线，拼接key使用
	 */
	public static final String UNDERLINE = "_";


    public static String INDEX ="index#";

    public static String PRODUCT ="product#";

    public static String MEMBER ="member#";

    public static String CHANNEL ="channel#";


    public static String COMMON_CACHE ="CommonCache";

    public static String INDEX_CACHE ="IndexCache";

    public static String PRODUCT_CACHE ="ProductCache";

    public static String MEMBER_CACHE ="MemberCache";

    public static String CHANNEL_CACHE ="ChannelCache";





	public static Map<String,String> keyCacheNameMap=new HashMap<String,String>();

	static {
		//通用
		keyCacheNameMap.put(INDEX, INDEX_CACHE);
		keyCacheNameMap.put(PRODUCT, PRODUCT_CACHE);
		keyCacheNameMap.put(MEMBER, MEMBER_CACHE);
		keyCacheNameMap.put(CHANNEL, CHANNEL_CACHE);

	}



}
