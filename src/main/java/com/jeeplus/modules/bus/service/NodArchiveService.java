/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.BaseEntity;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.dao.NodArchiveDao;
import com.jeeplus.modules.bus.entity.NodArchive;

/**
 * 附件存储Service
 * @author zhangsc
 */
@Service
@Transactional(readOnly = true)
public class NodArchiveService extends CrudService<NodArchiveDao, NodArchive> {
	
	
	public NodArchive get(String id) {
		return super.get(id);
	}
	
	/**
	 * 使用地域权限验证的方式获取单个nodArchive
	 * @param nodArchive
	 * @return
	 */
	public NodArchive getByValidate(NodArchive nodArchive){
		return dao.getByValidate(nodArchive);
	}
	
	public List<NodArchive> findList(NodArchive nodArchive) {
		return super.findList(nodArchive);
	}
	
	public Page<NodArchive> findPage(Page<NodArchive> page, NodArchive nodArchive) {
		return super.findPage(page, nodArchive);
	}
	
	@Transactional(readOnly = false)
	public void save(NodArchive nodArchive) {
		super.save(nodArchive);
	}
	
	@Transactional(readOnly = false)
	public void delete(NodArchive nodArchive) {
		super.delete(nodArchive);
	}
	
	public List<NodArchive> findListByIds(List<String> ids) {
		return dao.findListByIds(ids,BaseEntity.DEL_FLAG_NORMAL);
	}
	
}