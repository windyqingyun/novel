/**
 * 
 */
package com.jeeplus.modules.bus.login;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.oauth.wachatconfig.ProjectUrlConfig;
import com.jeeplus.common.utils.CookieUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.enums.ResultEnum;
import com.jeeplus.modules.bus.enums.URIEnum;
import com.jeeplus.modules.bus.exception.BookException;
import com.jeeplus.modules.bus.service.WechatService;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月18日
 * @version V1.0
 *
 */
@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/wechat")
public class WechatController {
	private Logger logger = LoggerFactory.getLogger(WechatController.class);
	@Autowired
    WxMpService wxMpService;
    @Autowired
    WxMpService wxOpenService;
    @Autowired
    private ProjectUrlConfig projectUrlConfig;
    @Autowired
    private WechatService wechatService;
    

    @RequestMapping(value="/authorize", method=RequestMethod.GET)
    public String authorize(@RequestParam(name = "returnUrl", required =false) String returnUrl) {
    	if (!StringUtils.isBlank(returnUrl)) {
    		returnUrl = URLDecoder.decode(returnUrl);
    	} else {
    		returnUrl = projectUrlConfig.getWechatMpAuthorize() + URIEnum.INDEX.getUri();
    	}
    	logger.info("authorize returnUrl = {}", returnUrl);
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(projectUrlConfig.getWechatMpAuthorize() + URIEnum.WECHAT_USERINFO.getUri(), WxConsts.OAUTH2_SCOPE_USER_INFO, returnUrl);
        logger.info("authorize redirectUrl = {}", redirectUrl);
        return "redirect:" + redirectUrl;
    }
    
    @RequestMapping(value="/userinfo",method=RequestMethod.GET)
    public String userInfo(@RequestParam("code") String code , @RequestParam(name = "state", required = false) String returnUrl,
    		HttpServletResponse response) {
    	logger.info("userInfo : returnUrl = {}", returnUrl);
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
        	wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
        	logger.error("【微信网页授权】授权失败{}" , e);
            throw new BookException(ResultEnum.WECHAT_MP_ERROR.getCode() , e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        
        wechatService.bindingLocalUser(wxMpOAuth2AccessToken);
        CookieUtil.addCookieByName(response, Constants.Cookie.CSESSIONID, Constants.Customer.WECHAT_TOKEN + wxMpOAuth2AccessToken.getOpenId(), Constants.Cookie.CSESSIONID_TIME);
//        return "redirect:" + returnUrl + "?openid="+openId;
        return "redirect:" + projectUrlConfig.getWechatMpAuthorize() + URIEnum.INDEX.getUri();
    }  
    
    @RequestMapping(value="/qrAuthorize",method=RequestMethod.GET)
    public String qrAuthorize(@RequestParam(name = "returnUrl", required = false) String returnUrl) {
    	if (!StringUtils.isBlank(returnUrl)) {
    		returnUrl = URLDecoder.decode(returnUrl);
    	} else {
    		returnUrl = projectUrlConfig.getWechatOpenAuthorize() + URIEnum.INDEX.getUri();
    	}
    	logger.info("qrAuthorize returnUrl = {}", returnUrl);
        String redirectUrl = wxOpenService.buildQrConnectUrl(projectUrlConfig.getWechatOpenAuthorize() + URIEnum.WECHAT_QR_USERINFO.getUri(), WxConsts.QRCONNECT_SCOPE_SNSAPI_LOGIN, returnUrl);
        logger.info("qrAuthorize redirectUrl = {}", redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @RequestMapping(value="/qrUserInfo",method=RequestMethod.GET)
    public String qrUserInfo(@RequestParam("code") String code, @RequestParam(name = "state", required = false) String returnUrl
    		, HttpServletResponse response) {
    	logger.info("qrUserInfo : returnUrl = {}", returnUrl);
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
        	logger.error("【微信网页授权】授权失败{}", e);
            throw new BookException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        
        wechatService.bindingLocalUser(wxMpOAuth2AccessToken);
        CookieUtil.addCookieByName(response, Constants.Cookie.CSESSIONID, Constants.Customer.WECHAT_TOKEN + wxMpOAuth2AccessToken.getOpenId(), Constants.Cookie.CSESSIONID_TIME);
//        return "redirect:" + returnUrl + "?openid="+openId;
        return "redirect:" + projectUrlConfig.getWechatOpenAuthorize() + URIEnum.INDEX.getUri();
    }
}
