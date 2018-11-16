/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.web;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.OfficeCoinConfig;
import com.jeeplus.modules.bus.service.OfficeCoinConfigService;

/**
 * 机构货币配置Controller
 * @author zhangsc
 * @version 2017-11-15
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/service/interface/officeCoinConfig")
public class OfficeCoinConfigController extends BaseController {

	@Autowired
	private OfficeCoinConfigService officeCoinConfigService;
	
	/**
	 * 根据机构获取机构货币配置
	 * @param officeId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "info", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String info(@RequestParam("officeId")String officeId) {
		String result = null;
		try {
			OfficeCoinConfig coinConfig = officeCoinConfigService.getOfficeCoinConfigByOffice(officeId);
			if(coinConfig == null){
				result = ServiceUtil.getMessage(RespCodeEnum.U5, "该机构没有货币配置信息");
			}else {
				result = ServiceUtil.getMessage(RespCodeEnum.U0, coinConfig);
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

	/**
	 * 查看，增加，编辑机构货币配置表单页面
	 */
	@RequiresPermissions(value={"bus:officeCoinConfig:view","bus:officeCoinConfig:add","bus:officeCoinConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OfficeCoinConfig officeCoinConfig, Model model) {
		model.addAttribute("officeCoinConfig", officeCoinConfig);
		return "modules/bus/officeCoinConfigForm";
	}

}