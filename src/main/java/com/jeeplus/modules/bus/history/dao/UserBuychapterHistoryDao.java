/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.history.entity.UserBuychapterHistory;

/**
 * 用户章节购买记录DAO接口
 * @author zhangsc
 * @version 2017-11-03
 */
@MyBatisDao
public interface UserBuychapterHistoryDao extends CrudDao<UserBuychapterHistory> {
	
	/**
	 * 获取消费记录
	 * @param officeId
	 * @return
	 */
	public List<Map> findExpenseList(@Param("userId")String userId ,@Param("officeId")String officeId);
	
	/**
	 * 获取已购列表
	 * @param userId
	 * @param officeIds
	 * @return
	 */
	public List<Map> findBoughtList(@Param("userId")String userId ,@Param("officeIds") List<String> officeIds);

	/**
	 * 获取用户的购买书籍的章节
	 * @param customerId
	 * @param bookId
	 * @return
	 */
	public List<Integer> selectByBookIdAndCustomerId(@Param("customerId") String customerId, @Param("bookId") String bookId);
}