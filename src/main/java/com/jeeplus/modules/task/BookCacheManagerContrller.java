package com.jeeplus.modules.task;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.RequestUtils;
import com.jeeplus.common.utils.StringUtil;
import com.jeeplus.common.utils.StringUtils;

@Controller
@RequestMapping("manager/cache")
public class BookCacheManagerContrller {

	private static final Logger logger = LoggerFactory.getLogger(BookCacheManagerContrller.class);

	@Autowired
	public ClearBookCacheTask clearBookCacheTask;
	
	@Autowired
	public SelectFineBookTask selectFineBookTask;
	
	@RequestMapping(value = "/clearBookAllTask", method = RequestMethod.POST)
	public void clearBook() {
		clearBookCacheTask.clearBookAll();
	}
	
	@RequestMapping(value = "/cacheBookAllTask", method = RequestMethod.POST)
	public void cacheBook() {
		clearBookCacheTask.cacheBookAll();
	}
	
	
	@RequestMapping(value = "/selectFineBookTask", method = RequestMethod.POST)
	public void selectFineBookTask() {
		selectFineBookTask.selectFineBookTask();
	}
	
	
	@RequestMapping(value = "/modifyConfig/{key}/{value}/admin", method = RequestMethod.POST)
	public void modifyConfig(@PathVariable("key")String key, @PathVariable("value") String value) {
		Global.modifyConfig(key, value);
		logger.info("modifyConfig key: {}, value: {}", key, value);
	}
	
	@RequestMapping(value = "/ip", method = RequestMethod.GET)
	public void modifyConfig(HttpServletRequest request) {
		logger.info("ip: {}", RequestUtils.getIpAddr(request));
		logger.info("ip: {}", RequestUtils.getRequestIp(request));
		
		logger.info("ip: {}", StringUtils.getRemoteAddr(request));
		
		logger.info("ip: {}", StringUtil.getRequestIp(request));
	}
	
}
