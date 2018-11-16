/**
 * 
 */
package com.jeeplus.modules.bus.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.BaseEntity;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtil;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.sys.entity.Office;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月17日
 * @version V1.0
 *
 */
public class UserBookShelfVo {
	private static final long serialVersionUID = -5603414713891404569L;
	
	private String id;
	private String name;
	private String title;
	private String image;

	private String customerId;
	private BookChapterVo lastReadBookChapter; // 最后阅读章节
	
	private Page<UserBookShelfVo> page;
	
	
	private Office office;		// 来源机构
	
	// tenghenOfficeId=100000002004
	// tenghenImgHost=http://www.tenghen.com/Public/images/cover/
	private String tenghenOfficeId = Global.getConfig("tenghenOfficeId");
	private String tenghenImgHost = Global.getConfig("tenghenImgHost");
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
	
	
	
	/**
	 * @return the office
	 */
	public Office getOffice() {
		return office;
	}
	/**
	 * @param office the office to set
	 */
	public void setOffice(Office office) {
		this.office = office;
	}
	
	/**
	 * 接口显示
	 */
	public String getImageUrl() {
		if (!StringUtils.isBlank(image) && office != null) {
			if (StringUtils.equals(office.getId(), tenghenOfficeId)) {
				image = tenghenImgHost + image;
			}
		}
		return image;
	}
	
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public BookChapterVo getLastReadBookChapter() {
		return lastReadBookChapter;
	}
	
	public void setLastReadBookChapter(BookChapterVo lastReadBookChapter) {
		this.lastReadBookChapter = lastReadBookChapter;
	}
	
	/**
	 * @return the page
	 */
	public Page<UserBookShelfVo> getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(Page<UserBookShelfVo> page) {
		this.page = page;
	}

	

}
