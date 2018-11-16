/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.ListUtils;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.history.dao.UserReadHistoryDao;
import com.jeeplus.modules.bus.history.entity.UserReadHistory;
import com.jeeplus.modules.bus.utils.JsonFieldConst;
import com.jeeplus.modules.sys.entity.User;

/**
 * 用户阅读记录Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserReadHistoryService extends CrudService<UserReadHistoryDao, UserReadHistory> {

	public UserReadHistory get(String id) {
		return super.get(id);
	}
	
	public List<UserReadHistory> findList(UserReadHistory userReadHistory) {
		return super.findList(userReadHistory);
	}
	
	public Page<UserReadHistory> findPage(Page<UserReadHistory> page, UserReadHistory userReadHistory) {
		return super.findPage(page, userReadHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(UserReadHistory userReadHistory) {
		super.save(userReadHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserReadHistory userReadHistory) {
		super.delete(userReadHistory);
	}
	
	public UserReadHistory getUserReadHistory(UserReadHistory userReadHistory){
		List<UserReadHistory> list = findList(userReadHistory);
		if(ListUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
	
	/** 根据用户id获取用户的阅读历史记录 */
	public UserReadHistory getUserReadHistoryByCustomerId(UserReadHistory userReadHistory){
		List<UserReadHistory> list = dao.getUserReadHistoryByCustomerId(userReadHistory);
		if(ListUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
	
	
	@Transactional
	public void insert(UserReadHistory userReadHistory){
		Date date = new Date();
		userReadHistory.setId(IdGen.uuid());
		userReadHistory.setCreateDate(date);
		userReadHistory.setUpdateDate(date);
		dao.insert(userReadHistory);
	}
	
	@Transactional
	public void update(UserReadHistory userReadHistory){
		userReadHistory.setUpdateDate(new Date());
		dao.update(userReadHistory);
	}
	
	/**
	 * 获取最后阅读的信息
	 * @return
	 */
	public Map getLastViewInfo(String userId, String officeId){
		return dao.getLastViewInfo(userId, officeId);
	}
	
	/**
	 * 获取用户阅读数量
	 * @param userReadHistory
	 * @return
	 */
	public Integer getViewCount(String userId, String officeId){
		return dao.getViewCount(userId, officeId);
	}
	
	@SuppressWarnings("all")
	public List<Map> findReadHistoryInfoList(UserReadHistory userReadHistory){
		return dao.findReadHistoryInfoList(userReadHistory);
	}
	
	public String findList(String userId, String officeId){
		Map resultMap = Maps.newHashMap();
		
		User user = new User(userId);
		UserReadHistory userReadHistory = new UserReadHistory();
		userReadHistory.setCreateBy(user);
		//如果阅读机构不为空，要设置来源机构id
		if(StringUtils.isNotBlank(officeId)) {
			userReadHistory.setOfficeId(officeId);
		}
		List<UserReadHistory> userReadHistoryList = findList(userReadHistory);
		List<Map> userReadHistoryListMap = Lists.newArrayList();
		if(ListUtils.isNotEmpty(userReadHistoryList)) {
			for (UserReadHistory readHistory : userReadHistoryList) {
				userReadHistoryListMap.add(convertUserReadHistoryToMap(readHistory));
			}
		}
		resultMap.put(JsonFieldConst.LIST, userReadHistoryListMap);
		
		return ServiceUtil.getMessage(RespCodeEnum.U0, resultMap);
	}
	
	
	public Map convertUserReadHistoryToMap(UserReadHistory userReadHistory) {
		Map map = Maps.newHashMap();
		map.put("id", userReadHistory.getId());
		map.put("fodderId", userReadHistory.getFodderId());
		map.put("bookId", userReadHistory.getBookId());
		map.put("chapter", userReadHistory.getChapter());
		
		return map;
	}
	
}