/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.ListUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.history.dao.UserClickHistoryDao;
import com.jeeplus.modules.bus.history.entity.UserClickHistory;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.service.FodderService;

/**
 * 用户点击记录Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserClickHistoryService extends CrudService<UserClickHistoryDao, UserClickHistory> {

	@Autowired
	private FodderService fodderService;
	@Autowired
	private BookService bookService;
	
	public UserClickHistory get(String id) {
		return super.get(id);
	}
	
	public List<UserClickHistory> findList(UserClickHistory userClickHistory) {
		return super.findList(userClickHistory);
	}
	
	public Page<UserClickHistory> findPage(Page<UserClickHistory> page, UserClickHistory userClickHistory) {
		return super.findPage(page, userClickHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(UserClickHistory userClickHistory) {
		super.save(userClickHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserClickHistory userClickHistory) {
		super.delete(userClickHistory);
	}
	
	/**
	 * 是否存在点击历史
	 * @return
	 */
	public UserClickHistory getClickHistory(UserClickHistory userClickHistory){
		List<UserClickHistory> list = Lists.newArrayList();
		if(ListUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
	
	@Transactional(readOnly = false)
	public void insert(UserClickHistory userClickHistory){
		Date date = new Date();
		userClickHistory.setId(IdGen.uuid());
		userClickHistory.setCreateDate(date);
		dao.insert(userClickHistory);
		//添加素材或者小说的点击次数
		addFodderOrBookClickCount(userClickHistory);
	}

	/**
	 * 添加素材和小说的点击次数
	 * @param userClickHistory
	 */
	@Transactional(readOnly = false)
	public void addFodderOrBookClickCount(UserClickHistory userClickHistory){
		if(StringUtils.isNotBlank(userClickHistory.getBookId())){
			bookService.addClickCount(userClickHistory.getBookId());
		}
		if(StringUtils.isNotBlank(userClickHistory.getFodderId())){
			fodderService.addClickCount(userClickHistory.getFodderId());
		}
	}
	
	@Transactional(readOnly = false)
	public void update(UserClickHistory userClickHistory){
		Date date = new Date();
		userClickHistory.setUpdateDate(date);
		dao.update(userClickHistory);
		//添加素材或者小说的点击次数
		addFodderOrBookClickCount(userClickHistory);
	}
}