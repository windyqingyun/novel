package com.jeeplus.modules.bus.history.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.history.entity.UserFavorites;
import com.jeeplus.modules.bus.history.service.UserFavoritesService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;

/**
 * 用户收藏记录Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/service/interface/userFavorites")
public class UserFavoritesController extends BaseController {

	@Autowired
	private UserFavoritesService userFavoritesService;
	
	/**
	 * 获取用户的收藏列表
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "list", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String list(HttpServletRequest request,
			@RequestParam("userId")String userId, 
			@RequestParam(name = "officeId", required = false)String officeId){
		userId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(userId)) {
			return ServiceUtil.doResponse(RespCodeEnum.U6, true);
		}
		String result = null;
		try {
			result = userFavoritesService.findList(userId, officeId);
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
	 * 添加收藏
	 */
	@RequestMapping(value = "add", method=RequestMethod.POST, 
			produces = { "application/json; charset=UTF-8" })
	public String save(HttpServletRequest request,
			@RequestParam("userId")String userId, 
			@RequestParam("fodderId")String fodderId, 
			@RequestParam(name = "officeId", required=false)String officeId) {
		userId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(userId)) {
			return ServiceUtil.doResponse(RespCodeEnum.U6, true);
		}
		String result = null;
		try {
			result = userFavoritesService.insert(userId, fodderId);
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
	 * 删除用户收藏（支持多条删除）
	 */
	@RequestMapping(value = "delete", method=RequestMethod.POST, 
			produces = { "application/json; charset=UTF-8" })
	public String delete(@RequestParam("ids")String[] ids) {
		String result = null;
		try {
			userFavoritesService.deleteByIds(ids);
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
	
	/**
	 * 删除用户收藏（支持多条删除）
	 */
	@RequestMapping(value = "cancel", method=RequestMethod.POST, 
			produces = { "application/json; charset=UTF-8" })
	public String cancel(@RequestParam("id")String id) {
		String result = null;
		try {
			userFavoritesService.delete(new UserFavorites(id));
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
	
	/**
	 * 判断用户是否存在某一个素材的收藏
	 * @param userId
	 * @param fodderId
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "existsFavorite", method=RequestMethod.POST, 
			produces = { "application/json; charset=UTF-8" })
	public String existsFavorite(HttpServletRequest request,
			@RequestParam("officeId")String officeId, 
			@RequestParam("userId")String userId, 
			@RequestParam("fodderId")String fodderId){
		userId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(userId)) {
			return ServiceUtil.doResponse(RespCodeEnum.U6, true);
		}
		String result = null;
		try {
			UserFavorites userFavorites = userFavoritesService.existsFavorite(userId, fodderId);
			Map dataMap = Maps.newHashMap();
			if(userFavorites != null && StringUtils.isNotBlank(userFavorites.getId())) {
				dataMap.put("id", userFavorites.getId());
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
}