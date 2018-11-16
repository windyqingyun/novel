/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.bus.entity.Fodder;

/**
 * 用户收藏记录Entity
 * @author zhangsc
 * @version 2017-11-03
 */
public class UserFavorites extends DataEntity<UserFavorites> {
	
	private static final long serialVersionUID = 1L;
	private String fodderId;		// 素材id
	
	private Fodder fodder;  //素材
	
	private String officeId;   //用来筛选用户收藏的素材（根据素材的来源机构）
	
	public UserFavorites() {
		super();
	}

	public UserFavorites(String id){
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

	public Fodder getFodder() {
		return fodder;
	}

	public void setFodder(Fodder fodder) {
		this.fodder = fodder;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
}