/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 流量提供端用户Entity
 * @author zhangsc
 * @version 2017-12-22
 */
public class ProviderUser extends DataEntity<ProviderUser> {
	
	private static final long serialVersionUID = 1L;
	private Office office;		// 归属部门
	private String sex;		// 性别(0:女 1:男)
	private String name;		// 姓名
	private String email;		// 邮箱
	private String mobile;		// 手机
	private String photo;		// 用户头像
	private String originalId;		// 原来的id
	
	public ProviderUser() {
		super();
	}

	public ProviderUser(String id){
		super(id);
	}

	public ProviderUser(String officeId, String originalId){
		super();
		this.office = new Office(officeId);
		this.originalId = originalId;
	}
	
	@ExcelField(title="归属部门", fieldType=Office.class, value="office.name", align=2, sort=1)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=0, max=10, message="性别(0:女 1:男)长度必须介于 0 和 10 之间")
	@ExcelField(title="性别(0:女 1:男)", dictType="", align=2, sort=2)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=100, message="姓名长度必须介于 0 和 100 之间")
	@ExcelField(title="姓名", align=2, sort=3)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=200, message="邮箱长度必须介于 0 和 200 之间")
	@ExcelField(title="邮箱", align=2, sort=4)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=200, message="手机长度必须介于 0 和 200 之间")
	@ExcelField(title="手机", align=2, sort=5)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=1000, message="用户头像长度必须介于 0 和 1000 之间")
	@ExcelField(title="用户头像", align=2, sort=6)
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@Length(min=0, max=100, message="原来的id长度必须介于 0 和 100 之间")
	@ExcelField(title="原来的id", align=2, sort=7)
	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	
}