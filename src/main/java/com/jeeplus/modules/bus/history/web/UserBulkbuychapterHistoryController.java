/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.web;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.enums.BulkBuyTypeEnum;
import com.jeeplus.modules.bus.history.entity.UserBulkbuychapterHistory;
import com.jeeplus.modules.bus.history.service.UserBulkbuychapterHistoryService;

/**
 * 用户多章购买Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/service/interface/userBulkbuychapterHistory")
public class UserBulkbuychapterHistoryController extends BaseController {

	@Autowired
	private UserBulkbuychapterHistoryService userBulkbuychapterHistoryService;

	/**
	 * 多章购买规则
	 */
	@RequestMapping(value = "bulkBuyChooses", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String bulkBuyChooses(UserBulkbuychapterHistory userBulkbuychapterHistory) throws Exception{
		JSONArray bulkBuyChooses = new JSONArray();
		
		for (BulkBuyTypeEnum e : BulkBuyTypeEnum.values()) {
			UserBulkbuychapterHistory bulkbuychapterHistory = (UserBulkbuychapterHistory) BeanUtils.cloneBean(userBulkbuychapterHistory);
			bulkbuychapterHistory.setBulkBuyTypeCode(e.getCode());
			
			//discount code des originalPrice payCoin
			JSONObject obj = e.convetToJson();
			bulkbuychapterHistory = userBulkbuychapterHistoryService.calculatePrice(bulkbuychapterHistory);
			obj.put("originalPrice", bulkbuychapterHistory.getOriginalprice());
			obj.put("payCoin", bulkbuychapterHistory.getPayCoin());
			
			
			bulkBuyChooses.add(obj);
		}
		
		return bulkBuyChooses.toJSONString();
	}
	
	/**
	 * 用户多章购买
	 */
	@RequestMapping(value = "bulkBuy", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String bulkBuy(UserBulkbuychapterHistory userBulkbuychapterHistory) throws Exception{
		String result = null;
		try {
			userBulkbuychapterHistoryService.bulkBuy(userBulkbuychapterHistory);
			result = ServiceUtil.getMessage(RespCodeEnum.U0, "支付成功");
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
	 * 计算支付金额
	 */
	@RequestMapping(value = "calculatePrice", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String calculatePrice(UserBulkbuychapterHistory userBulkbuychapterHistory) throws Exception{
		String result = null;
		try {
			userBulkbuychapterHistory = userBulkbuychapterHistoryService.calculatePrice(userBulkbuychapterHistory);
			result = ServiceUtil.getMessage(RespCodeEnum.U0, userBulkbuychapterHistory);
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