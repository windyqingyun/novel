/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import java.util.List;


import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.RecommendFodder;

/**
 * 素材DAO接口
 * @author zhangsc
 * @version 2017-11-02
 */
@MyBatisDao
public interface RecommendFodderDao extends CrudDao<RecommendFodder> {
	
	
	List<RecommendFodder> findRecommendFooderList(RecommendFodder fodder);
	
	void deleteAllRecommendFooder();
	
	void addRecommendFooder(List<RecommendFodder> fodder);
	
}