/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ListUtils;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.Customer;
import com.jeeplus.modules.bus.history.entity.UserBuychapterHistory;
import com.jeeplus.modules.bus.history.service.UserBuychapterHistoryService;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.service.OfficeCoinConfigService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.OriginUtil;

/**
 * 用户章节购买记录Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/service/interface/userBuychapterHistory")
public class UserBuychapterHistoryController extends BaseController {

	@Autowired
	private UserBuychapterHistoryService userBuychapterHistoryService;
	@Autowired
	private OfficeCoinConfigService officeCoinConfigService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private BookService bookService;
	
	/**
	 * 单章购买
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "signgleBuy", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String signgleBuy(UserBuychapterHistory userBuychapterHistory, HttpServletRequest request) {
		Customer currentCustomer = CurrentCustomerUtil.getCurrentCustomer(request);
		if (currentCustomer == null) {
			return ServiceUtil.getMessage(RespCodeEnum.U6, "未登录");
		}
		String result = null;
		try {
			UserBuychapterHistory t = userBuychapterHistoryService.getUserBuyChapterHistory(userBuychapterHistory);
			if(t != null && StringUtils.isNotBlank(t.getId())){
				result = ServiceUtil.getMessage(RespCodeEnum.U4, "您已购买此章节，无需重复购买");
			}else {
				// 支付请求来源哪个流量端
				userBuychapterHistory.setPayOrigin(OriginUtil.getOfficeIdByOrigin(request));
				// 支付给那个机构
				userBuychapterHistory.setOfficeId(bookService.getBookOfficeId(userBuychapterHistory.getBookId()));
				
				userBuychapterHistoryService.signgleBuy(userBuychapterHistory);
				result = ServiceUtil.getMessage(RespCodeEnum.U0, "支付成功");
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
	 * 消费记录
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "findExpenseList", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String findExpenseList(HttpServletRequest request,
			@RequestParam("userId")String userId,
			@RequestParam(name = "officeId", required = false)String officeId) {
		userId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(userId)) {
			return ServiceUtil.doResponse(RespCodeEnum.U6, true);
		}
		String result = null;
		try {
			/*
			 * map包含的字段 "payCoin", "pay_ticket", "chapter", "title", "officeId", "payDate" payDate格式为yyyy-MM-dd HH:mm:ss coinName
			 */
			List<Map> mapList = userBuychapterHistoryService.findExpenseList(userId, officeId);
			if(mapList != null && ListUtils.isNotEmpty(mapList)){
				for (Map map : mapList) {
					String bookOfficeId = (String) map.get("officeId");
					if(StringUtils.isNotBlank(bookOfficeId)){
						map.put("coinName", officeCoinConfigService.getCoinNameByOfficeId(bookOfficeId));
					}
				}
			}
			
			result = ServiceUtil.getMessage(RespCodeEnum.U0, mapList);
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
	 * 已购列表
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "findBoughtList", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String findBoughtList(HttpServletRequest request,
			@RequestParam("userId")String userId ,
			@RequestParam(name = "officeId", required = false)String officeId) {
		userId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(userId)) {
			return ServiceUtil.getMessage(RespCodeEnum.U6, true);
		}
		String result = null;
		try {
			/* 
			 * 如果officeId为空，查询用户所有的已购列表
			 * map包含的字段  "chapter", "chapterTitle", "officeId",  "payDate", "title", "titleImage" payDate格式为yyyy-MM-dd HH:mm:ss coinName
			 */
			List<Map> mapList = userBuychapterHistoryService.findBoughtList(userId, StringUtils.isNotBlank(officeId) ? 
					Lists.newArrayList(officeId) : officeService.findEnabledProviderId());
			
			result = ServiceUtil.getMessage(RespCodeEnum.U0, mapList);
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