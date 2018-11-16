/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.dto.UserWalletDto;
import com.jeeplus.modules.bus.entity.UserWallet;
import com.jeeplus.modules.bus.service.OfficeCoinConfigService;
import com.jeeplus.modules.bus.service.UserWalletService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;
import com.jeeplus.modules.sys.service.OfficeService;

/**
 * 用户钱包Controller
 * @author zhangsc
 * @version 2017-11-29
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/service/interface/userWallet")
public class UserWalletController extends BaseController {

	@Autowired
	private UserWalletService userWalletService;
	@Autowired
	private OfficeCoinConfigService officeCoinConfigService;
	@Autowired
	private OfficeService officeService;
	
	/**
	 * 获取用户余额
	 */
	@RequestMapping(value = "getWalletInfo", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String getWalletInfo(HttpServletRequest request,
			@RequestParam("officeId")String officeId,
			@RequestParam("userId")String userId) {
		userId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(userId)) {
			return ServiceUtil.doResponse(RespCodeEnum.U6, true);
		}
		
		String result = null;
		try {
			UserWallet userWallet = userWalletService.getUserWalletByOfficeId(userId, officeId);
			userWallet.setCoinConfig(officeCoinConfigService.getOfficeCoinConfigByOffice(officeId));
			
			result = ServiceUtil.getMessage(RespCodeEnum.U0, userWallet);
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
	
	/**
	 * 获取用户钱包的列表
	 */
	@RequestMapping(value = "findUserWalletList", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String findUserWalletList(HttpServletRequest request,
			@RequestParam("userId")String userId) {
		userId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(userId)) {
			return ServiceUtil.doResponse(RespCodeEnum.U6, true);
		}
		String result = null;
		try {
			if(!StringUtils.isBlank(userId)){
				List<UserWalletDto> walletDtos = Lists.newArrayList();
				List<String> officeIds = officeService.findEnabledProviderId();
				if(officeIds != null){
					for (String officeId : officeIds) {
						walletDtos.add(userWalletService.getUserWalletDtoByOffice(userId, officeId));
					}
				}
				
				result = ServiceUtil.getMessage(RespCodeEnum.U0, walletDtos);
			}else{
				result = ServiceUtil.getMessage(RespCodeEnum.U6, "请登录");
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