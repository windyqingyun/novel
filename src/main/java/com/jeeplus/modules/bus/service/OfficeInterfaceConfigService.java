/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.dao.OfficeInterfaceConfigDao;
import com.jeeplus.modules.bus.entity.OfficeInterfaceConfig;

/**
 * 机构接口配置Service
 * @author zhangsc
 * @version 2017-11-07
 */
@Service
@Transactional(readOnly = true)
public class OfficeInterfaceConfigService extends CrudService<OfficeInterfaceConfigDao, OfficeInterfaceConfig> {
	/** 机构接口配置缓存名称 */
	public static final String CACHE_OFFICE_INTERFACE_CONFIG = "office_interface_config";
	/** 接口接口配置键值  与结构id拼接作为key获取机构接口配置 */
	public static final String OFFICE_INTERFACE_CONFIG_KEY = "cid";
	
	public OfficeInterfaceConfig get(String id) {
		return super.get(id);
	}
	
	/**
	 * 根据机构编号获取机构接口配置
	 * @param officeId
	 * @return
	 */
	public OfficeInterfaceConfig getOfficeInterfaceConfigByOffice(String officeId) {
		OfficeInterfaceConfig officeInterfaceConfig = (OfficeInterfaceConfig) CacheUtils.get(CACHE_OFFICE_INTERFACE_CONFIG, OFFICE_INTERFACE_CONFIG_KEY+officeId);
		if(officeInterfaceConfig == null){
			officeInterfaceConfig = dao.getOfficeInterfaceConfigByOffice(officeId);
			if(officeInterfaceConfig == null || StringUtils.isBlank(officeInterfaceConfig.getId())) {
				return null;
			}
			CacheUtils.put(CACHE_OFFICE_INTERFACE_CONFIG, OFFICE_INTERFACE_CONFIG_KEY+officeInterfaceConfig.getOffice().getId(), officeInterfaceConfig);
		}
		
		return officeInterfaceConfig;
	}
	
	public List<OfficeInterfaceConfig> findList(OfficeInterfaceConfig officeInterfaceConfig) {
		return super.findList(officeInterfaceConfig);
	}
	
	public Page<OfficeInterfaceConfig> findPage(Page<OfficeInterfaceConfig> page, OfficeInterfaceConfig officeInterfaceConfig) {
		return super.findPage(page, officeInterfaceConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(OfficeInterfaceConfig officeInterfaceConfig) {
		super.save(officeInterfaceConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(OfficeInterfaceConfig officeInterfaceConfig) {
		super.delete(officeInterfaceConfig);
	}
	
	public List<OfficeInterfaceConfig> findAll() {
		return dao.findAllList(new OfficeInterfaceConfig());
	}
	
	public Map<String, OfficeInterfaceConfig> findAllMap() {
		Map<String, OfficeInterfaceConfig> configMap = Maps.newHashMap();
		
		List<OfficeInterfaceConfig> list = findAll();
		if(list!= null && !list.isEmpty()) {
			for (OfficeInterfaceConfig config : list) {
				configMap.put(config.getId(), config);
			}
		}
		
		return configMap;
	}
}