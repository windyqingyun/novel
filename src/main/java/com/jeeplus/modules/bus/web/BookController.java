package com.jeeplus.modules.bus.web;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.annotation.ContentProvider;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.RequestUtils;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.service.BookChapterService;
import com.jeeplus.modules.bus.service.BookService;

/**
 * 书籍Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/service/interface/book")
public class BookController extends BaseController {

	@Autowired
	private BookService bookService;
	@Autowired
	private BookChapterService bookChapterService;
	
//	/**
//	 * 书籍列表页面
//	 */
//	@RequiresPermissions("bus:book:list")
//	@RequestMapping(value = {"list", ""})
//	public String list(Book book, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<Book> page = bookService.findPage(new Page<Book>(request, response), book); 
//		model.addAttribute("page", page);
//		return "modules/bus/bookList";
//	}

	/**
	 * 保存书籍
	 * json格式para:{officeId: , data{bookList:[bookField: ,chapterList:[chapterField:]]}}
	 * @throws IOException 
	 */
	@ContentProvider
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(HttpServletRequest request) throws IOException{
		String para = request.getParameter("para");
		String result = null;
		if(StringUtils.isBlank(para)){
			para = RequestUtils.getRequestPayload(request);
		}
	
		try {
			bookService.saveBooksFromInterface(parseAddOrUpdateParam(para));
			result = ServiceUtil.doResponse(RespCodeEnum.U0, true);
		} catch (ServiceException e) {
			logger.error("导入错误:" + saveFile(para));
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error("导入错误:" + saveFile(para));
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("返回结果:" + result);
		return result;
	}
	
	/**
	 * 获取小说传输的最大章节
	 */
	@RequestMapping(value = "getMaxChapterForBook", method = RequestMethod.POST)
	public String getMaxChapterForBook(@RequestParam("bookId")String bookId, @RequestParam("officeId")String officeId ,HttpServletRequest request) throws IOException{
		String result = null;
	
		try {
			String id = bookService.existsBookAndrstId(officeId, bookId);
			if(StringUtils.isBlank(id)){
				result = ServiceUtil.getMessage(RespCodeEnum.U5, "没有找到该小说");
			}else{
				result = ServiceUtil.getMessage(RespCodeEnum.U0, (Object) bookChapterService.getMaxChapterOfBook(id));
			}
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
	
	/**
	 * 更改书籍
	 * json格式para:{officeId: , data{bookList:[bookField: ,chapterList:[chapterField:]]}}
	 * @throws IOException 
	 */
	@ContentProvider
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(HttpServletRequest request) throws IOException{
		String para = request.getParameter("para");
		String result = null;
		if(StringUtils.isBlank(para)){
			para = RequestUtils.getRequestPayload(request);
		}
		
		try {
			bookService.saveBooksFromInterface(parseAddOrUpdateParam(para));
			result = ServiceUtil.doResponse(RespCodeEnum.U0, true);
		} catch (ServiceException e) {
			logger.error("导入错误:" + saveFile(para));
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error("导入错误:" + saveFile(para));
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("返回结果:" + result);
		return result;
	}
	
	
	
	/**
	 * 保存书籍
	 * json格式para:{officeId: , data{bookList:[bookField: ,chapterList:[chapterField:]]}}
	 * @throws IOException 
	 */
	@ContentProvider
	@RequestMapping(value = "urlEncodeAdd", method = RequestMethod.POST)
	public String urlEncodeAdd(HttpServletRequest request) throws IOException{
		String para = request.getParameter("para");
		String result = null;
		if(StringUtils.isBlank(para)){
			para = RequestUtils.getRequestPayload(request);
		}
	    
		try {
			//需要urlDecode
			para = URLDecoder.decode(para, "utf-8");
			bookService.saveBooksFromInterface(parseAddOrUpdateParam(para));
			result = ServiceUtil.doResponse(RespCodeEnum.U0, true);
		} catch (ServiceException e) {
			logger.error("导入错误:" + saveFile(para));
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error("导入错误:" + saveFile(para));
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("返回结果:" + result);
		return result;
	}
	
	/**
	 * 更改书籍
	 * json格式para:{officeId: , data{bookList:[bookField: ,chapterList:[chapterField:]]}}
	 * @throws IOException 
	 */
	@ContentProvider
	@RequestMapping(value = "urlEncodeUpd", method = RequestMethod.POST)
	public String urlEncodeUpd(HttpServletRequest request) throws IOException{
		String para = request.getParameter("para");
		String result = null;
		if(StringUtils.isBlank(para)){
			para = RequestUtils.getRequestPayload(request);
		}
		
		try {
			//需要urlDecode
			para = URLDecoder.decode(para, "utf-8");
			bookService.saveBooksFromInterface(parseAddOrUpdateParam(para));
			result = ServiceUtil.doResponse(RespCodeEnum.U0, true);
		} catch (ServiceException e) {
			logger.error("导入错误:" + saveFile(para));
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error("导入错误:" + saveFile(para));
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("返回结果:" + result);
		return result;
	}
	
	
	
	public JSONObject parseAddOrUpdateParam(String para) throws UnsupportedEncodingException{
		if(para.startsWith("null")){
			para = para.replace("null", "");
		}
		if(para.startsWith("para=")){
			para = para.replace("para=", "");
		}
		
		para = RequestUtils.getJson(para);
		JSONObject jsonObj = JSONObject.parseObject(para);
		if(jsonObj.containsKey("para")){
			jsonObj = jsonObj.getJSONObject("para");
		}
		
		return jsonObj;
	}
	
	public String saveFile(String para) throws IOException{
		String fileName = Global.getConfig("userfiles.basedir")+System.currentTimeMillis()+".txt";
		File file = new File(fileName);
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(para);
		fileWriter.flush();
		fileWriter.close();
		
		return fileName;
	}
	
	/**
	 * 删除书籍
	 * {office:123123,id:123123}
	 * @param request
	 * @return
	 */
	@ContentProvider
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public String delete(HttpServletRequest request){
		String para = request.getParameter("para");
		String result = null;
		
		try {
			logger.info("请求参数:" + para);
			
			JSONObject jsonObj = JSONObject.parseObject(para);
			String id = jsonObj.getString("id");
			String officeId = jsonObj.getString("officeId");
			//获取小说对应的书籍并删除
			String bookId = bookService.existsBookAndrstId(officeId, id);
			
			bookService.deleteByLogic(new Book(bookId));
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