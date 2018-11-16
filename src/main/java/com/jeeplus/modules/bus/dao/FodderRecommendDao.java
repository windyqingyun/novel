/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.FodderRecommend;

/**
 * 推荐素材列表DAO接口
 * @author zhangsc
 * @version 2018-03-14
 */
@MyBatisDao
public interface FodderRecommendDao extends CrudDao<FodderRecommend> {
	Integer getMaxGroupNum();
}