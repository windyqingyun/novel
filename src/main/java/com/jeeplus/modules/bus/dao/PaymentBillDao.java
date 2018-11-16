/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.PaymentBill;

/**
 * 支付单DAO接口
 * @author zhangsc
 * @version 2017-11-03
 */
@MyBatisDao
public interface PaymentBillDao extends CrudDao<PaymentBill> {
	PaymentBill getFirstPaymentBillIfNoSuc(@Param("userId")String userId);
}