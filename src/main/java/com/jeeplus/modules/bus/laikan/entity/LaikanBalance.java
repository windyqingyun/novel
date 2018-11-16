/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.laikan.entity;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 来看用户余额Entity
 * @author zhangsc
 * @version 2018-01-17
 */
public class LaikanBalance extends DataEntity<LaikanBalance> {
	
	private static final long serialVersionUID = 1L;
	private BigDecimal money;		// money
	private String isSuccess;		// 是否添加成功（0:否  1:是）
	
	public LaikanBalance() {
		super();
	}

	public LaikanBalance(String id){
		super(id);
	}

	@NotNull(message="money不能为空")
	@ExcelField(title="money", align=2, sort=1)
	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	@Length(min=1, max=6, message="是否添加成功（0:否  1:是）长度必须介于 1 和 6 之间")
	@ExcelField(title="是否添加成功（0:否  1:是）", align=2, sort=2)
	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	
}