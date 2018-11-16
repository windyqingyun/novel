/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户钱包Entity
 * @author zhangsc
 * @version 2017-11-29
 */
@JsonInclude(Include.NON_NULL)
public class UserWallet extends DataEntity<UserWallet> {
	
	private static final long serialVersionUID = 1L;
	private Office office;		// 机构id
	private BigDecimal coin;		// 阅读币
	private BigDecimal ticket;		// 阅读券
	private String coinName;      //阅读币名称
	
	private OfficeCoinConfig coinConfig;   //对应的机构货币配置
	
	public UserWallet() {
		super();
	}

	public UserWallet(String id){
		super(id);
	}

	@ExcelField(title="机构id", fieldType=Office.class, value="office.name", align=2, sort=1)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="阅读币", align=2, sort=2)
	public BigDecimal getCoin() {
		return coin;
	}

	public void setCoin(BigDecimal coin) {
		this.coin = coin;
	}
	
	@ExcelField(title="阅读券", align=2, sort=3)
	public BigDecimal getTicket() {
		return ticket;
	}

	public void setTicket(BigDecimal ticket) {
		this.ticket = ticket;
	}

	public OfficeCoinConfig getCoinConfig() {
		return coinConfig;
	}

	public void setCoinConfig(OfficeCoinConfig coinConfig) {
		this.coinConfig = coinConfig;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}
	
}