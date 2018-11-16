package com.jeeplus.modules.sys.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.utils.CookieUtil;

public class TokenUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

	public static String getCustomerToken(HttpServletRequest request) {
		String token = CookieUtil.getCSESSIONID(request);
        logger.debug("token = {}", token);
        if (!StringUtils.isBlank(token)) {
            return token;
        } else {
        	token = request.getParameter(Constants.Cookie.CSESSIONID);
        	if (!StringUtils.isBlank(token)) {
        		return token;
        	} 
        }
		return token;
	}
}
