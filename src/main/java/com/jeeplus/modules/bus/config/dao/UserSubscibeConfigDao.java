/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.config.dao;

import java.util.List;
import java.util.Map;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.config.entity.UserSubscibeConfig;

/**
 * 用户订阅配置DAO接口
 * @author zhangsc
 * @version 2017-11-03
 */
@MyBatisDao
public interface UserSubscibeConfigDao extends CrudDao<UserSubscibeConfig> {
	public List<Map> findSubscibeInfoList(UserSubscibeConfig userSubscibeConfig);
}