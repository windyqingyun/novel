package com.jeeplus.modules.bus.dto;

import java.io.Serializable;

import com.jeeplus.modules.bus.entity.UserWallet;

/**
 * 个人中心中的个人钱包dto
 * @author zhangsc
 * @version 2017年12月4日
 */
public class UserWalletDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UserWallet userWallet;     //用户钱包
	private Integer viewCount;        //阅读小数的数量
	private String lastViewName;     //最后阅读的小说名称
	private String lastViewChapterName;   //最后阅读的章节名称
 	
	public UserWalletDto(){
		super();
	}
	public UserWalletDto(UserWallet userWallet){
		super();
		this.userWallet = userWallet;
	}
	
	public UserWallet getUserWallet() {
		return userWallet;
	}
	public void setUserWallet(UserWallet userWallet) {
		this.userWallet = userWallet;
	}
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public String getLastViewName() {
		return lastViewName;
	}
	public void setLastViewName(String lastViewName) {
		this.lastViewName = lastViewName;
	}
	public String getLastViewChapterName() {
		return lastViewChapterName;
	}
	public void setLastViewChapterName(String lastViewChapterName) {
		this.lastViewChapterName = lastViewChapterName;
	}
	
}
