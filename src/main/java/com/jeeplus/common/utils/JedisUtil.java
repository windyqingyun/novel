package com.jeeplus.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeplus.common.config.Global;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * @description:
 * @author: lzp
 * @date: 2018/1/23
 */
public class JedisUtil {

	private static Logger log = LoggerFactory.getLogger(JedisUtil.class);
	
	private static JedisPool jedisPool = SpringContextHolder.getBean(JedisPool.class);

	public static final String KEY_PREFIX = Global.getConfig("redis.keyPrefix");
	
    public static void returnResource(Jedis jedis) {
    	jedisPool.returnResource(jedis);
    }

    public static void returnBrokenResource(Jedis jedis) {
    	jedisPool.returnBrokenResource(jedis);
    }

    /**
     * 设置   key:value
     *
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error ", key, value, e);
            returnBrokenResource(jedis);
            return result;
        }
        returnResource(jedis);
        return result;
    }


    /**
     * 设置   key:T
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String set(String key, T value) {
        String result = null;
        String str = beanToString(value);
        if (str == null || str.length() <= 0) {
            return result;
        }
        return set(key, result);
    }

    /**
     * 设置   key:value (time)
     *
     * @param key
     * @param value
     * @param exTime 秒
     * @return
     */
    public static String setEx(String key, String value, int exTime) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setEx key:{} value:{} error ", key, value, e);
            returnBrokenResource(jedis);
            return result;
        }
        returnResource(jedis);
        return result;
    }

    /**
     * 设置   key:T (time)
     * @param key
     * @param value
     * @param exTime 秒
     * @param <T>
     * @return
     */
    public static <T> String setEx(String key, T value, int exTime) {
        String result = null;
        String str = beanToString(value);
        if (str == null || str.length() <= 0) {
            return result;
        }
        return setEx(key, str, exTime);
    }


    public static Long setNx(String key, String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setNx key:{} value:{} error ", key, value, e);
            returnBrokenResource(jedis);
            return result;
        }
        returnResource(jedis);
        return result;
    }

    /**
     * 设置key的时间
     *
     * @param key
     * @param exTime 秒
     * @return
     */
    public static Long expire(String key, int exTime) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} error ", key, e);
            returnBrokenResource(jedis);
            return result;
        }
        returnResource(jedis);
        return result;
    }


    /**
     * 获取   vlaue
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error ", key, e);
            returnBrokenResource(jedis);
            return result;
        }
        returnResource(jedis);
        return result;
    }

    /**
     * 获取   T
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T get(String key, Class<T> clazz) {
    	return stringToBean(get(key), clazz);
    }


    /**
     * 设置 newValue 并返回旧值 oldValue
     * @param key
     * @param newValue
     * @return
     */
    public static String getSet(String key, String newValue) {
        Jedis jedis = null;
        String oldValue = null;
        try {
            jedis = jedisPool.getResource();
            oldValue = jedis.getSet(key, newValue);
        } catch (Exception e) {
            log.error("getSet key:{} newValue:{} error ", key, newValue, e);
            returnBrokenResource(jedis);
            return oldValue;
        }
        returnResource(jedis);
        return oldValue;
    }
    
    public static Boolean isExistsKey(String key) {
    	Jedis jedis = null;
        try {
        	jedis = jedisPool.getResource();
             return jedis.exists(key);
        } catch (Exception e) {
            log.error("isExistsKey key:{} error {}", key,  e);
            returnBrokenResource(jedis);
        }
        returnResource(jedis);
    	return false;
    }

    public static int isExistsKeys(String pattern) {
    	Jedis jedis = null;
    	int size = 0;
        try {
        	jedis = jedisPool.getResource();
        	List<String> keysList = scanPattern(pattern, jedis);
            if (keysList != null && keysList.size() > 0) {
            	return keysList.size();
            }
        } catch (Exception e) {
            log.error("isExistsKeys pattern:{} error {}", pattern,  e);
            returnBrokenResource(jedis);
        }
        returnResource(jedis);
        return size;
    }
    
    public static Long del(String key) {
        Jedis jedis = null;
        Long result = 0l;
        try {
            jedis = jedisPool.getResource();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error ", key, e);
            returnBrokenResource(jedis);
            return result;
        }
        returnResource(jedis);
        return result;
    }

    /**
     * 
     * @param pattern
     * @return
     */
    public static Long delBatch(String pattern) {
        Jedis jedis = null;
        Long result = 0l;
        try {
            jedis = jedisPool.getResource();
            List<String> keysList = scanPattern(pattern, jedis);
            if (keysList != null && keysList.size() > 0) {
            	result = jedis.del(keysList.toArray(new String[0]));
            }
        } catch (Exception e) {
            log.error("delBatch pattern key:{} error ", pattern, e);
            returnBrokenResource(jedis);
            return result;
        }
        returnResource(jedis);
        return result;
    }

    public static Object eval(String script, int keyCount, String... params) {
        Jedis jedis = null;
        Object obj = null;
        try {
            jedis = jedisPool.getResource();
            obj = jedis.eval(script, keyCount, params);
        } catch (Exception e) {
            log.error("eval script:{} keyCount:{} params:{} error ", script, keyCount, params, e);
            returnBrokenResource(jedis);
            return obj;
        }
        returnResource(jedis);
        return obj;
    }

    private static List<String> scanPattern(String pattern, Jedis jedis) {
        List<String> keys = new ArrayList<String>();
        String cursor = "0";
        ScanParams sp = new ScanParams();
        sp.match(pattern);
        sp.count(100);
        do {
            ScanResult<String> ret = jedis.scan(cursor, sp);
            List<String> result = ret.getResult();
            if (result != null && result.size() > 0) {
                keys.addAll(result);
            }
            //再处理cursor
            cursor = ret.getStringCursor();
        } while (!cursor.equals("0"));
        return keys;
    }


    private static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JsonUtil.obj2String(value);
        }
    }
    

    private static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JsonUtil.string2Obj(str, clazz);
        }
    }
}
