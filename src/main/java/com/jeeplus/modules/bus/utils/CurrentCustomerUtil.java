/**
 * 
 */
package com.jeeplus.modules.bus.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.jeeplus.common.cache.GuavaCache;
import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.utils.CookieUtil;
import com.jeeplus.modules.bus.entity.Customer;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月24日
 * @version V1.0
 *
 */
public class CurrentCustomerUtil {

	/**
	 * 获取缓存中的用户ID
	 */
	public static String getCurrentCustomerId(HttpServletRequest request) {
		String csessionid = CookieUtil.getCSESSIONID(request);
		return getCurrentCustomerId(csessionid);
	}
	
	/**
	 * 获取缓存中的用户ID
	 */
	public static String getCurrentCustomerId(String token) {
		if (!StringUtils.isBlank(token)) {
			Map<String, String> map = GuavaCache.getKey(token);
			if (map != null) {
				return map.get(Constants.Customer.CUSTOMER_ID);
			}
		}
		return null;
	}
	
	/**
	 * 获取缓存中的用户
	 */
	public static Customer getCurrentCustomer(HttpServletRequest request) {
		String csessionid = CookieUtil.getCSESSIONID(request);
		return getCurrentCustomer(csessionid);
	}
	
	/**
	 * 获取缓存中的用户
	 */
	public static Customer getCurrentCustomer(String token) {
		if (!StringUtils.isBlank(token)) {
			Map<String, String> map = GuavaCache.getKey(token);
			if (map != null) {
				Customer customer = new Customer();
				customer.setId(Integer.parseInt(map.get(Constants.Customer.CUSTOMER_ID)));
				customer.setNickname(map.get(Constants.Customer.NICK_NAME));
				customer.setHeadimgurl(map.get(Constants.Customer.HEAD_IMGURL));
				customer.setSex(Integer.parseInt(map.get(Constants.Customer.SEX)));
				return customer;
			}
		}
		return null;
	}
	
	public static Map<String, String> getMap(HttpServletRequest request) {
		String csessionid = CookieUtil.getCSESSIONID(request);
		return getMap(csessionid);
	}
	
	public static Map<String, String> getMap(String token) {
		Map<String, String> map = GuavaCache.getKey(token);
		return map;
	}

}
