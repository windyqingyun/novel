/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.laikan.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.laikan.entity.LaikanBalance;

/**
 * 来看用户余额DAO接口
 * @author zhangsc
 * @version 2018-01-17
 */
@MyBatisDao
public interface LaikanBalanceDao extends CrudDao<LaikanBalance> {

	
}