/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.ListUtils;
import com.jeeplus.modules.bus.dto.BulkBuyTypeDto;
import com.jeeplus.modules.bus.entity.UserWallet;
import com.jeeplus.modules.bus.enums.BulkBuyTypeEnum;
import com.jeeplus.modules.bus.history.dao.UserBulkbuychapterHistoryDao;
import com.jeeplus.modules.bus.history.entity.UserBulkbuychapterHistory;
import com.jeeplus.modules.bus.history.entity.UserBuychapterHistory;
import com.jeeplus.modules.bus.service.BookChapterService;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.service.UserWalletService;

/**
 * 用户多章购买Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserBulkbuychapterHistoryService extends CrudService<UserBulkbuychapterHistoryDao, UserBulkbuychapterHistory> {
	@Autowired
	private BookChapterService bookChapterService;
	@Autowired
	private UserWalletService userWalletService;
	@Autowired
	private UserBuychapterHistoryService userBuychapterHistoryService;
	@Autowired
	private BookService bookService;
	
	public UserBulkbuychapterHistory get(String id) {
		return super.get(id);
	}
	
	public List<UserBulkbuychapterHistory> findList(UserBulkbuychapterHistory userBulkbuychapterHistory) {
		return super.findList(userBulkbuychapterHistory);
	}
	
	public Page<UserBulkbuychapterHistory> findPage(Page<UserBulkbuychapterHistory> page, UserBulkbuychapterHistory userBulkbuychapterHistory) {
		return super.findPage(page, userBulkbuychapterHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(UserBulkbuychapterHistory userBulkbuychapterHistory) {
		super.save(userBulkbuychapterHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserBulkbuychapterHistory userBulkbuychapterHistory) {
		super.delete(userBulkbuychapterHistory);
	}
	
	public UserBulkbuychapterHistory getUserBulkBuyChapter(UserBulkbuychapterHistory userBulkbuychapterHistory){
		List<UserBulkbuychapterHistory> list = findList(userBulkbuychapterHistory);
		if(list != null && ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 多章购买
	 * @param userBulkbuychapterHistory
	 */
	@Transactional
	public void bulkBuy(UserBulkbuychapterHistory userBulkbuychapterHistory){
	    userBulkbuychapterHistory.setOfficeId(bookService.getBookOfficeId(userBulkbuychapterHistory.getBookId()));
		calculatePrice(userBulkbuychapterHistory);
		this.insert(userBulkbuychapterHistory);

		//添加单章购买的记录
		for (int i = userBulkbuychapterHistory.getBeginChapter(); i <= userBulkbuychapterHistory.getEndChapter(); i++) {
			BigDecimal price = bookChapterService.getPriceBuyChapterAndBookId(userBulkbuychapterHistory.getBookId(), i);
			if(price != null && price.doubleValue() > 0.0){
				UserBuychapterHistory signgleBuy = new UserBuychapterHistory();
				signgleBuy.setOriginalprice(price);
				signgleBuy.setDiscount(userBulkbuychapterHistory.getDiscount());
				signgleBuy.setPayCoin(price.multiply(signgleBuy.getDiscount()));
				signgleBuy.setBookId(userBulkbuychapterHistory.getBookId());
				signgleBuy.setFodderId(userBulkbuychapterHistory.getFodderId());
				signgleBuy.setChapter(i);
				signgleBuy.setBulkbuychapterHistoryId(userBulkbuychapterHistory.getId());
				signgleBuy.setCreateBy(userBulkbuychapterHistory.getCreateBy());
				signgleBuy.setOfficeId(userBulkbuychapterHistory.getOfficeId());
				
				userBuychapterHistoryService.insert(signgleBuy);
			}
			
		}
		
		//减少用户余额
		UserWallet userWallet = userWalletService.getUserWalletByOfficeId(userBulkbuychapterHistory.getCreateBy().getId(), userBulkbuychapterHistory.getOfficeId());
		userWalletService.addCoinAndTicket(userWallet.getId(), userBulkbuychapterHistory.getPayCoin().negate(), new BigDecimal(0));
	}
	
	@Transactional(readOnly = false)
	public void insert(UserBulkbuychapterHistory userBulkbuychapterHistory){
		userBulkbuychapterHistory.setId(IdGen.uuid());
		userBulkbuychapterHistory.setCreateDate(new Date());
		dao.insert(userBulkbuychapterHistory);
	}
	
	/**
	 * 计算支付金额
	 * @param userBuychapterHistory
	 * @return
	 */
	public UserBulkbuychapterHistory calculatePrice(UserBulkbuychapterHistory userBulkbuychapterHistory){
		BulkBuyTypeEnum buyType = BulkBuyTypeEnum.getBulkBuyTypeEnumByCode(userBulkbuychapterHistory.getBulkBuyTypeCode());
		BulkBuyTypeDto bulkBuyType = new BulkBuyTypeDto(buyType);
		userBulkbuychapterHistory.setBulkBuyType(bulkBuyType);
		
		//(0:从第一章开始购买  1:按照正常顺序购买  2:从倒数的开始购买)
		if(bulkBuyType.getType().equals(0)){
			userBulkbuychapterHistory.setBeginChapter(bookChapterService.getMinChapterOfBook(userBulkbuychapterHistory.getBookId()));
			userBulkbuychapterHistory.setEndChapter(userBulkbuychapterHistory.getBeginChapter() + bulkBuyType.getSize());
		
		}else if(bulkBuyType.getType().equals(1)){
			userBulkbuychapterHistory.setEndChapter(userBulkbuychapterHistory.getBeginChapter() + bulkBuyType.getSize());
		
		}else if(bulkBuyType.getType().equals(2)){
			userBulkbuychapterHistory.setEndChapter(bookChapterService.getMaxChapterOfBook(userBulkbuychapterHistory.getBookId()));
			userBulkbuychapterHistory.setBeginChapter(userBulkbuychapterHistory.getEndChapter() - bulkBuyType.getSize());
		}
		
		BigDecimal price = dao.calculatePrice(userBulkbuychapterHistory);
		
		//计算用户从开始章节到结束章节中的已购总额
		BigDecimal sumPaidCoin = dao.sumUserBuyAmout(userBulkbuychapterHistory);
		
		userBulkbuychapterHistory.setOriginalprice(price);
		userBulkbuychapterHistory.setDiscount(userBulkbuychapterHistory.getBulkBuyType().getDiscount());
		//应付金额为 （章节总价-用户已购金额）* 折扣
		userBulkbuychapterHistory.setPayCoin((price.subtract(sumPaidCoin == null ? BigDecimal.ZERO : sumPaidCoin) )
				.multiply(userBulkbuychapterHistory.getDiscount()));
		
		return userBulkbuychapterHistory;
	}
}