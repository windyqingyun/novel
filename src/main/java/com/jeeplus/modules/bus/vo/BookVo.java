/**
 * 
 */
package com.jeeplus.modules.bus.vo;

import java.io.Serializable;

import com.jeeplus.modules.bus.entity.Book;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月15日
 * @version V1.0
 *
 */
public class BookVo extends Book implements Serializable {
 
	private static final long serialVersionUID = 3653390065741223961L;

	private BookChapterVo lastBookChapter;	// 最后一个章节

	private Integer chapterTotalCount;		// 总共的章节数
	
	private BookChapterVo lastReadBookChapter; // 记录用户最后阅读章节
	
	
	
	
	public BookChapterVo getLastBookChapter() {
		return lastBookChapter;
	}

	public void setLastBookChapter(BookChapterVo lastBookChapter) {
		this.lastBookChapter = lastBookChapter;
	}

	public Integer getChapterTotalCount() {
		return chapterTotalCount;
	}
 
	public void setChapterTotalCount(Integer chapterTotalCount) {
		this.chapterTotalCount = chapterTotalCount;
	}

	public BookChapterVo getLastReadBookChapter() {
		return lastReadBookChapter;
	}

	public void setLastReadBookChapter(BookChapterVo lastReadBookChapter) {
		this.lastReadBookChapter = lastReadBookChapter;
	}
	
	
	
}
