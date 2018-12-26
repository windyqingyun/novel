/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.Book;

import javax.annotation.security.PermitAll;

/**
 * 书籍DAO接口
 * @author zhangsc
 * @version 2017-11-03
 */
@MyBatisDao
public interface BookDao extends CrudDao<Book> {

	String existsBookAndrstId(@Param("officeId")String officeId, @Param("originalId")String originalId);
	
	void addClickCount(@Param("id")String id);
	
	String getBookOfficeId(@Param("id")String id);
	
	
	List<Book> findPageByFine(Book book);
	
	List<Book> findPageByPopularity(Book book);
	
	List<Book> findPageByHotsell(Book book);
	
	List<Book> findPageByNewbook(Book book);
	
	List<Book> recommend(Book book);
	
	int updateCustomviewcount(Book book);

	/**
	 * 收藏榜 对等关联
	 * @param book
	 * @return
	 */
	List<Book> findCollection(Book book);

	/**
	 * 新书榜 左外关联 ，排行榜排好之后用书数据补齐
	 * @param book
	 * @return
	 */
	List<Book> findNewBook(Book book);

	/**
	 * 一周上升最快
	 * @param book
	 * @return
	 */
	List<Book> findSoaring(Book book);

	/**
	 * 精品推荐
	 * @param book
	 * @return
	 */
	List<Book> findFine(Book book);

	/**
	 * 名字模糊查询书籍
	 * @param bookName
	 * @return
	 */
	List<Book> findLikeBook(@Param("bookName") String bookName);
	List<Book> findLikeBookColumns(@Param("bookName") String bookName);

	/**
	 * 热搜榜
	 * @param book
	 * @return
	 */
    List<Book> findHotSearch(Book book);
}