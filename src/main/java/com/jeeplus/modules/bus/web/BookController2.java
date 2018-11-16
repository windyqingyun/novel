package com.jeeplus.modules.bus.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.log.ActionConstants;
import com.jeeplus.common.log.ControllerLog;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.response.ResponseCode;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.utils.JedisUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.entity.Carousel;
import com.jeeplus.modules.bus.entity.example.CarouselExample;
import com.jeeplus.modules.bus.enums.BookCategoryEnum;
import com.jeeplus.modules.bus.enums.SexEnum;
import com.jeeplus.modules.bus.service.BookChapterService;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.service.CarouselService;
import com.jeeplus.modules.bus.vo.BookVo;

/**
 * 
 * @Description: 书籍
 * @author lzp
 * @date 2018年5月11日
 * @version V1.0
 *
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/service/interface/book2")
public class BookController2 extends BaseController {

	@Autowired
	private BookService bookService;
	@Autowired
	private BookChapterService bookChapterService;
	@Autowired
	private CarouselService carouselService;
	
 

	/**
	 * 书籍列表 
	 * 
	 * @param category 
	 * @param condition 0：女频；1：男频
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/list/{category}/{condition}" }, method = RequestMethod.GET)
	public ServerResponse<List<Book>> list(@PathVariable("category") String category, @PathVariable("condition") String condition,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {

		// 男频 or 女频
		Book book = new Book();
		if (String.valueOf(SexEnum.FEMALE.getCode()).equals(condition)) {
			book.setCondition(SexEnum.FEMALE.getCode());
		} else if (String.valueOf(SexEnum.MALE.getCode()).equals(condition)) {
			book.setCondition(SexEnum.MALE.getCode());
		} else {
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		
		Page<Book> page = new Page<Book>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setCycle(false);
		
		
		String key = JedisUtils.BOOK_PREFIX + condition + ":" + category + ":" + pageNo + pageSize;
		ServerResponse<List<Book>> serverResponse = null;
		serverResponse = findPerPageFromCache(key);
		if (serverResponse != null) {
			return serverResponse;
		}
		
		//精品推荐
		if (BookCategoryEnum.FINE.getDesc().equals(category)) {
				// 按照精品条件筛选(一周内充值排行)
				page = bookService.findPageByFine(page, book);
		}
		// 人气榜 书籍列表	(根据点击数)
		else if(BookCategoryEnum.POPULARITY.getDesc().equals(category)) {
				page = bookService.findPageByPopularity(page, book);
		}
		// 畅销榜 书籍列表 (根据每本书购买的人数)
		else if(BookCategoryEnum.HOT_SELL.getDesc().equals(category)) {
				page = bookService.findPageByHotsell(page, book);
		}
		//新书专区 书籍列表 (根据上传时间距离当前日期小于30天)
		else if(BookCategoryEnum.NEW_BOOK.getDesc().equals(category)) {
				page = bookService.findPageByNewbook(page, book);
		}
		else {
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return ServerResponse.createBySuccess(page.getList());
	}
	
	@SuppressWarnings("rawtypes")
	private ServerResponse findPerPageFromCache(String key) {
		return  (ServerResponse) JedisUtils.getObject(key);
//		return JedisUtil.get(condition+":"+category+":"+pageNo, ServerResponse.class);
	}
	
	@SuppressWarnings("rawtypes")
	private void cachePerPage2Redis(String key, ServerResponse s) {
		JedisUtils.setObject(key, s, 3600 * Integer.parseInt(Global.getConfig("redis.cache.time")));
//		JedisUtil.setEx(condition+":"+category+":"+pageNo, s, 3600 * Integer.parseInt(Global.getConfig("redis.cache.time")));
	}
	
	/**
	 *  首页数据查询缓存  
	 *  缓存前3页数据
	 *  三天更新一次缓存 	
	 *  flag=0点 第一页:currentTime-flag<24; 第二页:24=<currentTime-flag<=48; 第三页:currentTime-flag>48 
	 *  currentTime-flag > 72 => flag=0点
	 */
	private void listFromRedis(String category, String condition, Integer pageNo, Integer pageSize) {
	}
	

	/**
	 * 书籍详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail/{bookId}")
	@ControllerLog(channel="getBookDetail", action=ActionConstants.CLICK_BOOK, title="记录书籍点击记录",isSaveRequestData=true)
	public ServerResponse<BookVo> getBookDetail(HttpServletRequest request, @PathVariable("bookId") String bookId) {
//		// 来源未知
//		if (StringUtils.isBlank(OriginUtil.getOfficeIdByOrigin(request))) {
//			return ServerResponse.createByError(ResponseCode.ILLEGAL_ORIGIN.getCode(), ResponseCode.ILLEGAL_ORIGIN.getDesc());
//		}
		// 参数错误
		if (StringUtils.isBlank(bookId)) {
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return bookService.getBookDetail(request, bookId);
		
	}


	
	/**
	 * 猜你喜欢
	 * @return
	 */
	@RequestMapping("/recommend")
	public ServerResponse recommend(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo
			,@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize
			,@RequestParam(value = "condition", defaultValue = "0") Integer condition
			) {
		Page page = new Page<>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setCycle(false);
		Book book = new Book();
		if (condition == 0 || condition == 1) {
			book.setCondition(condition);
		}else {
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		
		page = bookService.recommend(page, book);
		return ServerResponse.createBySuccess(page.getList());
	}

	
	/**
	 * 轮播图
	 */
	@RequestMapping("/carousel")
	public ServerResponse carousel(@RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "4", required = false) Integer pageSize) {
		CarouselExample example = new CarouselExample();
		example.setPageNo(pageNo);
		example.setPageSize(pageSize);
		List<Carousel> carouselList = carouselService.selectByExample(example);
		return ServerResponse.createBySuccess(carouselList);
	}

	
	/**
	 * book详情的点击量
	 */
	@RequestMapping(value = "/click-count/{bookId}", method = RequestMethod.POST)
	public void clickCount(@PathVariable(value = "bookId") String bookId) {
//		logger.info("book.viewcont : ", bookId);
	}
	


	
}