/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import com.jeeplus.modules.bus.entity.Fodder;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.sys.entity.Office;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 推荐素材列表Entity
 * @author zhangsc
 * @version 2018-03-14
 */
public class FodderRecommend extends DataEntity<FodderRecommend> {
	
	private static final long serialVersionUID = 1L;
	private Fodder fodder;		// 素材编号
	private Book book;		// 小说编号
	private Office office;		// 机构编号
	private Office toOffice;		// to_office_id
	private BigDecimal fodderCtr;		// 素材的ctr
	private Date fodderCreateDate;		// 小说创建时间
	private BigDecimal convertRate;		// 转换率
	private Date invalidDate;           //过期时间
	private int groupNum;              //所属组号
	private Date showDate;             //显示时间
	
	private Date beginDate;   //开始时间
	private Date endDate;     //结束时间
	private Date maxInvalidDate;   //最大过期时间
	private String notInId;       //排除的Id
	
	public FodderRecommend() {
		super();
	}

	public FodderRecommend(String id){
		super(id);
	}

	public FodderRecommend(Fodder fodder, Office toOffice, Date invalidDate, int groupNum, Date showDate){
		super();
		this.id = fodder.getNewId();
		this.fodder = fodder;
		this.book = new Book(fodder.getBookId());
		this.office = fodder.getOffice();
		this.toOffice = toOffice;
		this.fodderCtr = fodder.getCtr();
		this.fodderCreateDate = fodder.getCreateDate();
		this.convertRate = fodder.getConvertRate();
		this.invalidDate = invalidDate;
		this.groupNum = groupNum;
		this.showDate = showDate;
	}
	
	@ExcelField(title="素材编号", align=2, sort=1)
	public Fodder getFodder() {
		return fodder;
	}

	public void setFodder(Fodder fodder) {
		this.fodder = fodder;
	}
	
	@ExcelField(title="小说编号", align=2, sort=2)
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	@ExcelField(title="机构编号", fieldType=Office.class, value="office.name", align=2, sort=3)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="to_office_id", align=2, sort=4)
	public Office getToOffice() {
		return toOffice;
	}

	public void setToOffice(Office toOffice) {
		this.toOffice = toOffice;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="小说创建时间", align=2, sort=6)
	public Date getFodderCreateDate() {
		return fodderCreateDate;
	}

	public void setFodderCreateDate(Date fodderCreateDate) {
		this.fodderCreateDate = fodderCreateDate;
	}

	public BigDecimal getFodderCtr() {
		return fodderCtr;
	}

	public void setFodderCtr(BigDecimal fodderCtr) {
		this.fodderCtr = fodderCtr;
	}

	public BigDecimal getConvertRate() {
		return convertRate;
	}

	public void setConvertRate(BigDecimal convertRate) {
		this.convertRate = convertRate;
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

	public Date getMaxInvalidDate() {
		return maxInvalidDate;
	}

	public void setMaxInvalidDate(Date maxInvalidDate) {
		this.maxInvalidDate = maxInvalidDate;
	}

	public Date getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	public int getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}

	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}

	public String getNotInId() {
		return notInId;
	}

	public void setNotInId(String notInId) {
		this.notInId = notInId;
	}
}