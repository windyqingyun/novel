/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 充值规则Entity
 * @author zhangsc
 * @version 2017-11-29
 */
public class RechargeRule extends DataEntity<RechargeRule> {
	
	private static final long serialVersionUID = 1L;
	private Office office;		// 机构id
	private String name;		// 规则名称
	private BigDecimal price;		// 金额
	private BigDecimal rstCoin;		// 充值对应的阅读币
	private BigDecimal rstTicket;		// 充值对应的阅读券
	private String enable;		// 是否启用(0:否 1:是)
	private String isFirstCharge;     //是否是首次充值
	
	public RechargeRule() {
		super();
	}

	public RechargeRule(String id){
		super(id);
	}

	@NotNull(message="机构id不能为空")
	@ExcelField(title="机构id", fieldType=Office.class, value="office.name", align=2, sort=1)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=1, max=100, message="规则名称长度必须介于 1 和 100 之间")
	@ExcelField(title="规则名称", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="金额不能为空")
	@ExcelField(title="金额", align=2, sort=3)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@NotNull(message="返回阅读币不能为空")
	@ExcelField(title="充值对应的阅读币", align=2, sort=4)
	public BigDecimal getRstCoin() {
		return rstCoin;
	}

	public void setRstCoin(BigDecimal rstCoin) {
		this.rstCoin = rstCoin;
	}
	
	@ExcelField(title="充值对应的阅读券", align=2, sort=5)
	public BigDecimal getRstTicket() {
		return rstTicket;
	}

	public void setRstTicket(BigDecimal rstTicket) {
		this.rstTicket = rstTicket;
	}
	
	@Length(min=0, max=5, message="是否启用(0:否 1:是)长度必须介于 0 和 5 之间")
	@ExcelField(title="是否启用(0:否 1:是)", align=2, sort=6)
	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getIsFirstCharge() {
		return isFirstCharge;
	}

	public void setIsFirstCharge(String isFirstCharge) {
		this.isFirstCharge = isFirstCharge;
	}
}