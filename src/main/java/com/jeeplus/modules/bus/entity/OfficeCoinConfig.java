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
 * 机构货币配置Entity
 * @author zhangsc
 * @version 2017-11-15
 */
public class OfficeCoinConfig extends DataEntity<OfficeCoinConfig> {
	
	private static final long serialVersionUID = 1L;
	private String officesimplyname;		// 机构简称
	private Office office;		// 机构id
	private String ticketname;		// 阅读券名称
	private String coinname;		// 阅读币名称
	private BigDecimal coinRate;        //阅读币汇率
	
	public OfficeCoinConfig() {
		super();
	}

	public OfficeCoinConfig(String id){
		super(id);
	}

	@Length(min=0, max=64, message="接口名称长度必须介于 0 和 64 之间")
	@ExcelField(title="机构简称", align=2, sort=1)
	public String getOfficesimplyname() {
		return officesimplyname;
	}

	public void setOfficesimplyname(String officesimplyname) {
		this.officesimplyname = officesimplyname;
	}
	
	@NotNull(message="机构id不能为空")
	@ExcelField(title="机构id", fieldType=Office.class, value="office.name", align=2, sort=2)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=0, max=64, message="密钥长度必须介于 0 和 64 之间")
	@ExcelField(title="阅读券名称", align=2, sort=3)
	public String getTicketname() {
		return ticketname;
	}

	public void setTicketname(String ticketname) {
		this.ticketname = ticketname;
	}
	
	@Length(min=1, max=64, message="阅读币名称长度必须介于 1 和 64 之间")
	@ExcelField(title="阅读币名称", align=2, sort=4)
	public String getCoinname() {
		return coinname;
	}

	public void setCoinname(String coinname) {
		this.coinname = coinname;
	}

	public BigDecimal getCoinRate() {
		return coinRate;
	}

	public void setCoinRate(BigDecimal coinRate) {
		this.coinRate = coinRate;
	}
	
}