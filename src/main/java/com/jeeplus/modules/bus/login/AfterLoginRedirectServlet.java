package com.jeeplus.modules.bus.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.utils.CookieUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.enums.URIEnum;
import com.jeeplus.modules.bus.service.TencentService;
import com.jeeplus.modules.sys.utils.OriginUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;

@RestController
@CrossOrigin(origins = "*")
public class AfterLoginRedirectServlet {

	private Logger logger = LoggerFactory.getLogger(AfterLoginRedirectServlet.class);

	@Autowired
	private TencentService tencentService;

	// http://www.content.vip/nrfx_intertem/home/QQcallback
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/home/QQcallback", method = RequestMethod.GET)
	public void QQlogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html; charset=utf-8");

		PrintWriter out = response.getWriter();

		try {
			AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);

			String accessToken = null, openID = null;
			long tokenExpireIn = 0L;

			if (accessTokenObj.getAccessToken().equals("")) {
				// 我们的网站被CSRF攻击了或者用户取消了授权
				// 做一些数据统计工作
				logger.info("没有获取到响应参数");
			} else {
				accessToken = accessTokenObj.getAccessToken();
				tokenExpireIn = accessTokenObj.getExpireIn();
				//
				// request.getSession().setAttribute("demo_access_token",
				// accessToken);
				// request.getSession().setAttribute("demo_token_expirein",
				// String.valueOf(tokenExpireIn));
				//
				// // 利用获取到的accessToken 去获取当前用的openid -------- start
				OpenID openIDObj = new OpenID(accessToken);
				openID = openIDObj.getUserOpenID();
				//
				// logger.info("欢迎你，代号为 " + openID + " 的用户!");
				// request.getSession().setAttribute("qqopenid", openID);
				//// out.println("<a href=" + "/shuoshuoDemo.html" + "
				// target=\"_blank\">去看看发表说说的demo吧</a>");
				// // 利用获取到的accessToken 去获取当前用户的openid --------- end
				//
				//
				// logger.info("<p> start
				// -----------------------------------利用获取到的accessToken,openid
				// 去获取用户在Qzone的昵称等信息 ---------------------------- start </p>");
				UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
				// UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
				//
				//
				// out.println("<br/>");
				// if (userInfoBean.getRet() == 0) {
				// out.println(userInfoBean.getNickname() + "<br/>");
				// out.println(userInfoBean.getGender() + "<br/>");
				// out.println("黄钻等级： " + userInfoBean.getLevel() + "<br/>");
				// out.println("会员 : " + userInfoBean.isVip() + "<br/>");
				// out.println("黄钻会员： " + userInfoBean.isYellowYearVip() +
				// "<br/>");
				// out.println("<image src=" +
				// userInfoBean.getAvatar().getAvatarURL30() + "/><br/>");
				// out.println("<image src=" +
				// userInfoBean.getAvatar().getAvatarURL50() + "/><br/>");
				// out.println("<image src=" +
				// userInfoBean.getAvatar().getAvatarURL100() + "/><br/>");
				// } else {
				// out.println("很抱歉，我们没能正确获取到您的信息，原因是： " +
				// userInfoBean.getMsg());
				// }
				// String officeIdOf =
				// OfficeEnum.officeIdOf(CookieUtil.getORIGIN(request));

				String officeId = OriginUtil.getOfficeIdByOrigin(request);
				if (StringUtils.isBlank(officeId)) {
					logger.info("orgin is blank : {}", officeId);
					response.sendRedirect(URIEnum.HOST_INDEX.getUri());
					return;
				}
				logger.info("officeId : {}", officeId);

				tencentService.bindingLocalUser(openID, qzoneUserInfo, officeId);
				CookieUtil.addCookieByName(response, Constants.Cookie.CSESSIONID,
						Constants.Customer.TENCENT_TOKEN + openIDObj.getUserOpenID(), Constants.Cookie.CSESSIONID_TIME);
				logger.info("cookie成功");
			}

			logger.info(
					"<p> end -----------------------------------利用获取到的accessToken,openid 去获取用户在Qzone的昵称等信息 ---------------------------- end </p>");

		} catch (QQConnectException e) {
			logger.error("QQConnectException error : " + e.getMessage());
			response.sendRedirect(URIEnum.HOST_INDEX.getUri());
		}

		String redirectUrl = CookieUtil.getCookieValueByKey(request, Constants.Cookie.LOGIN_REDIRECT);
		if (!StringUtils.isBlank(redirectUrl)) {
			logger.info("redirectUrl : {}", redirectUrl);
			CookieUtil.addCookieByName(response, Constants.Cookie.LOGIN_REDIRECT, null, 0);
			response.sendRedirect(redirectUrl);
		} else {
			response.sendRedirect(URIEnum.HOST_INDEX.getUri());
		}

	}
}
