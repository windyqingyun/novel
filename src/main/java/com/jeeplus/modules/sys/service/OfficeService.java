/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.TreeService;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.ListUtils;
import com.jeeplus.modules.sys.dao.OfficeDao;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * @author jeeplus
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {
	/** 内容提供端的机构id，根据此id可以获取内容提供端下的机构 **/
	public static final String PROVIDER_PARENT_ID = "3b57e95561d84d82959aaae8211d6d0a";
	/** 内容提供端口的机构id列表  只放已启用的 **/
	public static final String CACHE_PROVIDER_OFFICE_IDS = "cache_provider_office_ids";
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getOfficeAllList();
		}else{
			return UserUtils.getOfficeList();
		}
	}
	
	@Transactional(readOnly = true)
	public List<Office> findList(Office office){
		office.setParentIds(office.getParentIds()+"%");
		return dao.findByParentIdsLike(office);
	}
	
	@Transactional(readOnly = true)
	public Office getByCode(String code){
		return dao.getByCode(code);
	}
	
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		CacheUtils.remove(CACHE_PROVIDER_OFFICE_IDS);
	}
	
	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		CacheUtils.remove(CACHE_PROVIDER_OFFICE_IDS);
	}
	
	/**
	 * 获取可用的内容提供端的机构id 从缓存中获取
	 * @return
	 */
	public List<String> findEnabledProviderId(){
		@SuppressWarnings("unchecked")
		List<String> providerIdList = (List<String>) CacheUtils.get(CACHE_PROVIDER_OFFICE_IDS);
		if(providerIdList == null) {
			providerIdList = dao.findEnabledProviderId(PROVIDER_PARENT_ID);
			if(providerIdList != null && ListUtils.isNotEmpty(providerIdList)){
				CacheUtils.put(CACHE_PROVIDER_OFFICE_IDS, providerIdList);
			}
		}
		
		return providerIdList;
	}
	
}
