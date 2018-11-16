/**
 * 
 */
package com.jeeplus.common.constant;

import com.jeeplus.common.config.Global;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月21日
 * @version V1.0
 *
 */
public class Constants {

	public interface Customer {
		String WECHAT_TOKEN = "wechat_";
		String TENCENT_TOKEN = "tencent_";
		String CUSTOMER_ID = "id";
		String OPENID = "openid";
		String OPENID_TYPE = "openidType";
		String NICK_NAME = "nickname";
		String HEAD_IMGURL = "headimgurl";
		String SEX = "sex";
		String ORIGIN = "origin"; // 请求来源
	}

	public interface Cookie {
		String COOKIE_DOMAIN = ".content.vip";
		String PATH = "/";
		int CSESSIONID_TIME = Integer.parseInt(Global.getConfig("customer.keep.active")) * 24 * 3600; // 15天
		String CSESSIONID = "gyf.session.id";
		int LOGIN_REDIRECT_TIME = 5 * 60; // 5分钟
		String LOGIN_REDIRECT = "gyf.login.redirect";
		int ORIGIN_TIME = Integer.parseInt(Global.getConfig("customer.keep.active")) * 24 * 3600; // 15天
		String ORIGIN = "gyf.origin";
	}
}
