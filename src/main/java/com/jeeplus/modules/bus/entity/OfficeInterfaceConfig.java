/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 机构接口配置Entity
 * @author zhangsc
 * @version 2017-11-07
 */
public class OfficeInterfaceConfig extends DataEntity<OfficeInterfaceConfig> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 接口名称
	private Office office;		// 机构id
	private String secretKey;		// 密钥
	private String enable;		// 是否启用(0:不启用，1:启用)
	private String payInterfaceUrl;   //调用支付的地址
	private String payQueryUrl;   //支付查询接口的地址
	
	public OfficeInterfaceConfig() {
		super();
	}

	public OfficeInterfaceConfig(String id){
		super(id);
	}

	@Length(min=0, max=64, message="接口名称长度必须介于 0 和 64 之间")
	@ExcelField(title="接口名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="机构id不能为空")
	@ExcelField(title="机构id", fieldType=Office.class, value="office.name", align=2, sort=2)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=0, max=150, message="密钥长度必须介于 0 和 250 之间")
	@ExcelField(title="密钥", align=2, sort=3)
	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	@Length(min=1, max=2, message="是否启用(0:不启用，1:启用)长度必须介于 1 和 2 之间")
	@ExcelField(title="是否启用(0:不启用，1:启用)", align=2, sort=4)
	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getPayInterfaceUrl() {
		return payInterfaceUrl;
	}

	public void setPayInterfaceUrl(String payInterfaceUrl) {
		this.payInterfaceUrl = payInterfaceUrl;
	}

	public String getPayQueryUrl() {
		return payQueryUrl;
	}

	public void setPayQueryUrl(String payQueryUrl) {
		this.payQueryUrl = payQueryUrl;
	}
	
}