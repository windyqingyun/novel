/**
 * 
 */
package com.jeeplus.modules.bus.vo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jeeplus.common.utils.DateUtil;
import com.jeeplus.common.utils.DateUtils;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月15日
 * @version V1.0
 *
 */
public class BookChapterVo implements Serializable{
 
	private static final long serialVersionUID = 1L;

	private String id;
	private String title;
	private String isvip;
	private String isBuy;
	private String content;
	private Date updateDate;

	private String bookId;
	private Integer chapter;
	
	private BookChapterVo preBookChapterVo;
	private BookChapterVo nextBookChapterVo;
	
	private String lastestReadChapterTitle; //最后阅读章节 
	private Date latestReadTime;			 //最后阅读时间
	

	/**
	 * 用于显示	30分钟前更新
	 * @return the latestUpdateTime
	 */
	// 最后更新时间到现在的距离时间
	public Map getLatestUpdateTime() {
		Map timesBetween = new HashMap<>();
		if(updateDate != null) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format = sdf.format(System.currentTimeMillis());
			try {
				Date parseDate = DateUtils.parseDate(format, "yyyy-MM-dd HH:mm:ss");
				timesBetween = DateUtil.timesBetween(updateDate, parseDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return timesBetween;
	}
	
	
 
	public String getId() {
		return id;
	}
 
	public void setId(String id) {
		this.id = id;
	}
 
	public String getTitle() {
		return title;
	}
 
	public void setTitle(String title) {
		this.title = title;
	}
 
	public String getIsvip() {
		return isvip;
	}
 
	public void setIsvip(String isvip) {
		this.isvip = isvip;
	}
 
	public String getIsBuy() {
		return isBuy;
	}
 
	public void setIsBuy(String isBuy) {
		this.isBuy = isBuy;
	}
 
	public String getContent() {
		return content;
	}
 
	public void setContent(String content) {
		this.content = content;
	}
 
	public Date getUpdateDate() {
		return updateDate;
	}
 
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	
	
	/**
	 * @return the chapter
	 */
	public Integer getChapter() {
		return chapter;
	}

	/**
	 * @param chapter the chapter to set
	 */
	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}
 
	/**
	 * @return the bookId
	 */
	public String getBookId() {
		return bookId;
	}

	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	/**
	 * @return the preBookChapterVo
	 */
	public BookChapterVo getPreBookChapterVo() {
		return preBookChapterVo;
	}

	/**
	 * @param preBookChapterVo the preBookChapterVo to set
	 */
	public void setPreBookChapterVo(BookChapterVo preBookChapterVo) {
		this.preBookChapterVo = preBookChapterVo;
	}

	/**
	 * @return the nextBookChapterVo
	 */
	public BookChapterVo getNextBookChapterVo() {
		return nextBookChapterVo;
	}

	/**
	 * @param nextBookChapterVo the nextBookChapterVo to set
	 */
	public void setNextBookChapterVo(BookChapterVo nextBookChapterVo) {
		this.nextBookChapterVo = nextBookChapterVo;
	}



	public String getLastestReadChapterTitle() {
		return lastestReadChapterTitle;
	}

	public void setLastestReadChapterTitle(String lastestReadChapterTitle) {
		this.lastestReadChapterTitle = lastestReadChapterTitle;
	}

	public String getLatestReadTime() {
		return DateUtil.dateToSimpleStr(latestReadTime);
	}

	public void setLatestReadTime(Date latestReadTime) {
		this.latestReadTime = latestReadTime;
	}
	
	
}
