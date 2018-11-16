/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.FodderRecommend;
import com.jeeplus.modules.bus.service.FodderRecommendService;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 推荐素材列表Controller
 * @author zhangsc
 * @version 2018-03-14
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/service/interface/fodderRecommend")
public class FodderRecommendController extends BaseController {

	@Autowired
	private FodderRecommendService fodderRecommendService;
	
	/**
	 * 推荐素材列表列表页面
	 */
	@RequestMapping(value = {"list", ""}, method=RequestMethod.POST)
	public String list(@RequestParam("pageSize")Integer pageSize, @RequestParam("pageNo")Integer pageNo,
			@RequestParam(name = "officeId", required = false)String officeId, 
			@RequestParam(name = "toOfficeId")String toOfficeId,
			@RequestParam(name = "notInId")String notInId,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		String result = null;
		FodderRecommend fodderRecommend = new FodderRecommend();
		fodderRecommend.setOffice(new Office(officeId));
		fodderRecommend.setNotInId(notInId);
		
		Page<FodderRecommend> page = fodderRecommendService.findPage(new Page<FodderRecommend>(pageNo, pageSize), fodderRecommend); 
		//如果查询出来的条数为0，去掉查询条件里的编号，重新查询
		if(page.getCount() == 0){
			fodderRecommend.setOffice(null);
			page = fodderRecommendService.findPage(new Page<FodderRecommend>(pageNo, pageSize), fodderRecommend); 
		}
		
		
		List<Map<String, String>> listMap = Lists.newArrayList();
		if(page.getList() != null){
			for (FodderRecommend recommend : page.getList()) {
				listMap.add(fodderRecommendService.convertFodderToMap(recommend, toOfficeId));
			}	
		}
		
		result = ServiceUtil.getMessage(RespCodeEnum.U0, listMap);
		
		return result;
	}

}