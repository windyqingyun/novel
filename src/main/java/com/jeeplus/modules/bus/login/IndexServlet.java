package com.jeeplus.modules.bus.login;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.jeeplus.common.cache.GuavaCache;
import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.utils.CookieUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.oauth.Oauth;


/**
 * Date: 12-12-4
 * Time: 上午10:28
 */
@CrossOrigin(origins = "*")
@Controller
public class IndexServlet {
	
	private Logger logger = LoggerFactory.getLogger(IndexServlet.class);
 
    @SuppressWarnings("deprecation")
	@RequestMapping("login.do")
    public void login(HttpServletRequest request, HttpServletResponse response, 
    		@RequestParam(value = "redirectUrl") String redirectUrl) {
		if (StringUtils.isBlank(redirectUrl)) {
			return; // throw ex ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
 
		logger.info("redirectUrl: {}", redirectUrl);
		CookieUtil.addCookieByName(response, Constants.Cookie.LOGIN_REDIRECT, redirectUrl, Constants.Cookie.LOGIN_REDIRECT_TIME);
		response.setContentType("text/html;charset=utf-8");

		try {
			response.sendRedirect(new Oauth().getAuthorizeURL(request));
		} catch (IOException | QQConnectException e) {
			CookieUtil.addCookieByName(response, Constants.Cookie.LOGIN_REDIRECT, null, 0);
			e.printStackTrace();
		}
       
    }
    
    public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
	}
    
    
	@RequestMapping("/qq/add.do")
	@ResponseBody
    public String qqAdd(HttpServletRequest request, HttpServletResponse response, 
    		@CookieValue(name=Constants.Cookie.CSESSIONID, required = false) String token) throws IOException {
		if (StringUtils.isBlank(token)) {
			return "token is null"; // throw ex ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		Map map = Maps.newConcurrentMap();
        map.put(Constants.Customer.OPENID, "9EA3BAFBE3F71277A8A16D953E03BC70");
        map.put(Constants.Customer.CUSTOMER_ID, "1");
        map.put(Constants.Customer.NICK_NAME, "梦境test");
        map.put(Constants.Customer.SEX, "1");
        map.put(Constants.Customer.HEAD_IMGURL, "http://qzapp.qlogo.cn/qzapp/101479906/9EA3BAFBE3F71277A8A16D953E03BC70/50");
        GuavaCache.setKey(token, map);
        return "success";
    }
    
}
