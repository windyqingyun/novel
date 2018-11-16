/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.entity;

import com.jeeplus.common.persistence.DataEntity;

/**
 * 用户阅读记录Entity
 * @author zhangsc
 * @version 2017-11-03
 */
public class ViewIpHistory extends DataEntity<ViewIpHistory> {
	
	private static final long serialVersionUID = 1L;
	
	private String ipNum;    //ip地址
	private String userId;   //用户编号
	private String viewUrl;  //访问路径
	private String bizId;    //业务编号
	
	public ViewIpHistory() {
		super();
	}

	public ViewIpHistory(String id){
		super(id);
	}
	
	public ViewIpHistory(String ipNum, String userId, String viewUrl, String bizId){
		super();
		this.ipNum = ipNum;
		this.userId = userId;
		this.viewUrl = viewUrl;
		this.bizId = bizId;
	}

	public String getIpNum() {
		return ipNum;
	}

	public void setIpNum(String ipNum) {
		this.ipNum = ipNum;
	}

	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
}