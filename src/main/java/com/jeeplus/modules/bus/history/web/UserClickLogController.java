package com.jeeplus.modules.bus.history.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.modules.bus.history.service.UserClickLogService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/service/interface/userClickLogController")
public class UserClickLogController {

	@Autowired
	private UserClickLogService userClickLogService;
	
	
	
}
