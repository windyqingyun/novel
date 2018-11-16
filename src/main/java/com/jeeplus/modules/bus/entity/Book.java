/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 小数Entity
 * @author zhangsc
 * @version 2017-11-06
 */
@JsonInclude(Include.NON_NULL)
public class Book extends DataEntity<Book> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String title;		// title
	private String author;		// 作者
	private String image;		// 图片链接
	private Integer condition;	// 0:女频；1:男频
	private String tags;		// 标签，以','分割
	private String [] TagsArr;	// tags数组
	private Office office;		// 来源机构
	private String originalId;		// 原始id
	private Date publishDate;		// publish_date
	private Integer viewcount; // 浏览次数
	private Integer customviewcount; //gyf浏览次数
	
	private List<BookChapter> chapterList;   //章节列表
	
	public Book() {
		super();
	}

	public Book(String id){
		super(id);
	}

	/**
	 * 返回 tags 的数组格式
	 * @return String[]
	 */
	public String [] getTagsArr() {
		if(!StringUtils.isBlank(tags)) {
			if (StringUtils.contains(tags, ",")) {
				return StringUtils.split(tags, ",");
			}
			return StringUtils.split(tags, " ");
		}
		return new String[0];
	}
	
	
	// tenghenOfficeId=100000002004
	// tenghenImgHost=http://www.tenghen.com/Public/images/cover/
	private String tenghenOfficeId = Global.getConfig("tenghenOfficeId");
	private String tenghenImgHost = Global.getConfig("tenghenImgHost");
	
	@Length(min=1, max=64, message="名称长度必须介于 1 和 64 之间")
	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=64, message="title长度必须介于 1 和 64 之间")
	@ExcelField(title="title", align=2, sort=2)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=64, message="作者长度必须介于 0 和 64 之间")
	@ExcelField(title="作者", align=2, sort=3)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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
	
	@Length(min=0, max=250, message="图片链接长度必须介于 0 和 250 之间")
	@ExcelField(title="图片链接", align=2, sort=4)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Length(min=0, max=200, message="标签，以','分割长度必须介于 0 和 200 之间")
	@ExcelField(title="标签，以','分割", align=2, sort=5)
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
 
	public Integer getCondition() {
		return condition;
	}
 
	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	@NotNull(message="来源机构不能为空")
	@ExcelField(title="来源机构", fieldType=Office.class, value="office.name", align=2, sort=6)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=1, max=64, message="原始id长度必须介于 1 和 64 之间")
	@ExcelField(title="原始id", align=2, sort=7)
	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="publish_date不能为空")
	@ExcelField(title="publish_date", align=2, sort=8)
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	@ExcelField(title="浏览次数", align=2, sort=9)
	public Integer getViewcount() {
		return viewcount;
	}

	public void setViewcount(Integer viewcount) {
		this.viewcount = viewcount;
	}
	
	/**
	 * @return the customviewcount
	 */
	public Integer getCustomviewcount() {
		return customviewcount;
	}

	/**
	 * @param customviewcount the customviewcount to set
	 */
	public void setCustomviewcount(Integer customviewcount) {
		this.customviewcount = customviewcount;
	}

	public List<BookChapter> getChapterList() {
		return chapterList;
	}

	public void setChapterList(List<BookChapter> chapterList) {
		this.chapterList = chapterList;
	}
	
}