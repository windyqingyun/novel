/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.RechargeRule;

/**
 * 充值规则DAO接口
 * @author zhangsc
 * @version 2017-11-29
 */
@MyBatisDao
public interface RechargeRuleDao extends CrudDao<RechargeRule> {
	
}