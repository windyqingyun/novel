/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.config.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.config.entity.UserSubscibeConfig;
import com.jeeplus.modules.bus.config.service.UserSubscibeConfigService;

/**
 * 用户订阅配置Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/service/interface/userSubscibeConfig")
public class UserSubscibeConfigController extends BaseController {

	@Autowired
	private UserSubscibeConfigService userSubscibeConfigService;
	
	/**
	 * 用户订阅配置列表页面
	 */
	@RequestMapping(value = "list",  method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String list(UserSubscibeConfig userSubscibeConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		String result = null;
		try {
			List<Map> mapList = Lists.newArrayList();
			if(userSubscibeConfig != null && userSubscibeConfig.getCreateBy() != null && StringUtils.isNotBlank(userSubscibeConfig.getCreateBy().getId())){
				/*
				 * map内容 "title", "titleImage", "officeName", "createDate"
				 */
				mapList = userSubscibeConfigService.findSubscibeInfoList(userSubscibeConfig);
				if(mapList != null){
					for (Map map : mapList) {
						map.put("titleImage", Global.downloadArchiveURL+"?url="+map.get("titleImage"));
					}
				}
				
				result = ServiceUtil.getMessage(RespCodeEnum.U0, mapList);
			}else {
				result = ServiceUtil.getMessage(RespCodeEnum.U0, mapList);
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
	 * 保存用户订阅配置
	 */
	@RequestMapping(value = "save", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String save(UserSubscibeConfig userSubscibeConfig) throws Exception{
		String result = null;
		try {
			userSubscibeConfig.setIsAutoBuy(Global.YES);
			String id = null;

			UserSubscibeConfig t = userSubscibeConfigService.getUserSubscibeConfig(userSubscibeConfig);  //从数据库取出记录的值
			if(t != null && StringUtils.isNotBlank(t.getId())){
				MyBeanUtils.copyBeanNotNull2Bean(userSubscibeConfig, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
				userSubscibeConfigService.save(t);//保存
				id = t.getId();
			}else{//新增表单保存
				userSubscibeConfigService.save(userSubscibeConfig);//保存
				id = userSubscibeConfig.getId();
			}

			result = ServiceUtil.getMessage(RespCodeEnum.U0, (Object)id);
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
	 * 判断用户是否存在某一个素材的订阅
	 * @param userId
	 * @param fodderId
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "existsSubscibe", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String existsSubscibe(UserSubscibeConfig userSubscibeConfig){
		String result = null;
		try {
			UserSubscibeConfig userSubscConfig = userSubscibeConfigService.getUserSubscibeConfig(userSubscibeConfig);
			Map dataMap = Maps.newHashMap();
			if(userSubscConfig != null && StringUtils.isNotBlank(userSubscConfig.getId())) {
				dataMap.put("id", userSubscConfig.getId());
			}else {
				dataMap.put("id", null);
			}

			result = ServiceUtil.getMessage(RespCodeEnum.U0, dataMap);
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
	 * 删除用户订阅配置
	 */
	@RequestMapping(value = "delete", method=RequestMethod.POST,  produces = { "application/json; charset=UTF-8" })
	public String delete(UserSubscibeConfig userSubscibeConfig, RedirectAttributes redirectAttributes) {
		String result = null;
		try {
			userSubscibeConfigService.delete(userSubscibeConfig);
			result = ServiceUtil.getMessage(RespCodeEnum.U0, "");
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