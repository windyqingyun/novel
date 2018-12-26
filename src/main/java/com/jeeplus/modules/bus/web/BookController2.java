package com.jeeplus.modules.bus.web;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.modules.bus.entity.Feedback;
import com.jeeplus.modules.bus.service.FeedbackService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
import org.springframework.web.client.RestTemplate;

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
	@Autowired
	private FeedbackService feedbackService;
 

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
		
		
		/*String key = JedisUtils.BOOK_PREFIX + condition + ":" + category + ":" + pageNo + pageSize;
		ServerResponse<List<Book>> serverResponse = null;
		serverResponse = findPerPageFromCache(key);
		if (serverResponse != null) {
			return serverResponse;
		}*/

		//精品推荐(手动推荐)
		if (BookCategoryEnum.FINE.getDesc().equals(category)) {
			page = bookService.findFine(page,book);
		}
		// 人气榜 (点击排名最多)(最近三天的点击量)
		else if(BookCategoryEnum.POPULARITY.getDesc().equals(category)) {
			page = bookService.findPageByPopularity(page,book);
		}
		// 热搜榜 搜索量最多
		else if(BookCategoryEnum.HOT_SEARCH.getDesc().equals(category)) {
			page = bookService.findHotSearch(page,book);
		}
		//新书榜 (从新书首发日期算起，30天内点击最多)
		else if(BookCategoryEnum.NEW_BOOK.getDesc().equals(category)) {
			page = bookService.findNewBook(page,book);
		}
		//飙升榜 (一周内点击量上升最快)
		else if(BookCategoryEnum.SOARING.getDesc().equals(category)){
			page = bookService.findSoaring(page,book);
		}
		//收藏榜 (加入书架最多)
		else if(BookCategoryEnum.COLLECTION.getDesc().equals(category)){
			page = bookService.findCollection(page,book);
		}
		//最新入库 (从内容提供商那里获取数据，时间最新的排最前)
		else if(BookCategoryEnum.LATEST_BOOK.getDesc().equals(category)){
			//TODO 带考究
		}
		else {
			//没有分类的查询
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
	 * 猜你喜欢 TODO
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

	/**
	 * 搜索提示
	 * 设计：用户进行模糊搜索给出输入关键字的模糊查询全词
	 * 		如：面包->菠萝面包，草莓面包
	 * 		用户搜索根据提示词汇查询数据，获得程序关键数据
	 * 		用户手动输入词汇，查询数据存在或不存在，并且数据在不存在或多匹配为较多情况
	 * @param bookName
	 * @return
	 */
	@RequestMapping(value = "/findBook", method = RequestMethod.GET)
	public ServerResponse<List<Book>> findBook(HttpServletRequest request,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) throws UnsupportedEncodingException {
		String bookName = new String((request.getParameter("bookName")).getBytes("iso-8859-1"),"utf-8");
		if(StringUtils.isBlank(bookName))
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ORIGIN.getCode(), ResponseCode.ILLEGAL_ORIGIN.getDesc());
		Page<Book> page = new Page<Book>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setCycle(false);
		Book book = new Book();
		book.setName(bookName);
		System.err.println(bookName);
		//模糊查询书名（不需要男频女频）
		page = bookService.findLikeBook(page,book);
		return ServerResponse.createBySuccess(page.getList());
	}

	/**
	 * 搜索详细数据信息:点击搜索
	 * @param bookName
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/findBookSearch", method = RequestMethod.GET)
	public ServerResponse<List<Book>> findBookSearch(HttpServletRequest request,
													 @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
													 @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) throws UnsupportedEncodingException {
		String bookName = new String((request.getParameter("bookName")).getBytes("iso-8859-1"),"utf-8");
		Page<Book> page = new Page<Book>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setCycle(false);
		Book book = new Book();
		book.setName(bookName);
		page = bookService.findLikeBookSearch(page, book);
		return ServerResponse.createBySuccess(page.getList());
	}

	/**
	 * 意见反馈
	 * @return
	 */
	@RequestMapping(value = "/addFeedback", method = RequestMethod.POST)
	public ServerResponse<?> addFeedback(@RequestBody Feedback feedback){
		feedbackService.insert(feedback);
		return ServerResponse.createBySuccess();
	}

	/**
	 * 微信登录
	 * @param code
	 * @return
	 */
	public ServerResponse<?> login(@RequestParam("code") String code){
		//登录微信，成功后返回用户token,调用用户相关接口解析token

	}
}