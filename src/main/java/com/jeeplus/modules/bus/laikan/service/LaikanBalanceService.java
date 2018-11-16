/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.laikan.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.API.ProviderInterface;
import com.jeeplus.modules.bus.entity.ProviderUser;
import com.jeeplus.modules.bus.entity.UserWallet;
import com.jeeplus.modules.bus.laikan.dao.LaikanBalanceDao;
import com.jeeplus.modules.bus.laikan.entity.LaikanBalance;
import com.jeeplus.modules.bus.service.ProviderUserService;
import com.jeeplus.modules.bus.service.UserWalletService;
import com.jeeplus.modules.bus.web.ProviderInterfaceController;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 来看用户余额Service
 * @author zhangsc
 * @version 2018-01-17
 */
@Service
@Lazy(value=false)
@Transactional(readOnly = true)
public class LaikanBalanceService extends CrudService<LaikanBalanceDao, LaikanBalance> {

	@Autowired
	private ProviderUserService providerUserService;
	@Autowired
	private UserWalletService userWalletService;
	
	public LaikanBalance get(String id) {
		return super.get(id);
	}
	
	public List<LaikanBalance> findList(LaikanBalance laikanBalance) {
		return super.findList(laikanBalance);
	}
	
	public Page<LaikanBalance> findPage(Page<LaikanBalance> page, LaikanBalance laikanBalance) {
		return super.findPage(page, laikanBalance);
	}
	
	@Transactional(readOnly = false)
	public void save(LaikanBalance laikanBalance) {
		super.save(laikanBalance);
	}
	
	@Transactional(readOnly = false)
	public void delete(LaikanBalance laikanBalance) {
		super.delete(laikanBalance);
	}
	
	/**
	 * 清洗用户数据 10秒执行一次
	 * @throws Exception
	 */
//	@Scheduled(fixedDelay = 10000)
	@Transactional(readOnly = false)
	public void transformUser() throws Exception{
		LaikanBalance searchBean = new LaikanBalance();
		searchBean.setIsSuccess(Global.NO);
		Page<LaikanBalance> page = new Page<LaikanBalance>(1, 100);
		Page<LaikanBalance> pageList = findPage(page, searchBean);
		List<LaikanBalance> list = pageList.getList();
		if(list != null && list.size() > 0 ){
			for (LaikanBalance laikanBalance : list) {
				ProviderUser user = new ProviderUser(laikanBalance.getId());
				user.setOffice(new Office("200000002001"));
				
				//获取接口
				ProviderInterface providerInterface = ProviderInterfaceController.providerInterFaceMap.get("200000002001");
				//保存用户
				ProviderUser newUser = providerInterface.getUserInfo("{userId:'"+ user.getId() +"'}", user.getId());
				newUser.setId(newUser.getOriginalId());
				providerUserService.save(newUser);
			
				UserWallet userWallet = userWalletService.getUserWalletByOfficeId(user.getId(), "100000002007");
				//添加用户余额
				userWalletService.addCoinAndTicket(userWallet.getId(), laikanBalance.getMoney(), BigDecimal.ZERO);
				
				//设置标识为清洗成功
				laikanBalance.setIsSuccess(Global.YES);
				save(laikanBalance);
				
				logger.info("用户"+laikanBalance.getId()+"清洗成功");
			}
		}else{
			logger.info("所有用户余额清洗成功");
		}
	}
	
}