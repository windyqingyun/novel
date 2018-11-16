/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.laikan.dao;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.laikan.entity.LaikanChapterHis;

/**
 * 来看用户购买记录DAO接口
 * @author zhangsc
 * @version 2018-01-17
 */
@MyBatisDao
public interface LaikanChapterHisDao extends CrudDao<LaikanChapterHis> {

	void updateBookIdByChapterId(@Param("chapterId")String chapterId, @Param("bookId")String bookId, @Param("chapter")Integer chapter);
	
	void updSuccess(@Param("userId")String userId, @Param("chapterId")String chapterId);
}