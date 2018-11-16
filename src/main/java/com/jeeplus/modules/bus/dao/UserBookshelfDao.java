/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.UserBookshelf;
import com.jeeplus.modules.bus.vo.UserBookShelfVo;

/**
 * 用户书架DAO接口
 * @author lzp
 * @version 2018-05-15
 */
@MyBatisDao
public interface UserBookshelfDao extends CrudDao<UserBookshelf> {

	public List<UserBookShelfVo> findBookPageByBookshelf(UserBookShelfVo userBookShelfVo);
	
}