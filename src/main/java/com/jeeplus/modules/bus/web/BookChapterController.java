package com.jeeplus.modules.bus.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.RequestUtils;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.history.entity.ViewIpHistory;
import com.jeeplus.modules.bus.history.service.ViewIpHistoryService;
import com.jeeplus.modules.bus.service.BookChapterService;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;

/**
 * 小说章节内容Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/service/interface/bookChapter")
public class BookChapterController extends BaseController {

	@Autowired
	private BookChapterService bookChapterService;
	@Autowired
	private BookService bookService;
	@Autowired
	private ViewIpHistoryService viewIpHistoryService;
	
	/**
	 * 小说章节内容列表页面
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String list(@RequestParam("pageNo")Integer pageNo, @RequestParam("pageSize")Integer pageSize,
			@RequestParam("bookId")String bookId, @RequestParam(name="userId", required = false)String userId) {
		String result = null;
		try {
			result = bookChapterService.findPage(pageNo, pageSize, bookId, userId);
		} catch (ServiceException e) {
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		
		return result;
	}
	

	/**
	 * 获取小说章节的信息
	 */
	@RequestMapping(value = "info", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String info(@RequestParam("bookId")String bookId, @RequestParam("chapter")Integer chapter,
					@RequestParam(name = "userId", required = false)String userId,
					HttpServletRequest request) {

		String result = null;
		try {
			viewIpHistoryService.save(new ViewIpHistory(RequestUtils.getIpAddr(request), userId, "bookInfo", bookId));
			result = bookChapterService.getBookChapterInfo(request, bookId, chapter, userId);
		} catch (ServiceException e) {
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		
		return result;
	}

	/**
	 * 保存小说章节内容
	 */
	@RequestMapping(value = "save")
	public String save(BookChapter bookChapter, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if(!bookChapter.getIsNewRecord()){//编辑表单保存
			BookChapter t = bookChapterService.get(bookChapter.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(bookChapter, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			bookChapterService.save(t);//保存
		}else{//新增表单保存
			bookChapterService.save(bookChapter);//保存
		}
		addMessage(redirectAttributes, "保存小说章节内容成功");
		return "redirect:"+Global.getAdminPath()+"/bus/bookChapter/?repage";
	}
	
	/**
	 * 删除小说章节
	 * @param request
	 * @return
	 */
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "delete")
	public String delete(HttpServletRequest request){
		String para = request.getParameter("para");
		String result = null;
		
		try {
			logger.info("请求参数:" + para);
			
			JSONObject jsonObj = JSONObject.parseObject(para);
			String officeId = jsonObj.getString("officeId");
			String bookId = jsonObj.getString("bookId");
			Integer chapter = jsonObj.getInteger("chapter");
			
			String chapterId = bookChapterService.exsitsChapterAndrstId(bookService.existsBookAndrstId(officeId, bookId), chapter);
			
			bookChapterService.deleteByLogic(new BookChapter(chapterId));
			result = ServiceUtil.doResponse(RespCodeEnum.U0, true);
		} catch (ServiceException e) {
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("返回结果:" + result);
		return result;
	}
}