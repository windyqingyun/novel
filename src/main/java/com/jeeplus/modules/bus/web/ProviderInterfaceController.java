package com.jeeplus.modules.bus.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.API.ChinaCalendarInterface;
import com.jeeplus.modules.bus.API.ProviderInterface;
import com.jeeplus.modules.bus.entity.ProviderUser;
import com.jeeplus.modules.bus.service.ProviderUserService;
import com.jeeplus.modules.sys.service.OfficeService;

/**
 * 内容提供端接口管理
 * @author Administrator
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/service/interface/provider")
public class ProviderInterfaceController extends BaseController {
	private static OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
	
	@Autowired
	private ProviderUserService providerUserService;
	
	public static Map<String, ProviderInterface> providerInterFaceMap = null;
	
	/** 中华万年历机构编号 **/
	private static final String CHINA_CALENDAR_OFFICE_ID = "200000002001";
	
	
	static{
		providerInterFaceMap = Maps.newHashMap();
		
		//中华万年历的接口
		providerInterFaceMap.put(CHINA_CALENDAR_OFFICE_ID, new ChinaCalendarInterface(officeService.get(CHINA_CALENDAR_OFFICE_ID)));
	}
	
	/**
	 * 注册用户
	 * @param para
	 * @param officeId
	 * @return
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String registerOrLogin(@RequestParam("para")String para, @RequestParam("officeId")String officeId){
		String result = null;
		try {
			String userId = JSONObject.parseObject(para).getString("userId");
			
			ProviderUser orginalUser = providerUserService.getUserByOfficeAndOriginalId(officeId, userId);
			if(orginalUser == null || StringUtils.isBlank(orginalUser.getId())){
				//获取接口
				ProviderInterface providerInterface = providerInterFaceMap.get(officeId);
				
				ProviderUser user = providerInterface.getUserInfo(para, userId);
				user.setId(user.getOriginalId());
				providerUserService.save(user);
			}
			
			result = ServiceUtil.doResponse(RespCodeEnum.U0, true);
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
	 * 获取登录地址
	 * @param id 机构id
	 * @return
	 */
	@RequestMapping(value = "getLoginUrl", method = RequestMethod.POST)
	public String getLoginUrl(@RequestParam("id")String id){
		String result = null;
		
		try {
			//获取接口
			ProviderInterface providerInterface = providerInterFaceMap.get(id);
			
			result = ServiceUtil.getMessage(RespCodeEnum.U0, (Object)providerInterface.getLoginUrl());
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
