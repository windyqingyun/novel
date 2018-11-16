/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.laikan.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.history.entity.UserBuychapterHistory;
import com.jeeplus.modules.bus.history.service.UserBuychapterHistoryService;
import com.jeeplus.modules.bus.laikan.dao.LaikanChapterHisDao;
import com.jeeplus.modules.bus.laikan.entity.LaikanChapterHis;
import com.jeeplus.modules.bus.service.BookChapterService;
import com.jeeplus.modules.sys.entity.User;

/**
 * 来看用户购买记录Service
 * @author zhangsc
 * @version 2018-01-17
 */
@Service
@Lazy(value=false)
@Transactional(readOnly = true)
public class LaikanChapterHisService extends CrudService<LaikanChapterHisDao, LaikanChapterHis> {
	@Autowired
	private UserBuychapterHistoryService userBuychapterHistoryService;
	@Autowired
	private BookChapterService bookChapterService;
	
	public LaikanChapterHis get(String id) {
		return super.get(id);
	}
	
	public List<LaikanChapterHis> findList(LaikanChapterHis laikanChapterHis) {
		return super.findList(laikanChapterHis);
	}
	
	public Page<LaikanChapterHis> findPage(Page<LaikanChapterHis> page, LaikanChapterHis laikanChapterHis) {
		return super.findPage(page, laikanChapterHis);
	}
	
	@Transactional(readOnly = false)
	public void save(LaikanChapterHis laikanChapterHis) {
		super.save(laikanChapterHis);
	}
	
	@Transactional(readOnly = false)
	public void delete(LaikanChapterHis laikanChapterHis) {
		super.delete(laikanChapterHis);
	}
	
	/**
	 * 更新bookId
	 * @param chapterId
	 * @param bookId
	 * @param chapter
	 */
	@Transactional(readOnly = false)
	public void updateBookIdByChapterId(String chapterId, String bookId, Integer chapter){
		dao.updateBookIdByChapterId(chapterId, bookId, chapter);
	}
	
	/**
	 * 数据转换 每一分钟执行一次
	 */
	@Transactional(readOnly = false)
	public void transform(){
		LaikanChapterHis searchBean = new LaikanChapterHis();
		searchBean.setIsSuccess(Global.NO);
		Page<LaikanChapterHis> page = new Page<LaikanChapterHis>(1, 100);
		Page<LaikanChapterHis> pageList = findPage(page, searchBean);
		List<LaikanChapterHis> list = pageList.getList();
		for (LaikanChapterHis his : list) {
			//保存记录
			UserBuychapterHistory userBuychapterHistory = new UserBuychapterHistory();
			userBuychapterHistory.setChapter(his.getChapter());
			userBuychapterHistory.setBookId(his.getBookId());
			userBuychapterHistory.setCreateBy(new User(his.getId()));
			userBuychapterHistory.setCreateDate(new Date());
			
			userBuychapterHistoryService.insert(userBuychapterHistory);
		
			//更新清洗状态
			updSuccess(his.getId(), his.getChapterId());
		}
	}
	
	/**
	 * 更改清洗状态为成功
	 * @param userId
	 * @param chapterId
	 */
	@Transactional(readOnly = false)
	public void updSuccess(String userId, String chapterId){
		dao.updSuccess(userId, chapterId);
	}
}