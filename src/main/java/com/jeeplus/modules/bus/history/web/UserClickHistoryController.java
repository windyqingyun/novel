/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.history.entity.UserClickHistory;
import com.jeeplus.modules.bus.history.service.UserClickHistoryService;
import com.jeeplus.modules.sys.entity.User;

/**
 * 用户点击记录Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/service/interface/userClickHistory")
public class UserClickHistoryController extends BaseController {

	@Autowired
	private UserClickHistoryService userClickHistoryService;

	/**
	 * 保存用户点击记录
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String save(@RequestParam(name = "fodderId", required = false)String fodderId, @RequestParam(name="bookId", required = false)String bookId,
			@RequestParam(name = "userId")String userId) throws Exception{
		String result = null;
		try {
			UserClickHistory userClickHistory = new UserClickHistory();
			userClickHistory.setCreateBy(new User(userId));
			userClickHistory.setBookId(bookId);
			userClickHistory.setFodderId(fodderId);
			
			UserClickHistory originalBean = userClickHistoryService.getClickHistory(userClickHistory);
			
			if(originalBean != null && StringUtils.isNotBlank(originalBean.getId())){
				originalBean.setClickCount(originalBean.getClickCount() + 1);  //增加点击次数
				MyBeanUtils.copyBeanNotNull2Bean(userClickHistory, originalBean);
				userClickHistoryService.update(originalBean);
			}else {//新增表单保存
				userClickHistory.setClickCount(1);
				userClickHistoryService.insert(userClickHistory);
			}
			
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

}