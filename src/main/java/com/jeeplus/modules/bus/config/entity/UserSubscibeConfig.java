/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.config.entity;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户订阅配置Entity
 * @author zhangsc
 * @version 2017-11-03
 */
@JsonInclude(Include.NON_NULL)
public class UserSubscibeConfig extends DataEntity<UserSubscibeConfig> {
	
	private static final long serialVersionUID = 1L;
	private String isAutoBuy;		// 是否自动购买（0:否 1:是）
	private String bookId;		// 图书id
	private String fodderId;    //素材id
	
	public UserSubscibeConfig() {
		super();
	}

	public UserSubscibeConfig(String id){
		super(id);
	}

	@Length(min=0, max=64, message="是否自动购买（0:否 1:是）长度必须介于 0 和 64 之间")
	@ExcelField(title="是否自动购买（0:否 1:是）", align=2, sort=1)
	public String getIsAutoBuy() {
		return isAutoBuy;
	}

	public void setIsAutoBuy(String isAutoBuy) {
		this.isAutoBuy = isAutoBuy;
	}
	
	@Length(min=0, max=64, message="图书id长度必须介于 0 和 64 之间")
	@ExcelField(title="图书id", align=2, sort=2)
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getFodderId() {
		return fodderId;
	}

	public void setFodderId(String fodderId) {
		this.fodderId = fodderId;
	}
	
}