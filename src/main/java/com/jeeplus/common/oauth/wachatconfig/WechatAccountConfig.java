/**
 * 
 */
package com.jeeplus.common.oauth.wachatconfig;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月18日
 * @version V1.0
 *
 */
@Component
@Configuration
public class WechatAccountConfig {
	@Value("${wechat.mpAppId}")
    private String mpAppId;

	@Value("${wechat.mpAppSecret}")
    private String mpAppSecret;

	@Value("${wechat.openAppId}")
    private String openAppId;

	@Value("${wechat.openAppSecret}")
    private String openAppSecret;

	@Value("${wechat.mchId}")
    private String mchId;

	@Value("${wechat.mchKey}")
    private String mchKey;

	@Value("${wechat.keyPath}")
    private String keyPath;

	@Value("${wechat.notifyUrl}")
    private String notifyUrl;

    private Map<String, String> templateId;

	/**
	 * @return the mpAppId
	 */
	public String getMpAppId() {
		return mpAppId;
	}

	/**
	 * @param mpAppId the mpAppId to set
	 */
	public void setMpAppId(String mpAppId) {
		this.mpAppId = mpAppId;
	}

	/**
	 * @return the mpAppSecret
	 */
	public String getMpAppSecret() {
		return mpAppSecret;
	}

	/**
	 * @param mpAppSecret the mpAppSecret to set
	 */
	public void setMpAppSecret(String mpAppSecret) {
		this.mpAppSecret = mpAppSecret;
	}

	/**
	 * @return the openAppId
	 */
	public String getOpenAppId() {
		return openAppId;
	}

	/**
	 * @param openAppId the openAppId to set
	 */
	public void setOpenAppId(String openAppId) {
		this.openAppId = openAppId;
	}

	/**
	 * @return the openAppSecret
	 */
	public String getOpenAppSecret() {
		return openAppSecret;
	}

	/**
	 * @param openAppSecret the openAppSecret to set
	 */
	public void setOpenAppSecret(String openAppSecret) {
		this.openAppSecret = openAppSecret;
	}

	/**
	 * @return the mchId
	 */
	public String getMchId() {
		return mchId;
	}

	/**
	 * @param mchId the mchId to set
	 */
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	/**
	 * @return the mchKey
	 */
	public String getMchKey() {
		return mchKey;
	}

	/**
	 * @param mchKey the mchKey to set
	 */
	public void setMchKey(String mchKey) {
		this.mchKey = mchKey;
	}

	/**
	 * @return the keyPath
	 */
	public String getKeyPath() {
		return keyPath;
	}

	/**
	 * @param keyPath the keyPath to set
	 */
	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	/**
	 * @return the notifyUrl
	 */
	public String getNotifyUrl() {
		return notifyUrl;
	}

	/**
	 * @param notifyUrl the notifyUrl to set
	 */
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	/**
	 * @return the templateId
	 */
	public Map<String, String> getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(Map<String, String> templateId) {
		this.templateId = templateId;
	}
    
}
