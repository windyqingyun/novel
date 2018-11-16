/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.history.entity.UserReadHistory;
import com.jeeplus.modules.bus.history.service.UserReadHistoryService;
import com.jeeplus.modules.bus.service.BookChapterService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;
import com.jeeplus.modules.sys.entity.User;

/**
 * 用户阅读记录Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/service/interface/userReadHistory")
public class UserReadHistoryController extends BaseController {

	@Autowired
	private UserReadHistoryService userReadHistoryService;
	@Autowired
	private BookChapterService bookChapterService;
	
	/**
	 * 获取用户阅读历史
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "list", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String list(UserReadHistory userReadHistory, HttpServletRequest request){
		String currentCustomerId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(currentCustomerId)) {
			return ServiceUtil.getMessage(RespCodeEnum.U6, true);
		}
		User user = new User();
		user.setId(currentCustomerId);
		userReadHistory.setCreateBy(user);
		String result = null;
		try {
			List<Map> mapList = Lists.newArrayList();
			if(userReadHistory != null && userReadHistory.getCreateBy() != null && StringUtils.isNotBlank(userReadHistory.getCreateBy().getId())){
				/*
				 * map  title","titleImage","officeName","createDate"
				 */
				mapList = userReadHistoryService.findReadHistoryInfoList(userReadHistory);
				
				result = ServiceUtil.getMessage(RespCodeEnum.U0, mapList);
			}else{
				result = ServiceUtil.getMessage(RespCodeEnum.U0, mapList);
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
	 * 保存用户阅读记录
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String save(HttpServletRequest request,
			@RequestParam(name = "userId", required = false)String userId, 
			@RequestParam(name = "bookId", required = false)String bookId,
			@RequestParam(name = "chapter", required = false)Integer chapter, 
			@RequestParam(name = "chapterTitle", required = false)String chapterTitle, 
			@RequestParam(name = "fodderId", required = false)String fodderId) throws Exception{
		logger.info("UserReadHistoryController.save : {}", userId);
		if (StringUtils.isBlank(bookId) || (chapter == null || chapter < 0)) {
			return ServiceUtil.getMessage(RespCodeEnum.U4, "bookId or chapter null");
		}
		userId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(userId)) {
			return "";
		}
		
		BookChapter bookChapter = new BookChapter();
		bookChapter.setBookId(bookId);
		bookChapter.setChapter(chapter);
		BookChapter existbookChapter = bookChapterService.getByChapterAndBookId(bookChapter);
		if (existbookChapter == null) {
			return ServiceUtil.getMessage(RespCodeEnum.U4, "chapterTitle null");
		}
		
		String result = null;
		try {
			// 执行逻辑
			UserReadHistory userReadHistory = new UserReadHistory();
			userReadHistory.setBookId(bookId);
			userReadHistory.setChapter(chapter);                       //设置新的阅读章节，如果原有该记录则更新，否则保存
			userReadHistory.setChapterTitle(existbookChapter.getTitle());
			User user = new User(userId);
			userReadHistory.setCreateBy(user);
			UserReadHistory h = userReadHistoryService.getUserReadHistoryByCustomerId(userReadHistory);//从数据库取出记录的值
			if(h != null && !StringUtils.isBlank(h.getId())){
				MyBeanUtils.copyBeanNotNull2Bean(userReadHistory, h);	//将编辑表单中的非NULL值覆盖数据库记录中的值
				h.setUpdateBy(user);
				userReadHistoryService.update(h);//保存
			}else {
				userReadHistoryService.insert(userReadHistory);//保存
			}
			result = ServiceUtil.getMessage(RespCodeEnum.U0, "");
			
			
			/*if(StringUtils.isBlank(fodderId) && StringUtils.isBlank(bookId)) {
				result = ServiceUtil.getMessage(RespCodeEnum.U4, "素材编号和小说编号至少有一个不为空");
			}else{
				//如果传入了小说编号，那么章节也是必传项
				if(StringUtils.isNotBlank(bookId) && (chapter == null || chapter < 1)){
					result = ServiceUtil.getMessage(RespCodeEnum.U4, "章节不能为空");
				}else{
					UserReadHistory userReadHistory = new UserReadHistory();
					userReadHistory.setBookId(bookId);
					userReadHistory.setFodderId(fodderId);
					userReadHistory.setCreateBy(new User(userId));
					
					UserReadHistory t = userReadHistoryService.getUserReadHistory(userReadHistory);//从数据库取出记录的值
					userReadHistory.setChapter(chapter);                       //设置新的阅读章节，如果原有该记录则更新，否则保存
					if(t != null && StringUtils.isNotBlank(t.getId())){
						MyBeanUtils.copyBeanNotNull2Bean(userReadHistory, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
						userReadHistoryService.update(t);//保存
					}else {
						userReadHistoryService.insert(userReadHistory);//保存
					}
					result = ServiceUtil.getMessage(RespCodeEnum.U0, "");
				}
			}*/
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
	 * 删除用户阅读记录
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST, 
			produces = { "application/json; charset=UTF-8" })
	public String delete(@RequestParam("id")String id) {
		String result = null;
		try {
			userReadHistoryService.delete(new UserReadHistory(id));
			result = ServiceUtil.getMessage(RespCodeEnum.U0, "");
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
	 * 获取用户阅读小说的最后阅读章节
	 * @param bookId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping(value = "getLastViewChapter", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String getLastViewChapter(HttpServletRequest request,
			@RequestParam(name = "fodderId",required = false)String fodderId,
//			@RequestParam(name = "chapter", required = false)Integer chapter,
			@RequestParam(name = "bookId", required = false)String bookId, 
			@RequestParam("userId")String userId){
		userId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(userId)) {
			return ServiceUtil.doResponse(RespCodeEnum.U6, true);
		}
		
		String result = null;
		try{
			UserReadHistory searchBean = new UserReadHistory();
			searchBean.setFodderId(fodderId);
//			searchBean.setChapter(chapter);
			searchBean.setCreateBy(new User(userId));
			searchBean.setBookId(bookId);
			
			UserReadHistory userReadHistory =  userReadHistoryService.getUserReadHistory(searchBean);
			
			Map dataMap = Maps.newHashMap();
			if(userReadHistory != null && StringUtils.isNotBlank(userReadHistory.getId()) && userReadHistory.getChapter() != null) {
				//如果存在小说阅读记录返回上次阅读章节
				dataMap.put("bookId", userReadHistory.getBookId());
				dataMap.put("chapter", userReadHistory.getChapter());
				result = ServiceUtil.getMessage(RespCodeEnum.U0, dataMap);
			}else {
				result = ServiceUtil.getMessage(RespCodeEnum.U0, "");
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

}