package com.comall.songshu.cache.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;
import java.util.List;

public class CacheSerializable {

	public static String toJsonString(Object object){
		return  JSON.toJSONString(object, getSerializerFeatures());
	}

	public static <V extends Serializable> List<V> getList(String json, Class<V> clazz){
		return JSON.parseArray(json, clazz);
	}

	public static <V extends Serializable> V get(String json, Class<V> clazz){
		try{
			return JSON.parseObject(json, clazz);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}


    public static SerializerFeature[] getSerializerFeatures() {
        SerializerFeature[] features = { SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null，输出为""，而不是null
            SerializerFeature.WriteNonStringKeyAsString,//如果key不为String 则转换为String 比如Map的key为Integer
            SerializerFeature.DisableCircularReferenceDetect//循环依赖
        };
        return features;
    }
}
