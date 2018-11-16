/**
 * 
 */
package com.jeeplus.modules.bus.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.jeeplus.common.cache.GuavaCache;
import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.response.ResponseCode;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.utils.CookieUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.Customer;
import com.jeeplus.modules.bus.enums.URIEnum;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月23日
 * @version V1.0
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/service/interface/customer")
public class CustomerController extends BaseController {

	@RequestMapping("/info")
	public ServerResponse getCustomer(HttpServletRequest request) {
		Customer currentCustomer = CurrentCustomerUtil.getCurrentCustomer(request);
		if (currentCustomer != null) {
			Map map = Maps.newConcurrentMap();
			map.put("nickname", currentCustomer.getNickname());
			map.put("headurl", currentCustomer.getHeadimgurl());
			return ServerResponse.createBySuccess(map);
		}
		return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
	}
	
	@RequestMapping("/info/customerId")
	public ServerResponse getCustomerId(HttpServletRequest request) {
		String currentCustomerId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (!StringUtils.isBlank(currentCustomerId)) {
			Map map = Maps.newConcurrentMap();
			map.put("customerid", currentCustomerId);
			return ServerResponse.createBySuccess(map);
		}
		return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
	}
	
    @RequestMapping("/loginout.do")
    public void loginout(HttpServletRequest request, HttpServletResponse response,
    		@CookieValue(name=Constants.Cookie.CSESSIONID, required = false) String token) throws IOException {
    	CookieUtil.addCookieByName(response, Constants.Cookie.CSESSIONID, null, 0);
    	if (StringUtils.isNotBlank(token)) {
    		GuavaCache.setKey(token, new HashMap<String, String>());
		}
    	response.sendRedirect(URIEnum.HOST_INDEX.getUri());
    }
	
}




