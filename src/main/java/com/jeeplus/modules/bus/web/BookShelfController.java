/**
 * 
 */
package com.jeeplus.modules.bus.web;

import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.response.ResponseCode;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.utils.StringUtil;
import com.jeeplus.modules.bus.entity.UserBookshelf;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.service.UserBookshelfService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;
import com.jeeplus.modules.bus.vo.UserBookShelfVo;
import com.jeeplus.modules.sys.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
	 * 删除书架
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public ServerResponse deleteBookShelf(@RequestParam("ids[]") String[] ids,
										  @CookieValue(name = Constants.Cookie.CSESSIONID, required = false) String token) {
		if (ids == null || ids.length == 0)
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

		if (StringUtils.isBlank(token))
			return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

		String currentCustomerId = CurrentCustomerUtil.getCurrentCustomerId(token);
		if (StringUtils.isBlank(currentCustomerId))
			return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());

		List<UserBookshelf> userBookshelfList = new ArrayList<>();
		for (String id : ids) {
			UserBookshelf userBookshelf = userBookshelfService.get(id);
			if (userBookshelf != null || userBookshelf.getUser() != null) {
				userBookshelfList.add(userBookshelf);
			}
			if (!userBookshelf.getUser().getId().equals(currentCustomerId))
				return ServerResponse.createByError("不能删除别人的书架");
		}

		userBookshelfService.delete(userBookshelfList);
		return ServerResponse.createBySuccessMessage("删除成功");
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
