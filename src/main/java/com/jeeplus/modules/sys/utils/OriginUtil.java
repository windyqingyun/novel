package com.jeeplus.modules.sys.utils;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.CookieUtil;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.sys.dao.OfficeDao;
import com.jeeplus.modules.sys.dao.UserDao;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;


public class OriginUtil {
	
	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);


	/** 流量端的机构id，根据此id可以获取流量端端下的机构 **/
	public static final String CUSTOMER_PARENT_ID = "b75a19627f874085afae2cbdfefe9ada";
	/** 流量提供端口的 机构id 和 机构列表 缓存 只放已启用的 **/
	public static final String CACHE_CUSTOMER_OFFICE = "cache_customer_office";
	/** 流量提供端口的机构列表 只放已启用的 **/
	public static final String CACHE_KEY_CUSTOMER_OFFICE_LIST = "cache_key_customer_office_list";

	/**
	 * ?src=机构下用户名 src=机构id src=机构代号 获取可用的流量端的机构 从缓存中获取
	 * 
	 * @return
	 */
	private static Map<String, Object> findEnabledCutomerOffice() {
		@SuppressWarnings("unchecked")
		Map<String, Object> customerOfficeMap = (Map<String, Object>) CacheUtils.get(CACHE_CUSTOMER_OFFICE);
		if (customerOfficeMap == null) {
			customerOfficeMap = Maps.newConcurrentMap();
			List<Office> customerOfficeList = officeDao.findEnabledCustomerOffice(CUSTOMER_PARENT_ID);
			if (customerOfficeList != null && customerOfficeList.size() > 0) {
				int size = customerOfficeList.size();
				for (int i = 0; i < size; i++) {
					Office office = customerOfficeList.get(i);
					customerOfficeMap.put(office.getId(), office.getId());
					customerOfficeMap.put(office.getCode(), office.getId());
					Office officetmp = new Office();
					User usertmp = new User();
					usertmp.setOffice(officetmp);
					List<User> userListByOfficeId = userDao.findUserByOfficeId(usertmp);
					int size2 = userListByOfficeId.size();
					for (int j = 0; j < size2; j++) {
						User user = userListByOfficeId.get(j);
						customerOfficeMap.put(user.getLoginName(), user.getOffice().getId());
					}
				}
				customerOfficeMap.put(CACHE_KEY_CUSTOMER_OFFICE_LIST, customerOfficeList);
				if (customerOfficeMap != null && MapUtils.isNotEmpty(customerOfficeMap)) {
					CacheUtils.put(CACHE_CUSTOMER_OFFICE, customerOfficeMap);
				}
			}
		}
		return customerOfficeMap;
	}

	public static String getOfficeIdByOrigin(String origin) {
		if (!StringUtils.isBlank(origin) && !"null".equals(origin)) {
			Map<String, Object> customerOfficeMap = findEnabledCutomerOffice();
			if (customerOfficeMap != null && !customerOfficeMap.isEmpty()) {
				return (String) customerOfficeMap.get(origin);
			}
		}
		return null;
	}

	public static String getOfficeIdByOrigin(HttpServletRequest request) {
		String officeId = null;
		String origin = CookieUtil.getORIGIN(request);
		if (!StringUtils.isBlank(origin) && !"null".equals(origin)) {
			officeId = getOfficeIdByOrigin(origin);
		}else {
			origin = request.getParameter(Constants.Cookie.ORIGIN);
			if (!StringUtils.isBlank(origin) && !"null".equals(origin)) {
				officeId = getOfficeIdByOrigin(origin);
			}
		}
		return officeId;
	}

	public static Boolean accessByOrigin(HttpServletRequest request) {
		Map<String, Object> customerOfficeMap = findEnabledCutomerOffice();
		String origin = CookieUtil.getORIGIN(request);
		if (!StringUtils.isBlank(origin) && !"null".equals(origin)) {
			if (customerOfficeMap.containsKey(origin)) {
				return true;
			}
		}else {
			origin = request.getParameter(Constants.Cookie.ORIGIN);
			if (!StringUtils.isBlank(origin) && !"null".equals(origin)) {
				if (customerOfficeMap.containsKey(origin)) {
					return true;
				}
			}
		}
		return false;
	}
}
