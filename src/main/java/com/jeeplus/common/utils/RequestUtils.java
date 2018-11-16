package com.jeeplus.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.RequestFacade;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.support.json.JSONUtils;
import com.jeeplus.common.mapper.JsonMapper;

/**
 * request工具类
 * 
 * @author zhangsc
 * @version 2017年11月7日
 */
public class RequestUtils {
	private static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

	private static Field requestField;

	private static Field parametersParsedField;

	private static Method addParamMethod;

	// private static Field coyoteRequestField;
	//
	// private static Field parametersField;
	//
	// private static Field hashTabArrField;

	static {
		try {
			Class clazz = Class.forName("org.apache.catalina.connector.RequestFacade");
			requestField = clazz.getDeclaredField("request");
			requestField.setAccessible(true);

			parametersParsedField = requestField.getType().getDeclaredField("parametersParsed");
			parametersParsedField.setAccessible(true);

			addParamMethod = requestField.getType().getDeclaredMethod("addParameter", String.class, String[].class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取request中的参数
	 * 
	 * @param req
	 * @return
	 */
	public static String getRequestPayload(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = req.getReader();
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * 更改request中的parameter并返回旧值 ps:只对tomcat服务器管用
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String setParmterAndRstOldValue(String key, String value, HttpServletRequest req) throws Exception {
		ShiroHttpServletRequest request = (ShiroHttpServletRequest) req;
		RequestFacade facade = (RequestFacade) request.getRequest();

		String oldPara = req.getParameter(key);

		Object innerRequest = requestField.get(facade);
		parametersParsedField.setBoolean(innerRequest, true);
		addParamMethod.invoke(innerRequest, key, new String[] { value });
		parametersParsedField.setBoolean(innerRequest, false);

		return oldPara;
	}

	public static void printAllParameters(HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		System.out.println("------------------------开始打印request中的所有参数---------------------------");
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			System.err.println("name:" + name + ",value:" + request.getParameter(name));
		}
	}

	public static String getJson(String json) {
		logger.info("json from : {}", json);
		try {
			return JsonMapper.toJsonString(JSONUtils.parse(json));
		} catch (Exception e) {
			logger.error("JSONUtils error : {}", e.getMessage());
			return JsonMapper.toJsonString(JsonUtil.parseJson(json));
		}
	}

	/**
	 * 获取真实的ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestIp(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		String forwarded = request.getHeader("X-Forwarded-For");
		String realIp = request.getHeader("X-Real-IP");

		String ip = null;
		if (realIp == null) {
			if (forwarded == null) {
				ip = remoteAddr;
			} else {
				ip = remoteAddr + "/" + forwarded.split(",")[0];
			}
		} else {
			if (realIp.equals(forwarded)) {
				ip = realIp;
			} else {
				if (forwarded != null) {
					forwarded = forwarded.split(",")[0];
				}
				ip = realIp + "/" + forwarded;
			}
		}
		return ip;
	}

	
	/**
	 * IP 获取ip地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = null;
		try {
			ip = request.getHeader("x-forwarded-for");
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} catch (Exception e) {
			logger.error("IPUtils ERROR ", e);
		}

		// 使用代理，则获取第一个IP地址
		if ((!StringUtils.isEmpty(ip)) && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}

		return ip;
	}

}
