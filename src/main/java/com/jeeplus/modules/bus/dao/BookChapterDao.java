/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.vo.BookChapterVo;

/**
 * 小说章节内容DAO接口
 * @author zhangsc
 * @version 2017-11-03
 */
@MyBatisDao
public interface BookChapterDao extends CrudDao<BookChapter> {
	public String exsitsChapterAndrstId(@Param("bookId")String bookId, @Param("chapter")int chapter);
	
	public BookChapter getByChapterAndBookId(BookChapter bookChapter);
	
	public BigDecimal getPriceBuyChapterAndBookId(@Param("bookId")String bookId, @Param("chapter")Integer chapter);
	
	public Integer getMaxChapterOfBook(@Param("bookId")String bookId);
	
	public Integer getMinChapterOfBook(@Param("bookId")String bookId);

	/**
	 * 获取小说的最后一个章节
	 */
	BookChapter getLastBookChapterByBookId(@Param("bookId") String bookId);
	
	/**
	 * 获取用户的最后一个阅读章节
	 */
	BookChapter getLastReadChapterByBookIdAndCustomerId(@Param("bookId") String bookId, @Param("customerId") String customerId);
 
	/**
	 * 根据小说获取章节总数
	 * @param bookId
	 * @return
	 */
	Integer selectBookChapterCountByBookId(@Param("bookId") String bookId);
	
	/**
	 * 返回章节内容
	 * @param bookChapter
	 * @return
	 */
	BookChapterVo findBookChapterContent(BookChapter bookChapter);
}