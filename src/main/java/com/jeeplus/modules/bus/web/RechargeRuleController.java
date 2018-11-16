/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ListUtils;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.RechargeRule;
import com.jeeplus.modules.bus.service.OfficeCoinConfigService;
import com.jeeplus.modules.bus.service.RechargeRuleService;

/**
 * 充值规则Controller
 * @author zhangsc
 * @version 2017-11-29
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/service/interface/rechargeRule")
public class RechargeRuleController extends BaseController {

	@Autowired
	private RechargeRuleService rechargeRuleService;
	@Autowired
	private OfficeCoinConfigService officeCoinConfigService;
	
	/**
	 * 根据机构获取对应的充值规则
	 * @param officeId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String list(@RequestParam("officeId")String officeId, @RequestParam(value = "isFirstRecharge", required = false)String isFirstRecharge) {
		String result = null;
		try {
			List<RechargeRule> list = rechargeRuleService.findRuleListByOffice(officeId);
			if(ListUtils.isEmpty(list)){
				result = ServiceUtil.getMessage(RespCodeEnum.U5, "该小说站尚无充值规则");
			}else {
				for (RechargeRule rechargeRule : list) {
					rechargeRule.setIsFirstCharge(Global.NO);
				}
				//如果规则的数量大约4个  保留前4个，其他的删除，list是按照金额进行的 顺序排序
				if(list.size() > 4){
					list = list.subList(0, 4);
				}
				if(StringUtils.isNotBlank(isFirstRecharge) && isFirstRecharge.equals(Global.YES)){
					RechargeRule rule = new RechargeRule();
					rule.setIsFirstCharge(Global.YES);
					rule.setPrice(new BigDecimal(10));
					rule.setRstCoin(officeCoinConfigService.getOfficeCoinConfigByOffice(officeId).getCoinRate().multiply(rule.getPrice()));
					list.add(rule);
				}
				result = ServiceUtil.getMessage(RespCodeEnum.U0, list);
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