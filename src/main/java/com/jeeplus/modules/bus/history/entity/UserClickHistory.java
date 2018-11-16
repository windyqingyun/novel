/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户点击记录Entity
 * @author zhangsc
 * @version 2017-11-03
 */
public class UserClickHistory extends DataEntity<UserClickHistory> {
	
	private static final long serialVersionUID = 1L;
	private String fodderId;		// 素材id
	private Integer clickCount;		// 点击次数
	private String bookId;          //书籍id
	
	public UserClickHistory() {
		super();
	}

	public UserClickHistory(String id){
		super(id);
	}

	@Length(min=1, max=64, message="素材id长度必须介于 1 和 64 之间")
	@ExcelField(title="素材id", align=2, sort=1)
	public String getFodderId() {
		return fodderId;
	}

	public void setFodderId(String fodderId) {
		this.fodderId = fodderId;
	}
	
	@ExcelField(title="点击次数", align=2, sort=2)
	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
}