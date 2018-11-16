/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.Fodder;

/**
 * 素材DAO接口
 * @author zhangsc
 * @version 2017-11-02
 */
@MyBatisDao
public interface FodderDao extends CrudDao<Fodder> {
	
	void addClickCount(@Param("id")String id);
	
	List<Fodder> findToDayList(Fodder fodder);
	
	List<Fodder> findFodderPage(Fodder fodder);
	
	List<Fodder> findRecommendFodderPage(Fodder fodder);
	
	List<Fodder> findNewFodderPage(Fodder fodder);
	
	List<Fodder> findNoViewHisFodder(Fodder fodder);
}