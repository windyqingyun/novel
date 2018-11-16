/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 素材Entity
 * @author zhangsc
 * @version 2017-11-02
 */
public class RecommendFodder extends DataEntity<RecommendFodder> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String titleImage;		// 标题图片
	private String content;		// 素材内容
	private String linkUrl;		// 链接地址
	private String bookId;		// 书籍id
	private Integer chapter;   //对应章节
	private Office office;		// 来源机构
	private Integer viewcount;		// 浏览次数
	
	private Date beginDate;   //开始日期
	private Date endDate;  //结束日期
	
	private double dbPayCoin;//查询阀值
	
	private Integer sort;//推荐表排序
	
	
	public RecommendFodder() {
		super();
	}

	public RecommendFodder(String id){
		super(id);
	}

	@Length(min=1, max=64, message="标题长度必须介于 1 和 64 之间")
	@ExcelField(title="标题", align=2, sort=1)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=250, message="标题图片长度必须介于 0 和 250 之间")
	@ExcelField(title="标题图片", align=2, sort=2)
	public String getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}
	
	@ExcelField(title="素材内容", align=2, sort=3)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=250, message="链接地址长度必须介于 0 和 250 之间")
	@ExcelField(title="链接地址", align=2, sort=4)
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	@Length(min=0, max=64, message="书籍id长度必须介于 0 和 64 之间")
	@ExcelField(title="书籍id", align=2, sort=5)
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
	@NotNull(message="来源机构不能为空")
	@ExcelField(title="来源机构", fieldType=Office.class, value="office.name", align=2, sort=6)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="浏览次数", align=2, sort=7)
	public Integer getViewcount() {
		return viewcount;
	}

	public void setViewcount(Integer viewcount) {
		this.viewcount = viewcount;
	}

	public Integer getChapter() {
		return chapter;
	}

	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getDbPayCoin() {
		return dbPayCoin;
	}

	public void setDbPayCoin(double dbPayCoin) {
		this.dbPayCoin = dbPayCoin;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}