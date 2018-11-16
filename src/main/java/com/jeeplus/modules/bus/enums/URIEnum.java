/**
 * 
 */
package com.jeeplus.modules.bus.enums;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月18日
 * @version V1.0
 *
 */
public enum URIEnum {
	HOST_INDEX("http://www.content.vip/gyf/index.html"),
	HOST("http://www.content.vip"),
	INDEX("/gyf/index.html"),
	WECHAT_USERINFO("/nrfx_intertem/wechat/userinfo"),
	WECHAT_QR_USERINFO("/nrfx_intertem/wechat/qrUserInfo"); 
	
	private String uri;
	
	URIEnum(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
}
