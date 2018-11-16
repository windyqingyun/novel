package com.jeeplus.modules.bus.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.RequestUtils;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.Fodder;
import com.jeeplus.modules.bus.entity.RecommendFodder;
import com.jeeplus.modules.bus.history.entity.UserClickHistory;
import com.jeeplus.modules.bus.history.entity.ViewIpHistory;
import com.jeeplus.modules.bus.history.service.UserClickHistoryService;
import com.jeeplus.modules.bus.history.service.ViewIpHistoryService;
import com.jeeplus.modules.bus.service.FodderService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.DictService;

/**
 * 素材Controller
 * @author zhangsc
 * @version 2017-11-02
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/service/interface/fodder")
public class FodderController extends BaseController {

	@Autowired
	private FodderService fodderService;
	@Autowired
	private UserClickHistoryService userClickHistoryService;
	@Autowired
	private ViewIpHistoryService viewIpHistoryService;
	

	@Autowired
	private DictService dictService;
	
	private List<RecommendFodder> fodderLs = null;
	
//	/**
//	 * 素材内容列表页面
//	 */
//	@RequestMapping(value = "list", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
//	public String list(@RequestParam("pageSize")Integer pageSize, @RequestParam("pageNo")Integer pageNo,
//			@RequestParam(name = "userId", required = false)String userId, 
//			@RequestParam(name = "officeId", required = false)String officeId, 
//			@RequestParam(name = "beginDate", required = false)String beginDate,
//			@RequestParam(name = "endDate", required = false)String endDate,
//			HttpServletRequest request, HttpServletResponse response) {
//		String result = null;
//		try {
//			result = fodderService.findPage(pageNo, pageSize, officeId, beginDate, endDate);
//		} catch (ServiceException e) {
//			logger.error(e.getLocalMsg(), e);
//			result = ServiceUtil.doResponse(e.getResEnum(), true);
//		} catch (Exception e) {
//			logger.error(RespCodeEnum.U1.getResText(), e);
//			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
//		}
//		logger.info("流量端获取结果素材:" + result);
//		return result;
//	}
	
	
//	/**
//	 * 素材内容列表页面（新加方法2018-03-06）
//	 */
//	@RequestMapping(value = "listFodder", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
//	public String listFodder(@RequestParam("pageSize")Integer pageSize, @RequestParam("pageNo")Integer pageNo,
//			@RequestParam(name = "userId", required = false)String userId, 
//			@RequestParam(name = "officeId", required = false)String officeId, 
//			@RequestParam(name = "beginDate", required = false)String beginDate,
//			@RequestParam(name = "endDate", required = false)String endDate,
//			HttpServletRequest request, HttpServletResponse response) {
//		String result = null;
//		try {
//			//获取阀值
//			Dict d = new Dict();
//			d.setType("threshold_pay_coin");
//			List<Dict> listDict = dictService.findList(d);
//			double dbPayCoin = 0;
//			if(null!=listDict && !listDict.isEmpty()){
//				dbPayCoin = Double.parseDouble(listDict.get(0).getValue());
//			}
//			//获取查询条数
//			d.setType("fodder_pagesize");
//			listDict = dictService.findList(d);
//			int fodderPageSize = 0;
//			if(null!=listDict && !listDict.isEmpty()){
//				fodderPageSize = Integer.parseInt(listDict.get(0).getValue());
//			}
//			//获取往前计算多少天
//			d.setType("fodder_beforday");
//			listDict = dictService.findList(d);
//			int intDay = 0;
//			if(null!=listDict && !listDict.isEmpty()){
//				intDay = Integer.parseInt(listDict.get(0).getValue());
//			}
//			//获取计算系数
//			d.setType("fodder_coefficient");
//			listDict = dictService.findList(d);
//			double coefficient = 1;
//			if(null!=listDict && !listDict.isEmpty()){
//				coefficient = Double.parseDouble(listDict.get(0).getValue());
//			}
//			result = fodderService.findPage(pageNo, pageSize, officeId, beginDate, endDate,dbPayCoin, fodderPageSize,intDay,coefficient);
//			fodderLs = fodderService.findRecommendFodderPage(pageNo, pageSize, officeId, beginDate, endDate, dbPayCoin, fodderPageSize, intDay, coefficient);
//			 Thread thread = new StartThread();
//			 thread.start();
//		} catch (ServiceException e) {
//			logger.error(e.getLocalMsg(), e);
//			result = ServiceUtil.doResponse(e.getResEnum(), true);
//		} catch (Exception e) {
//			logger.error(RespCodeEnum.U1.getResText(), e);
//			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
//		}
//		logger.info("流量端获取结果素材:" + result);
//		
//		return result;
//	}
	
//	/**
//	 * 素材内容列表页面
//	 */
//	@RequestMapping(value = "list", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
//	public String list(@RequestParam("pageSize")Integer pageSize, @RequestParam("pageNo")Integer pageNo,
//			@RequestParam(name = "userId", required = false)String userId, 
//			@RequestParam(name = "officeId", required = false)String officeId, 
//			@RequestParam(name = "beginDate", required = false)String beginDate,
//			@RequestParam(name = "endDate", required = false)String endDate,
//			HttpServletRequest request, HttpServletResponse response) {
//		String result = null;
//		try {
//			//获取阀值
//			Dict d = new Dict();
//			d.setType("threshold_pay_coin");
//			List<Dict> listDict = dictService.findList(d);
//			double dbPayCoin = 0;
//			if(null!=listDict && !listDict.isEmpty()){
//				dbPayCoin = Double.parseDouble(listDict.get(0).getValue());
//			}
//			//获取查询条数
//			d.setType("fodder_pagesize");
//			listDict = dictService.findList(d);
//			int fodderPageSize = 0;
//			if(null!=listDict && !listDict.isEmpty()){
//				fodderPageSize = Integer.parseInt(listDict.get(0).getValue());
//			}
//			//获取往前计算多少天
//			d.setType("fodder_beforday");
//			listDict = dictService.findList(d);
//			int intDay = 0;
//			if(null!=listDict && !listDict.isEmpty()){
//				intDay = Integer.parseInt(listDict.get(0).getValue());
//			}
//			//获取计算系数
//			d.setType("fodder_coefficient");
//			listDict = dictService.findList(d);
//			double coefficient = 1;
//			if(null!=listDict && !listDict.isEmpty()){
//				coefficient = Double.parseDouble(listDict.get(0).getValue());
//			}
//			result = fodderService.findRecommendListForAdd(pageNo, pageSize, officeId, fodderPageSize, intDay, coefficient);
//		} catch (ServiceException e) {
//			logger.error(e.getLocalMsg(), e);
//			result = ServiceUtil.doResponse(e.getResEnum(), true);
//		} catch (Exception e) {
//			logger.error(RespCodeEnum.U1.getResText(), e);
//			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
//		}
//		logger.info("流量端获取结果素材:" + result);
//		
//		return result;
//	}
//	
//	/**
//	 * 返回给浏览端的素材集合
//	 * @version 2018-03-14
//	 * @author zhangsc
//	 */
//	@RequestMapping(value = "listFodder", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
//	public String listFodder(@RequestParam("pageSize")Integer pageSize, @RequestParam("pageNo")Integer pageNo,
//			@RequestParam(name = "userId", required = false)String userId, 
//			@RequestParam(name = "officeId", required = false)String officeId, 
//			@RequestParam(name = "beginDate", required = false)String beginDate,
//			@RequestParam(name = "endDate", required = false)String endDate,
//			HttpServletRequest request, HttpServletResponse response) {
//		String result = null;
//		try {
//			//获取阀值
//			Dict d = new Dict();
//			d.setType("threshold_pay_coin");
//			List<Dict> listDict = dictService.findList(d);
//			double dbPayCoin = 0;
//			if(null!=listDict && !listDict.isEmpty()){
//				dbPayCoin = Double.parseDouble(listDict.get(0).getValue());
//			}
//			//获取查询条数
//			d.setType("fodder_pagesize");
//			listDict = dictService.findList(d);
//			int fodderPageSize = 0;
//			if(null!=listDict && !listDict.isEmpty()){
//				fodderPageSize = Integer.parseInt(listDict.get(0).getValue());
//			}
//			//获取往前计算多少天
//			d.setType("fodder_beforday");
//			listDict = dictService.findList(d);
//			int intDay = 0;
//			if(null!=listDict && !listDict.isEmpty()){
//				intDay = Integer.parseInt(listDict.get(0).getValue());
//			}
//			//获取计算系数
//			d.setType("fodder_coefficient");
//			listDict = dictService.findList(d);
//			double coefficient = 1;
//			if(null!=listDict && !listDict.isEmpty()){
//				coefficient = Double.parseDouble(listDict.get(0).getValue());
//			}
//			result = fodderService.findRecommendList(pageNo, pageSize, officeId, fodderPageSize, intDay, coefficient);
//			
//			//开启线程查询
//			fodderLs = fodderService.findRecommendFodderPage(pageNo, pageSize, officeId, beginDate, endDate, dbPayCoin, fodderPageSize, intDay, coefficient);
//			Thread thread = new StartThread();
//			thread.start();
//		} catch (ServiceException e) {
//			logger.error(e.getLocalMsg(), e);
//			result = ServiceUtil.doResponse(e.getResEnum(), true);
//		} catch (Exception e) {
//			logger.error(RespCodeEnum.U1.getResText(), e);
//			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
//		}
//		logger.info("流量端获取结果素材:" + result);
//		
//		return result;
//	}
//	
	
	/**
	 * 素材内容列表页面
	 */
	@RequestMapping(value = "list", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String list(@RequestParam("pageSize")Integer pageSize, @RequestParam("pageNo")Integer pageNo,
			@RequestParam(name = "userId", required = false)String userId, 
			@RequestParam(name = "officeId", required = false)String officeId, 
			@RequestParam(name = "beginDate", required = false)String beginDate,
			@RequestParam(name = "endDate", required = false)String endDate,
			HttpServletRequest request, HttpServletResponse response) {
		String result = null;
		try {
			result = fodderService.findRecommendList(officeId);
		} catch (ServiceException e) {
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("流量端获取结果素材:" + result);
		
		return result;
	}
	
	/**
	 * 返回给浏览端的素材集合
	 * @version 2018-03-14
	 * @author zhangsc
	 */
	@RequestMapping(value = "listFodder", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String listFodder(@RequestParam("pageSize")Integer pageSize, @RequestParam("pageNo")Integer pageNo,
			@RequestParam(name = "userId", required = false)String userId, 
			@RequestParam(name = "officeId", required = false)String officeId, 
			@RequestParam(name = "beginDate", required = false)String beginDate,
			@RequestParam(name = "endDate", required = false)String endDate,
			HttpServletRequest request, HttpServletResponse response) {
		String result = null;
		try {
			result = fodderService.findRecommendList(officeId);
		} catch (ServiceException e) {
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("流量端获取结果素材:" + result);
		
		return result;
	}

	/**
	 * 获取推荐信息
	 * @param pageSize
	 * @param pageNo
	 * @param userId
	 * @param officeId
	 * @param beginDate
	 * @param endDate
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "listRecommendFodder", produces = { "application/json; charset=UTF-8" },method = RequestMethod.POST)
	public String listRecommendFodder(@RequestParam("pageSize")Integer pageSize, @RequestParam("pageNo")Integer pageNo,
			@RequestParam(name = "userId", required = false)String userId, 
			@RequestParam(name = "officeId", required = false)String officeId, 
			@RequestParam(name = "beginDate", required = false)String beginDate,
			@RequestParam(name = "endDate", required = false)String endDate,
			HttpServletRequest request, HttpServletResponse response) {
		String result = null;
		try {
			result = fodderService.findRecommendFodder(pageNo, pageSize, officeId, beginDate, endDate);
		} catch (ServiceException e) {
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("流量端获取结果素材:" + result);
		
		return result;
	}



	/**
	 * 获取素材
	 * @param para findToDayList
	 * @return
	 */
	@RequestMapping(value = "fodderList", method=RequestMethod.POST,
			produces = { "application/json; charset=UTF-8" })
	public String getFodderList(@RequestParam(value = "array[]") String[] array,
								@RequestParam("pageSize")Integer pageSize,
								@RequestParam("pageNo")Integer pageNo,
								HttpServletRequest request) {
		System.out.println(pageSize);

		Fodder fodder=new Fodder();
		ArrayList arrayList=new ArrayList();

		for (String s:array
			 ) {
			arrayList.add(s);
		}

		fodder.setNotViewId(arrayList);

		fodder.setPageSize(5);
		fodder.setPageNo(1);

		List<Fodder>  list= fodderService.findToDayList(fodder);

		System.out.println(list.size());
		Gson gson=new Gson();
		return gson.toJson(list);
	}

	
	
	/**
	 * 获取素材信息
	 * @param para findToDayList
	 * @return
	 */
	@RequestMapping(value = "fodderInfo", method=RequestMethod.POST, 
			produces = { "application/json; charset=UTF-8" })
	public String getFodderInfo(@RequestParam(name = "fodderId")String fodderId, 
								@RequestParam(value = "userId", required = false)String userId,
								HttpServletRequest request) {
		String result = null;
		try {
			if(StringUtils.isNotBlank(fodderId)){
				UserClickHistory userClickHistory = new UserClickHistory();
				userClickHistory.setFodderId(fodderId);
				userClickHistory.setClickCount(1);
				if(StringUtils.isNotBlank(userId))
					userClickHistory.setCreateBy(new User(userId));
				
				//添加作品点击数据
				userClickHistoryService.insert(userClickHistory);
				
				//添加素材点击数量
				//fodderService.addClickCount(fodderId);
				
				viewIpHistoryService.save(new ViewIpHistory(RequestUtils.getIpAddr(request), userId, "fodderInfo", fodderId));
				
				result = fodderService.getFodderInfo(fodderId);
			}else {
				result = ServiceUtil.doResponse(RespCodeEnum.U4, true);
			}
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
	 * 异步执行更新推荐信息
	 * @author Panjp
	 *
	 */
	 class StartThread extends Thread
	 {
	     @Override
	     public void run()
	     {
	    	 fodderService.deleteRecFood();
	    	 fodderService.addRecommendFooder(fodderLs);
	     }
	  }
}