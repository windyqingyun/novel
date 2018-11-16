/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.ListUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.dao.UserWalletDao;
import com.jeeplus.modules.bus.dto.UserWalletDto;
import com.jeeplus.modules.bus.entity.UserWallet;
import com.jeeplus.modules.bus.history.service.UserReadHistoryService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

/**
 * 用户钱包Service
 * @author zhangsc
 * @version 2017-11-29
 */
@Service
@Transactional(readOnly = true)
public class UserWalletService extends CrudService<UserWalletDao, UserWallet> {

	@Autowired
	private UserReadHistoryService userReadHistoryService;
	
	public UserWallet get(String id) {
		return super.get(id);
	}
	
	public List<UserWallet> findList(UserWallet userWallet) {
		return super.findList(userWallet);
	}
	
	public Page<UserWallet> findPage(Page<UserWallet> page, UserWallet userWallet) {
		return super.findPage(page, userWallet);
	}
	
	@Transactional(readOnly = false)
	public void save(UserWallet userWallet) {
		super.save(userWallet);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserWallet userWallet) {
		super.delete(userWallet);
	}
	
	public UserWallet getUserWallet(UserWallet userWallet){
		List<UserWallet> list = findList(userWallet);
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 获取用户钱包
	 * @param userId
	 * @param officeId
	 * @return
	 */
	@Transactional(readOnly = false)
	public UserWallet getUserWalletByOfficeId(String userId, String officeId){
		UserWallet searchBean = new UserWallet();
		searchBean.setCreateBy(new User(userId));
		searchBean.setOffice(new Office(officeId));
		
		//如果获取的为空，那么新建一个钱包
		UserWallet userWallet = getUserWallet(searchBean);
		if(userWallet == null || StringUtils.isBlank(userWallet.getId())){
			userWallet = createNewWallet(userId, officeId);
		}
		userWallet = getUserWallet(searchBean);
		
		return userWallet;
	}
	
	@Transactional(readOnly = false)
	public UserWallet createNewWallet(String userId, String officeId){
		UserWallet userWallet = new UserWallet();
		userWallet.setCreateBy(new User(userId));
		userWallet.setOffice(new Office(officeId));
		userWallet.setCoin(new BigDecimal(0));
		userWallet.setTicket(new BigDecimal(0));
		userWallet.setCreateDate(new Date());
		userWallet.setId(IdGen.uuid());
		
		dao.insert(userWallet);
		
		return userWallet;
	}
	
	/**
	 * 更改用户余额，余额采用+-的方式进行更新
	 * @param id
	 * @param addCoin 要添加的阅读币  减少传入负数，不改变传0
	 * @param addTicket 要添加的阅读券  减少传入负数，不改变传0
	 */
	@Transactional(readOnly = false)
	public void addCoinAndTicket(String id, BigDecimal addCoin, BigDecimal addTicket){
		dao.addCoinAndTicket(id, addCoin, addTicket);
	}
	
	/**
	 * 获取用户钱包Dto用于个人中心展示
	 * @param userId
	 * @param officeId
	 * @return
	 */
	@Transactional(readOnly = false)
	public UserWalletDto getUserWalletDtoByOffice(String userId, String officeId){
		UserWalletDto userWalletDto = new UserWalletDto(getUserWalletByOfficeId(userId, officeId));
		
		Map map = userReadHistoryService.getLastViewInfo(userId, officeId);
		if(map != null){
			userWalletDto.setLastViewName((String)map.get("lastViewTitle"));
			userWalletDto.setLastViewChapterName((String)map.get("bookChapterTitle"));
		}
		
		userWalletDto.setViewCount(userReadHistoryService.getViewCount(userId, officeId));
		
		return userWalletDto;
	}
}