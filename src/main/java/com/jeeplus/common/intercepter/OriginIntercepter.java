package com.jeeplus.common.intercepter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jeeplus.common.response.ResponseCode;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.utils.CookieUtil;
import com.jeeplus.common.utils.JsonUtil;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.OriginUtil;

public class OriginIntercepter implements HandlerInterceptor {

	private Logger logger = LoggerFactory.getLogger(OriginIntercepter.class);

	@Autowired
	private OfficeService officeService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String origin = CookieUtil.getORIGIN(request);
		logger.info("origin = {}", origin);
		if (OriginUtil.accessByOrigin(request)) {
			return true;
		}
		tips(response);
		return false;
	}

	private void tips(HttpServletResponse response) {
		logger.info("tips illegal origin");
		response.reset(); // 要reset(), 否则会报异常getWriter() has already ...
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");

		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.print(JsonUtil.toJson(ServerResponse.createByError(ResponseCode.ILLEGAL_ORIGIN.getCode(),
					ResponseCode.ILLEGAL_ORIGIN.getDesc())));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
