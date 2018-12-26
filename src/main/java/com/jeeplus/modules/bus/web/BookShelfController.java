/**
 * 
 */
package com.jeeplus.modules.bus.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.common.cache.GuavaCache;
import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.response.ResponseCode;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.entity.UserBookshelf;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.service.UserBookshelfService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;
import com.jeeplus.modules.bus.vo.UserBookShelfVo;
import com.jeeplus.modules.sys.entity.User;

/**
 * @Description: 书架Controller
 * @author lzp
 * @date 2018年5月17日
 * @version V1.0
 *
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/service/interface/bookshelf")
public class BookShelfController {
	private Logger logger = LoggerFactory.getLogger(BookShelfController.class);

	@Autowired
	private UserBookshelfService userBookshelfService;
	@Autowired
	private BookService bookService;
	
	/**
	 * 加入书架
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "add/{bookId}", method = RequestMethod.POST)
	public ServerResponse addBookshelf(@PathVariable("bookId") String bookId
			, @CookieValue(name=Constants.Cookie.CSESSIONID, required = false) String token
			) {
		if (StringUtils.isBlank(bookId)) {
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		
		logger.info("token : {}", token);
		if (StringUtils.isBlank(token)) {
			return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
 
		UserBookshelf userBookshelf = new UserBookshelf();
		String currentCustomerId = CurrentCustomerUtil.getCurrentCustomerId(token);
		if (!StringUtils.isBlank(currentCustomerId)) {
			User user = new User();
			user.setId(currentCustomerId);
			userBookshelf.setUser(user);
			userBookshelf.setBookId(bookId);
		} else {
			return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		
		return userBookshelfService.addBookToUserBookShelf(userBookshelf);
	}
	

	/**
	 * 我的书架  //TODO
	 * @return
	 */
	@RequestMapping(value = "/person",method = RequestMethod.GET)
	public ServerResponse personBookshelf(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
			@CookieValue(name=Constants.Cookie.CSESSIONID, required = false) String token,
			HttpServletRequest request) {
		logger.info("token : {}", token);
		
		if (StringUtils.isBlank(token)) {
			return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		
		UserBookShelfVo userBookShelfVo = new UserBookShelfVo();
		String currentCustomerId = CurrentCustomerUtil.getCurrentCustomerId(token);
		if (!StringUtils.isBlank(currentCustomerId)) {
			userBookShelfVo.setCustomerId(currentCustomerId);
		} else {
			return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		
		Page<UserBookShelfVo> page = new Page<UserBookShelfVo>(pageNo, pageSize);
		page.setCycle(false);
		page.setOrderBy(" tmp.create_date desc ");
		
		Page<UserBookShelfVo> userBookShelfVoPage = userBookshelfService.findBookPageByBookshelf(page, userBookShelfVo);
		return ServerResponse.createBySuccess(userBookShelfVoPage.getList());
	}
	
}
