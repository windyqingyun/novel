/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.NodArchive;

/**
 * 附件存储DAO接口
 * @author zhangsc
 */
@MyBatisDao
public interface NodArchiveDao extends CrudDao<NodArchive> {
	public List<NodArchive> findListByIds(@Param("ids") List<String> ids,@Param("DEL_FLAG_NORMAL") String DEL_FLAG_NORMAL) ;
	
	public NodArchive getByValidate(NodArchive nodArchive);
}