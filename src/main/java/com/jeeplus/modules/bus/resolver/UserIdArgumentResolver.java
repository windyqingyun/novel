/**
 * 
 */
package com.jeeplus.modules.bus.resolver;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jeeplus.common.cache.GuavaCache;
import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.utils.CookieUtil;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月22日
 * @version V1.0
 *
 */
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
	
	private Logger logger = LoggerFactory.getLogger(UserIdArgumentResolver.class);
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		String name = clazz.getName();
		logger.info("name = {} equals(userId) : {}",name, StringUtils.equals(name, "userId"));
        return StringUtils.equals(name, "userId");
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String customerId = null;
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		String csessionid = CookieUtil.getCSESSIONID(request);
	    if (StringUtils.isBlank(csessionid)) {
	    	Map<String, String> map = GuavaCache.getKey(csessionid);
	    	customerId = map.get(Constants.Customer.CUSTOMER_ID);
	    } 
	    logger.info("resolve customerId = {}", customerId);
        return customerId;
	}

}
