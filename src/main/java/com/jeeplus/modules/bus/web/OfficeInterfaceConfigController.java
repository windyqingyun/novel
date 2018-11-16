package com.jeeplus.modules.bus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.OfficeInterfaceConfig;
import com.jeeplus.modules.bus.service.OfficeInterfaceConfigService;

/**
 * 机构接口配置Controller
 * @author zhangsc
 * @version 2017-11-07
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/service/interface/officeInterfaceConfig")
public class OfficeInterfaceConfigController extends BaseController {

	@Autowired
	private OfficeInterfaceConfigService officeInterfaceConfigService;

	/**
	 * 获取小说站接口信息
	 */
	@RequestMapping(value = "getConfigInfo", method = RequestMethod.POST,
			produces = { "application/json; charset=UTF-8" })
	public String getConfigInfo(@RequestParam("officeId")String officeId) {
		String result = null;
		try {
			OfficeInterfaceConfig config = officeInterfaceConfigService.getOfficeInterfaceConfigByOffice(officeId);
			if(config != null && StringUtils.isNotBlank(config.getId())){
				if(config.getEnable().equals(Global.YES)){
					//如果接口已开启，返回接口信息
					result = ServiceUtil.getMessage(RespCodeEnum.U0, config);
				}else {
					//如果接口未开启，不返回接口信息
					result = ServiceUtil.getMessage(RespCodeEnum.U5, "该小说站接口未启用");
				}
			}else{
				result = ServiceUtil.getMessage(RespCodeEnum.U5, "获取不到小说站配置信息");
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