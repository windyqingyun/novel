/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.OfficeInterfaceConfig;

/**
 * 机构接口配置DAO接口
 * @author zhangsc
 * @version 2017-11-07
 */
@MyBatisDao
public interface OfficeInterfaceConfigDao extends CrudDao<OfficeInterfaceConfig> {

	OfficeInterfaceConfig getOfficeInterfaceConfigByOffice(@Param("officeId")String officeId);
}