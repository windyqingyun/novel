/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.modules.bus.dao.RechargeRuleDao;
import com.jeeplus.modules.bus.entity.RechargeRule;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 充值规则Service
 * @author zhangsc
 * @version 2017-11-29
 */
@Service
@Transactional(readOnly = true)
public class RechargeRuleService extends CrudService<RechargeRuleDao, RechargeRule> {

	/** 机构接口配置缓存名称 */
	public static final String CACHE_RECHARGE_RULE = "recharge_rule";
	/** 接口接口配置键值  与结构id拼接作为key获取机构接口配置 */
	public static final String OFFICE_RECHARGE_RULE_KEY = "rid";
	
	/**
	 * 根据机构id获取充值规则
	 * @param officeId
	 * @return
	 */
	public List<RechargeRule> findRuleListByOffice(String officeId)throws Exception{
		@SuppressWarnings("unchecked")
		ArrayList<RechargeRule> ruleList =  (ArrayList<RechargeRule>)CacheUtils.get(CACHE_RECHARGE_RULE, OFFICE_RECHARGE_RULE_KEY+officeId);
		if(ruleList == null || ruleList.isEmpty()){
			RechargeRule searchBean = new RechargeRule();
			searchBean.setOffice(new Office(officeId));
			searchBean.setEnable(Global.YES);
			
			ruleList = (ArrayList)findList(searchBean);
			if(ruleList == null || ruleList.isEmpty()) {
				return null;
			}
			CacheUtils.put(CACHE_RECHARGE_RULE, OFFICE_RECHARGE_RULE_KEY+officeId, ruleList);
		}
		
		return ( List<RechargeRule>) ruleList.clone();
	}
	
	public RechargeRule get(String id) {
		return super.get(id);
	}
	
	public List<RechargeRule> findList(RechargeRule rechargeRule) {
		return super.findList(rechargeRule);
	}
	
	public Page<RechargeRule> findPage(Page<RechargeRule> page, RechargeRule rechargeRule) {
		return super.findPage(page, rechargeRule);
	}
	
	@Transactional(readOnly = false)
	public void save(RechargeRule rechargeRule) {
		super.save(rechargeRule);
		//清除缓存
		CacheUtils.remove(CACHE_RECHARGE_RULE, OFFICE_RECHARGE_RULE_KEY+rechargeRule.getOffice().getId());
	}
	
	@Transactional(readOnly = false)
	public void delete(RechargeRule rechargeRule) {
		rechargeRule = get(rechargeRule);
		super.delete(rechargeRule);
		//清除缓存
		CacheUtils.remove(CACHE_RECHARGE_RULE, OFFICE_RECHARGE_RULE_KEY+rechargeRule.getOffice().getId());
	}
	
}