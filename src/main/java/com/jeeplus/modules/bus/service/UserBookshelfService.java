/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.dao.UserBookshelfDao;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.entity.UserBookshelf;
import com.jeeplus.modules.bus.history.service.UserReadHistoryService;
import com.jeeplus.modules.bus.vo.UserBookShelfVo;

/**
 * 用户书架Service
 * @author lzp
 * @version 2018-05-15
 */
@Service
@Transactional(readOnly = true)
public class UserBookshelfService extends CrudService<UserBookshelfDao, UserBookshelf> {

	@Autowired
	private UserReadHistoryService userReadHistoryService;
	@Autowired
	private BookService bookService;
	
	public UserBookshelf get(String id) {
		return super.get(id);
	}
	
	public List<UserBookshelf> findList(UserBookshelf userBookshelf) {
		return super.findList(userBookshelf);
	}
	
	public Page<UserBookshelf> findPage(Page<UserBookshelf> page, UserBookshelf userBookshelf) {
		return super.findPage(page, userBookshelf);
	}
	
	@Transactional
	public void save(UserBookshelf userBookshelf) {
		super.save(userBookshelf);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserBookshelf userBookshelf) {
		super.delete(userBookshelf);
	}
	
	/**
	 * 加入书架
	 */
	@Transactional
	public ServerResponse addBookToUserBookShelf(UserBookshelf userBookshelf) {
		// 书籍是否存在
		Book book = bookService.findUniqueByProperty("id", userBookshelf.getBookId());
		if (book != null) {
			save(userBookshelf);
			return ServerResponse.createBySuccessMessage("添加成功");
		}
		return ServerResponse.createByError("对不起,书籍没找到或已下架!");
	}
	
	/**
	 * 根据书架查找书籍
	 * @param page
	 * @param userBookshelf
	 * @return
	 */
	public Page<UserBookShelfVo> findBookPageByBookshelf(Page<UserBookShelfVo> page, UserBookShelfVo userBookShelfVo) {
		userBookShelfVo.setPage(page);
		page.setList(dao.findBookPageByBookshelf(userBookShelfVo));
		return page;
	}
	
}