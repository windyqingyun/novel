/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

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
import com.jeeplus.modules.bus.entity.UserWallet;
import com.jeeplus.modules.bus.history.dao.UserBuychapterHistoryDao;
import com.jeeplus.modules.bus.history.entity.UserBuychapterHistory;
import com.jeeplus.modules.bus.service.BookChapterService;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.service.UserWalletService;
import com.jeeplus.modules.sys.entity.User;

/**
 * 用户章节购买记录Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserBuychapterHistoryService extends CrudService<UserBuychapterHistoryDao, UserBuychapterHistory> {
	
	@Autowired
	private BookChapterService bookChapterService;
	@Autowired
	private UserWalletService userWalletService;
	@Autowired
	private BookService bookService;
	
	public UserBuychapterHistory get(String id) {
		return super.get(id);
	}
	
	public List<UserBuychapterHistory> findList(UserBuychapterHistory userBuychapterHistory) {
		return super.findList(userBuychapterHistory);
	}
	
	public Page<UserBuychapterHistory> findPage(Page<UserBuychapterHistory> page, UserBuychapterHistory userBuychapterHistory) {
		return super.findPage(page, userBuychapterHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(UserBuychapterHistory userBuychapterHistory) {
		super.save(userBuychapterHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserBuychapterHistory userBuychapterHistory) {
		super.delete(userBuychapterHistory);
	}
	
	public UserBuychapterHistory getUserBuyChapterHistory(UserBuychapterHistory userBuychapterHistory){
		List<UserBuychapterHistory> list = findList(userBuychapterHistory);
		if(list != null && ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 用户是否购买了该章节
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	public Boolean isUserBuyBookChapter(String userId,String bookId, int chapter) {
		UserBuychapterHistory userBuychapterHistory = new UserBuychapterHistory();
		userBuychapterHistory.setCreateBy(new User(userId));
		userBuychapterHistory.setBookId(bookId);
		userBuychapterHistory.setChapter(chapter);
		
		List<UserBuychapterHistory> buyChapterHistoryList = findList(userBuychapterHistory);
		if(buyChapterHistoryList != null && !buyChapterHistoryList.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取用户的购买书籍的章节
	 * @param customerId
	 * @param bookId
	 * @return
	 */
	public List<Integer> selectByBookIdAndCustomerId(String customerId, String bookId) {
		return dao.selectByBookIdAndCustomerId(customerId, bookId);
	}
	
	/**
	 * 获取用户消费记录
	 * @param userId
	 * @param officeId
	 * @return 
	 */
	public List<Map> findExpenseList(String userId ,String officeId){
		return dao.findExpenseList(userId, officeId);
	}
	
	/**
	 * 获取用户已购列表
	 * @param userId
	 * @param officeIds
	 * @return
	 */
	public List<Map> findBoughtList(String userId ,List<String> officeIds){
		return dao.findBoughtList(userId, officeIds);
	}
	
	/**
	 * 单章购买
	 */
	@Transactional(readOnly = false)
	public void signgleBuy(UserBuychapterHistory userBuychapterHistory){
		//获取原价
		BigDecimal price = bookChapterService.getPriceBuyChapterAndBookId(userBuychapterHistory.getBookId(), userBuychapterHistory.getChapter());
		
		//单章购买不打折
		userBuychapterHistory.setDiscount(new BigDecimal(1));
		userBuychapterHistory.setOriginalprice(price);

		userBuychapterHistory.setPayCoin(price.multiply(userBuychapterHistory.getDiscount()));
		//扣除用户余额
		UserWallet userWallet = userWalletService.getUserWalletByOfficeId(userBuychapterHistory.getCreateBy().getId(), userBuychapterHistory.getOfficeId());
		userWalletService.addCoinAndTicket(userWallet.getId(), price.negate(), new BigDecimal(0));
		userBuychapterHistory.setCreateDate(new Date());
		
		insert(userBuychapterHistory);
	}
	
	@Transactional(readOnly = false)
	public void insert(UserBuychapterHistory userBuychapterHistory){
		Date now = new Date();
		userBuychapterHistory.setCreateDate(now);
		userBuychapterHistory.setUpdateDate(now);
		userBuychapterHistory.setId(IdGen.uuid());
		
		dao.insert(userBuychapterHistory);
	}
}