/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.config.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.ListUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.config.dao.UserSubscibeConfigDao;
import com.jeeplus.modules.bus.config.entity.UserSubscibeConfig;

/**
 * 用户订阅配置Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserSubscibeConfigService extends CrudService<UserSubscibeConfigDao, UserSubscibeConfig> {

	public UserSubscibeConfig get(String id) {
		return super.get(id);
	}
	
	public List<UserSubscibeConfig> findList(UserSubscibeConfig userSubscibeConfig) {
		return super.findList(userSubscibeConfig);
	}
	
	public Page<UserSubscibeConfig> findPage(Page<UserSubscibeConfig> page, UserSubscibeConfig userSubscibeConfig) {
		return super.findPage(page, userSubscibeConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(UserSubscibeConfig userSubscibeConfig) {
		Date now = new Date();
		if(StringUtils.isBlank(userSubscibeConfig.getId())){
			userSubscibeConfig.setId(IdGen.uuid());
			userSubscibeConfig.setCreateDate(now);
			userSubscibeConfig.setUpdateDate(now);
			dao.insert(userSubscibeConfig);
		}else {
			userSubscibeConfig.setUpdateDate(now);
			dao.update(userSubscibeConfig);
		}
	}
	
	public List<Map> findSubscibeInfoList(UserSubscibeConfig userSubscibeConfig) {
		return dao.findSubscibeInfoList(userSubscibeConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserSubscibeConfig userSubscibeConfig) {
		super.delete(userSubscibeConfig);
	}
	
	/**
	 * 获取订阅信息
	 * @param userId
	 * @param fodderId
	 * @return
	 */
	public UserSubscibeConfig getUserSubscibeConfig(UserSubscibeConfig userSubscibeConfig){
		List<UserSubscibeConfig> list = findList(userSubscibeConfig);
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}