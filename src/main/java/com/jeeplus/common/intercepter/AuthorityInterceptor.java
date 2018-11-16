/**
 * 
 */
package com.jeeplus.common.intercepter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jeeplus.common.cache.GuavaCache;
import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.response.ResponseCode;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.utils.JsonUtil;
import com.jeeplus.modules.sys.utils.TokenUtil;


/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月21日
 * @version V1.0
 *
 */
public class AuthorityInterceptor implements HandlerInterceptor {
	
	private Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        String customerToken = TokenUtil.getCustomerToken(request);
        logger.debug("token = {}", customerToken);
        if (!StringUtils.isBlank(customerToken)) {
        	Map<String, String> map = GuavaCache.getKey(customerToken);
            if (map != null) {
            	logger.debug("openid = {}", map.get(Constants.Customer.OPENID));
            	return true;
            } else {
            	// 该用户未登录, 或者没有绑定到本地
            	tips(response);
            }
        }
        // 该用户未登录, 或者没有绑定到本地
    	tips(response);
		return false;
	}
	
	
	private void tips(HttpServletResponse response) {
		logger.debug("tips need login");
		// 该用户未登录, 或者没有绑定到本地
    	response.reset(); //要reset(), 否则会报异常getWriter() has already ...
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(JsonUtil.toJson(ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc())));
        } catch (IOException e) {
        	logger.error(ResponseCode.SYS_ERROR.getCode() + " : " + ResponseCode.SYS_ERROR.getDesc());
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}



}
