/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.bus.history.dao.ViewIpHistoryDao;
import com.jeeplus.modules.bus.history.entity.ViewIpHistory;

/**
 * 用户阅读记录Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class ViewIpHistoryService extends CrudService<ViewIpHistoryDao, ViewIpHistory> {

	public ViewIpHistory get(String id) {
		return super.get(id);
	}
	
	public List<ViewIpHistory> findList(ViewIpHistory viewIpHistory) {
		return super.findList(viewIpHistory);
	}
	
	public Page<ViewIpHistory> findPage(Page<ViewIpHistory> page, ViewIpHistory viewIpHistory) {
		return super.findPage(page, viewIpHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(ViewIpHistory viewIpHistory) {
		super.save(viewIpHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(ViewIpHistory viewIpHistory) {
		super.delete(viewIpHistory);
	}
	
	@Transactional(readOnly = false)
	public void insert(ViewIpHistory viewIpHistory){
		viewIpHistory.setId(IdGen.uuid());
		viewIpHistory.setCreateDate(new Date());
		dao.insert(viewIpHistory);
	}
	
	@Transactional(readOnly = false)
	public void update(ViewIpHistory viewIpHistory){
		dao.update(viewIpHistory);
	}
}