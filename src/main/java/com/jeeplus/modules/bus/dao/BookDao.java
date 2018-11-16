/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.Book;

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
}