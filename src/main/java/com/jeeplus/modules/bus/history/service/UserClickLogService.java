package com.jeeplus.modules.bus.history.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.bus.history.dao.UserClickLogDao;
import com.jeeplus.modules.bus.history.entity.UserClickLog;
import com.jeeplus.modules.bus.history.entity.example.UserClickLogExample;

@Service
@Transactional(readOnly = true)
public class UserClickLogService {
	
	@Autowired
	private UserClickLogDao userClickLogDao;

	public int countByExample(UserClickLogExample example) {
		return userClickLogDao.countByExample(example);
	}

//	@Transactional(readOnly = false)
	public int deleteByExample(UserClickLogExample example) {
		return userClickLogDao.deleteByExample(example);
	}

//	@Transactional(readOnly = false)
	public int deleteByPrimaryKey(Long id) {
		return userClickLogDao.deleteByPrimaryKey(id);
	}

	@Transactional(readOnly = false)
	public int insert(UserClickLog record) {
		return userClickLogDao.insert(record);
	}

	@Transactional(readOnly = false)
	public int insertSelective(UserClickLog record) {
		return userClickLogDao.insertSelective(record);
	}

	
	public List<UserClickLog> selectByExample(UserClickLogExample example) {
		return userClickLogDao.selectByExample(example);
	}

	
	public UserClickLog selectByPrimaryKey(Long id) {
		return userClickLogDao.selectByPrimaryKey(id);
	}

	@Transactional(readOnly = false)
	public int updateByExampleSelective(UserClickLog record, UserClickLogExample example) {
		return userClickLogDao.updateByExampleSelective(record, example);
	}

	@Transactional(readOnly = false)
	public int updateByExample(UserClickLog record, UserClickLogExample example) {
		return userClickLogDao.updateByExample(record, example);
	}

	@Transactional(readOnly = false)
	public int updateByPrimaryKeySelective(UserClickLog record) {
		return userClickLogDao.updateByPrimaryKeySelective(record);
	}

	@Transactional(readOnly = false)
	public int updateByPrimaryKey(UserClickLog record) {
		return userClickLogDao.updateByPrimaryKey(record);
	}

}
