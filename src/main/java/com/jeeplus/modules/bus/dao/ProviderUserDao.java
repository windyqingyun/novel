/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.ProviderUser;

/**
 * 流量提供端用户DAO接口
 * @author zhangsc
 * @version 2017-12-22
 */
@MyBatisDao
public interface ProviderUserDao extends CrudDao<ProviderUser> {

	
}