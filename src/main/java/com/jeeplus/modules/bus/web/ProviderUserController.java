/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.ProviderUser;
import com.jeeplus.modules.bus.service.ProviderUserService;

/**
 * 流量提供端用户Controller
 * @author zhangsc
 * @version 2017-12-22
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/service/interface/providerUser")
public class ProviderUserController extends BaseController {

	@Autowired
	private ProviderUserService providerUserService;
	
	/**
	 * 获取用户信息
	 */
	@RequestMapping(value = "info", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String info(@RequestParam(name = "officeId")String officeId, @RequestParam(name="userId")String userId) throws Exception{
		String result = null;
		try {
			if(StringUtils.isNotBlank(userId)){
				ProviderUser user = providerUserService.getUserByOfficeAndOriginalId(officeId, userId);
				result = ServiceUtil.getMessage(RespCodeEnum.U0, user);
			}else{
				result = ServiceUtil.getMessage(RespCodeEnum.U5, "请登录");
			}
		} catch (ServiceException e) {
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("返回结果:" + result);
		
		return result;
	}

}