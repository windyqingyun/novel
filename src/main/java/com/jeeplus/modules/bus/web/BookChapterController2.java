package com.jeeplus.modules.bus.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.response.ResponseCode;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.history.service.ViewIpHistoryService;
import com.jeeplus.modules.bus.service.BookChapterService;
import com.jeeplus.modules.bus.service.BookService;



/**
 * 
 * @Description: 小说章节
 * @author lzp
 * @date 2018年5月16日
 * @version V1.0
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/service/interface/bookChapter2")
public class BookChapterController2 extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(BookChapterController2.class);

	@Autowired
	private BookChapterService bookChapterService;
	@Autowired
	private BookService bookService;
	@Autowired
	private ViewIpHistoryService viewIpHistoryService;
	
	/**
	 * 章节目录
	 */
	@RequestMapping("/list/{bookId}")
	public ServerResponse list(@PathVariable("bookId") String bookId,
			@RequestParam("pageNo")Integer pageNo, @RequestParam("pageSize") Integer pageSize, 
			@RequestParam(value = "sort", defaultValue = "0") Integer sort,
			@CookieValue(name=Constants.Cookie.CSESSIONID, required = false) String token,
			HttpServletRequest request, HttpServletResponse response) {
		
		if (StringUtils.isBlank(bookId.trim())) {
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		logger.info("token : {}", token);
		
		BookChapter bookChapter = new BookChapter();
		bookChapter.setBookId(bookId);
		
		Page page = new Page<Book>();
		page.setCycle(false);
		page.setPageNo(pageNo); 
		page.setPageSize(pageSize); 
		if (sort == 1) {
			page.setOrderBy(" a.chapter DESC ");
		}
		@SuppressWarnings("rawtypes")
		ServerResponse res = bookChapterService.findBookChapterPageByBookIdAndUserId(page, bookChapter, token);
		return res;
	}
	

	/**
	 * 章节内容
	 */
	@RequestMapping("/info/{bookId}/{chapter}")
	public ServerResponse info(
			@PathVariable("bookId") String bookId,
			@PathVariable("chapter") Integer chapter,
			@RequestParam(name="userId", required = false)String userId,
			@CookieValue(name=Constants.Cookie.CSESSIONID, required = false) String token) {
		
		if (StringUtils.isBlank(bookId.trim()) || chapter == null) {
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		logger.info("token : {}", token);
		
		BookChapter bookChapter = new BookChapter();
//		bookChapter.setId(bookChapterId);
		bookChapter.setChapter(chapter);
		bookChapter.setBookId(bookId);
		
		ServerResponse serverResponse = bookChapterService.findBookChapterContentUserId(bookChapter, token);
		return serverResponse;
	}

	
}