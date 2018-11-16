/**
 * 
 */
package com.jeeplus.common.cache;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月11日
 * @version V1.0
 *
 */
public class GuavaCache {

	private static Logger logger = LoggerFactory.getLogger(GuavaCache.class);

	// LRU算法 初始化容量 最大缓存容量(超过LRU算法) 有效期
	private static LoadingCache<String, Map<String, String>> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
			.maximumSize(10000).expireAfterAccess(Integer.parseInt(Global.getConfig("customer.keep.active")), TimeUnit.DAYS).build(new CacheLoader<String, Map<String, String>>() {

				// 默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
				@Override
				public Map<String, String> load(String arg0) throws Exception {
					return null;
				}
				
			});

	public static void setKey(String key, Map<String, String> value) {
		localCache.put(key, value);
	}

	public static Map<String, String> getKey(String key) {
		if (!StringUtils.isBlank(key)) {
			Map<String, String> value = null;
			try {
				value = localCache.get(key);
				if("null".equals(value)){
	                return null;
	            }
				if (value != null){
					return value;
				}
			} catch (Exception e) {
				logger.warn("localCache get error", e.getMessage());
				return null;
			}
		}
		return null;
	}
}