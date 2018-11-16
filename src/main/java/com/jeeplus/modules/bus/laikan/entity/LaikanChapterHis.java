/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.laikan.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 来看用户购买记录Entity
 * @author zhangsc
 * @version 2018-01-17
 */
public class LaikanChapterHis extends DataEntity<LaikanChapterHis> {
	
	private static final long serialVersionUID = 1L;
	private String chapterId;		// chapter_id
	private String bookId;          //小说编号
	private Integer chapter;         //所属章节
	private String isSuccess;		// 是否同步成功(0:否  1:是)
	
	public LaikanChapterHis() {
		super();
	}

	public LaikanChapterHis(String id){
		super(id);
	}

	@Length(min=1, max=64, message="chapter_id长度必须介于 1 和 64 之间")
	@ExcelField(title="chapter_id", align=2, sort=1)
	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}
	
	@Length(min=0, max=6, message="是否同步成功(0:否  1:是)长度必须介于 0 和 6 之间")
	@ExcelField(title="是否同步成功(0:否  1:是)", align=2, sort=2)
	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public Integer getChapter() {
		return chapter;
	}

	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}
}