/**
 * 
 */
package com.jeeplus.common.oauth.wachatconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月18日
 * @version V1.0
 *
 */

@Configuration
public class ProjectUrlConfig {

	@Value("${wechat.projectUrl.wechatMpAuthorize}")
    private String wechatMpAuthorize;

	@Value("${wechat.projectUrl.wechatOpenAuthorize}")
    private String wechatOpenAuthorize;

	@Value("${wechat.projectUrl.nrfx_intertem}")
    private String nrfx_intertem;

	/**
	 * @return the wechatMpAuthorize
	 */
	public String getWechatMpAuthorize() {
		return wechatMpAuthorize;
	}

	/**
	 * @param wechatMpAuthorize the wechatMpAuthorize to set
	 */
	public void setWechatMpAuthorize(String wechatMpAuthorize) {
		this.wechatMpAuthorize = wechatMpAuthorize;
	}

	/**
	 * @return the wechatOpenAuthorize
	 */
	public String getWechatOpenAuthorize() {
		return wechatOpenAuthorize;
	}

	/**
	 * @param wechatOpenAuthorize the wechatOpenAuthorize to set
	 */
	public void setWechatOpenAuthorize(String wechatOpenAuthorize) {
		this.wechatOpenAuthorize = wechatOpenAuthorize;
	}

	/**
	 * @return the nrfx_intertem
	 */
	public String getNrfx_intertem() {
		return nrfx_intertem;
	}

	/**
	 * @param nrfx_intertem the nrfx_intertem to set
	 */
	public void setNrfx_intertem(String nrfx_intertem) {
		this.nrfx_intertem = nrfx_intertem;
	}

    
    
}
