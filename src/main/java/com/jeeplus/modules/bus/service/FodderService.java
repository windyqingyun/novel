package com.jeeplus.modules.bus.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.dao.FodderDao;
import com.jeeplus.modules.bus.dao.RecommendFodderDao;
import com.jeeplus.modules.bus.entity.Fodder;
import com.jeeplus.modules.bus.entity.FodderRecommend;
import com.jeeplus.modules.bus.entity.RecommendFodder;
import com.jeeplus.modules.bus.utils.JsonFieldConst;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 素材Service
 * @author zhangsc
 * @version 2017-11-02
 */
@Service
@Transactional(readOnly = true)
public class FodderService extends CrudService<FodderDao, Fodder> {
	
	@Autowired
	private RecommendFodderDao recommendFodderDao;
	@Autowired
	private FodderRecommendService fodderRecommendService;
	
	public Fodder get(String id) {
		return super.get(id);
	}
	
	public List<Fodder> findList(Fodder fodder) {
		return super.findList(fodder);
	}
	
	public Page<Fodder> findPage(Page<Fodder> page, Fodder fodder) {
		return super.findPage(page, fodder);
	}
	
	public Page<Fodder> findFodderPage(Page<Fodder> page, Fodder fodder) {
		fodder.setPage(page);
		page.setList(dao.findFodderPage(fodder));
		return page;
	}
	
	public Page<Fodder> findRecommendFodderPage(Page<Fodder> page, Fodder fodder) {
		fodder.setPage(page);
		page.setList(dao.findRecommendFodderPage(fodder));
		return page;
	}
	
	public Page<Fodder> findNoViewHisFodder(Page<Fodder> page, Fodder fodder){
		fodder.setPage(page);
		page.setList(dao.findNoViewHisFodder(fodder));
		return page;
	}
	
	public List<Fodder> findToDayList(Fodder fodder) {
		return dao.findToDayList(fodder);
	}
	
	public Page<Fodder> findToDayListPage(Page<Fodder> page,  Fodder fodder) {
		fodder.setPage(page);
		page.setList(dao.findToDayList(fodder));
		return page;
	}
	
	/**
	 * 查找新增的素材
	 * @param page
	 * @param fodder
	 * @return
	 */
	public Page<Fodder> findNewFodderPage(Page<Fodder> page,  Fodder fodder) {
		fodder.setPage(page);
		page.setList(dao.findNewFodderPage(fodder));
		return page;
	}
	
	public  Page<RecommendFodder> findRecommendFooderList(Page<RecommendFodder> page,  RecommendFodder fodder) {
		fodder.setPage(page);
		page.setList(recommendFodderDao.findRecommendFooderList(fodder));
		return page;
	}
	
	
	@Transactional(readOnly = false)
	public void save(Fodder fodder) {
		//如果素材的浏览次数是空，那么设置浏览次数设置为0
		if(fodder.getViewcount() == null) {
			fodder.setViewcount(0);
		}
		super.save(fodder);
	}
	
	@Transactional(readOnly = false)
	public void delete(Fodder fodder) {
		super.delete(fodder);
	}
	
	
	@Transactional(readOnly = false)
	public void deleteRecFood() {
		recommendFodderDao.deleteAllRecommendFooder();
	}
	
	@Transactional(readOnly = false)
	public void addRecommendFooder(List<RecommendFodder> fodderLs) {
		recommendFodderDao.addRecommendFooder(fodderLs);
	}
	
	@Transactional(readOnly = false)
	public void addClickCount(String id){
		dao.addClickCount(id);
	}
	
	public String getFodderInfo(String fodderId) {
		String result = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		// 1.转换参数
		ObjectNode object = JsonMapper.createNewObjectNode();
		// 2.查询数据
		Fodder fodder = get(fodderId);
		// 封装的数据到前台需要的数据格式
		object.put("id", fodder.getId());
		object.put("title", fodder.getTitle());
		object.put("content", fodder.getContent());
		object.put("linkUrl", fodder.getLinkUrl());
		object.put("office", fodder.getOffice().getName());
		object.put("viewcount", fodder.getViewcount());
		object.put("bookId", fodder.getBookId());
		object.put("chapter", fodder.getChapter());
		object.put("createDate", DateUtils.formatDate(fodder.getCreateDate(), "yyyy-MM-dd HH:mm"));
		
		resMap.put("data", object);
		resMap.put("resCode", RespCodeEnum.U0.getResCode());
		resMap.put("resText", RespCodeEnum.U0.getFriendlyText());
		
		result = JsonMapper.toJsonString(resMap);

		return result;
	}
	
	/**
	 * 分页查询，返回json数据
	 * @param para
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String findPage(int pageNo, int pageSize, String officeId, String beginDate, String endDate) throws ParseException {
		//构建查询的参数
		Fodder fodder = new Fodder();
		if(StringUtils.isNotBlank(beginDate)){
			fodder.setBeginDate(DateUtils.parseDate(beginDate, "yyyy-MM-dd"));
			if(StringUtils.isBlank(endDate)){
				fodder.setEndDate(DateUtils.addDays(fodder.getBeginDate(), 1));
			}else{
				fodder.setEndDate(DateUtils.addDays(DateUtils.parseDate(endDate, "yyyy-MM-dd"), 1));
			}
		}
		
		//构建返回值
		Page<Fodder> page = new Page<Fodder>(pageNo, pageSize);
		Page<Fodder> fodderPage = findPage(page, fodder);
		Map resultDataMap = Maps.newHashMap();
		resultDataMap.put(JsonFieldConst.PAGE_COUNT, new Long(fodderPage.getCount()).intValue());

		List<Map> fodderMapList = Lists.newArrayList();
		if(fodderPage.getList() != null && !fodderPage.getList().isEmpty()) {
			//将素材集合返回，素材json串中不用包含内容内容字段
			for (Fodder f : fodderPage.getList()) {
				fodderMapList.add(convertFodderToMap(f, officeId));
			}
		}
		resultDataMap.put(JsonFieldConst.LIST, fodderMapList);
		
		return ServiceUtil.getMessage(RespCodeEnum.U0, resultDataMap);
	}
	
	/**
	 * 分页查询，返回json数据
	 * @param para
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String findRecommendFodder(int pageNo, int pageSize, String officeId, String beginDate, String endDate) throws ParseException {
		//构建查询的参数
		RecommendFodder fodder = new RecommendFodder();
		if(StringUtils.isNotBlank(beginDate)){
			fodder.setBeginDate(DateUtils.parseDate(beginDate, "yyyy-MM-dd"));
			if(StringUtils.isBlank(endDate)){
				fodder.setEndDate(DateUtils.addDays(fodder.getBeginDate(), 1));
			}else{
				fodder.setEndDate(DateUtils.addDays(DateUtils.parseDate(endDate, "yyyy-MM-dd"), 1));
			}
		}
		
		//构建返回值
		Page<RecommendFodder> page = new Page<RecommendFodder>(pageNo, pageSize);
		Page<RecommendFodder> fodderPage = findRecommendFooderList(page, fodder);
		Map resultDataMap = Maps.newHashMap();
		resultDataMap.put(JsonFieldConst.PAGE_COUNT, new Long(fodderPage.getCount()).intValue());
		List<RecommendFodder> lsTop = new ArrayList<>();
		List<RecommendFodder> ls = new ArrayList<>();
		if(fodderPage.getList() != null && !fodderPage.getList().isEmpty()) {
			//将素材集合返回，素材json串中不用包含内容内容字段
			for (RecommendFodder f : fodderPage.getList()) {
				if(officeId.equals(f.getOffice().getId())){
					lsTop.add(f);
				}else{
					ls.add(f);
				}
			}
		}
		List<Map> fodderMapList = Lists.newArrayList();
		if(lsTop != null && !lsTop.isEmpty()) {
			//将素材集合返回，素材json串中不用包含内容内容字段
			for (RecommendFodder f : lsTop) {
				fodderMapList.add(convertFodderToMap(f, officeId));
			}
		}
		if(ls != null && !ls.isEmpty()) {
			//将素材集合返回，素材json串中不用包含内容内容字段
			for (RecommendFodder f : ls) {
				fodderMapList.add(convertFodderToMap(f, officeId));
			}
		}
		
	
		resultDataMap.put(JsonFieldConst.LIST, fodderMapList);
		
		return ServiceUtil.getMessage(RespCodeEnum.U0, resultDataMap);
	}
	
	
	/**
	 * 分页查询，返回json数据
	 * @param para
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String findPage(int pageNo, int pageSize, String officeId, String beginDate, String endDate,double dbPayCoin,
			int fodderPageSize,int intDay,double coefficient) throws ParseException {
		//构建查询的参数
		Fodder fodder = new Fodder();
		/*if(StringUtils.isNotBlank(beginDate)){
			fodder.setBeginDate(DateUtils.parseDate(beginDate, "yyyy-MM-dd"));
			if(StringUtils.isBlank(endDate)){
				fodder.setEndDate(DateUtils.addDays(fodder.getBeginDate(), 1));
			}else{
				fodder.setEndDate(DateUtils.addDays(DateUtils.parseDate(endDate, "yyyy-MM-dd"), 1));
			}
		}*/
		fodder.setBeginDate(DateUtils.parseDate(DateUtils.beforDate(intDay)+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		fodder.setEndDate(DateUtils.parseDate(DateUtils.beforDate(1)+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		//先根据充值排序查询百分之80,40条
		int foudPageSize =  (int)(fodderPageSize*coefficient);
		//构建返回值
		Page<Fodder> page = new Page<Fodder>(pageNo,foudPageSize);
		fodder.setDbPayCoin(dbPayCoin);
		Page<Fodder> fodderPage = findFodderPage(page, fodder);
		Map resultDataMap = Maps.newHashMap();
		
		List<Map> fodderMapList = Lists.newArrayList();
		//剩下20%的条目为平台当天新增小说的素材。
		Page<Fodder> pageNew = new Page<Fodder>(pageNo, fodderPageSize-foudPageSize);
		
		Page<Fodder> fodderPageNew = findToDayListPage(pageNew, fodder);
		int pageCont = 0;
		List<RecommendFodder> insertRecommendFodder = new ArrayList<>();
		int size = 0;
		if(fodderPage.getList() != null && !fodderPage.getList().isEmpty()) {
			pageCont +=fodderPage.getList().size();
			//将素材集合返回，素材json串中不用包含内容内容字段
			for (Fodder f : fodderPage.getList()) {
				fodderMapList.add(convertFodderToMap(f, officeId));
				size++;
				f.setCreateDate(new Date());
				RecommendFodder r = new RecommendFodder();
				try {
					BeanUtils.copyProperties(r, f);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				r.setSort(size);
				insertRecommendFodder.add(r);
			}
		}
		if(fodderPageNew.getList() != null && !fodderPageNew.getList().isEmpty()) {
			pageCont +=fodderPageNew.getList().size();
			//将素材集合返回，素材json串中不用包含内容内容字段
			for (Fodder f : fodderPageNew.getList()) {
				fodderMapList.add(convertFodderToMap(f, officeId));
				size++;
				f.setCreateDate(new Date());
				RecommendFodder r = new RecommendFodder();
				try {
					BeanUtils.copyProperties(r, f);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				r.setSort(size);
				insertRecommendFodder.add(r);
			}
		}
		resultDataMap.put(JsonFieldConst.PAGE_COUNT, new Long(pageCont));
		resultDataMap.put(JsonFieldConst.LIST, fodderMapList);
		//删除推荐表数据，然后重新添加。
		return ServiceUtil.getMessage(RespCodeEnum.U0, resultDataMap);
	}
	
	/**
	 * 分页查询，返回json数据
	 * @param para
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RecommendFodder> findRecommendFodderPage(int pageNo, int pageSize, String officeId, String beginDate, String endDate,double dbPayCoin,
			int fodderPageSize,int intDay,double coefficient) throws ParseException {
		//构建查询的参数
		Fodder fodder = new Fodder();
		/*if(StringUtils.isNotBlank(beginDate)){
			fodder.setBeginDate(DateUtils.parseDate(beginDate, "yyyy-MM-dd"));
			if(StringUtils.isBlank(endDate)){
				fodder.setEndDate(DateUtils.addDays(fodder.getBeginDate(), 1));
			}else{
				fodder.setEndDate(DateUtils.addDays(DateUtils.parseDate(endDate, "yyyy-MM-dd"), 1));
			}
		}*/
		fodder.setBeginDate(DateUtils.parseDate(DateUtils.beforDate(intDay)+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		fodder.setEndDate(DateUtils.parseDate(DateUtils.beforDate(1)+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		//先根据充值排序查询百分之80,40条
		int foudPageSize =  (int)(fodderPageSize*coefficient);
		//构建返回值
		Page<Fodder> page = new Page<Fodder>(pageNo,foudPageSize);
		fodder.setDbPayCoin(dbPayCoin);
		Page<Fodder> fodderPage = findFodderPage(page, fodder);
		Map resultDataMap = Maps.newHashMap();
		
		List<Map> fodderMapList = Lists.newArrayList();
		//剩下20%的条目为平台当天新增小说的素材。
		Page<Fodder> pageNew = new Page<Fodder>(pageNo, fodderPageSize-foudPageSize);
		
		Page<Fodder> fodderPageNew = findToDayListPage(pageNew, fodder);
		List<RecommendFodder> insertRecommendFodder = new ArrayList<>();
		int size = 0;
		if(fodderPage.getList() != null && !fodderPage.getList().isEmpty()) {
			//将素材集合返回，素材json串中不用包含内容内容字段
			for (Fodder f : fodderPage.getList()) {
				size++;
				f.setCreateDate(new Date());
				RecommendFodder r = new RecommendFodder();
				try {
					BeanUtils.copyProperties(r, f);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				r.setSort(size);
				insertRecommendFodder.add(r);
			}
		}
		if(fodderPageNew.getList() != null && !fodderPageNew.getList().isEmpty()) {
			//将素材集合返回，素材json串中不用包含内容内容字段
			for (Fodder f : fodderPageNew.getList()) {
				size++;
				f.setCreateDate(new Date());
				RecommendFodder r = new RecommendFodder();
				try {
					BeanUtils.copyProperties(r, f);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				r.setSort(size);
				insertRecommendFodder.add(r);
			}
		}
		return insertRecommendFodder;
	}
	
	/**
	 * 查询推荐的列表
	 * @param para
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String findRecommendListForAdd(int pageNo, int pageSize, final String officeId,
			int fodderPageSize,int intDay,double coefficient) throws ParseException {
		final Date today = DateUtils.getMinDateTimeOfDate(new Date());
		Long pageCont = new Long(0);
		Map resultDataMap = Maps.newHashMap();
		List<Map> fodderMapList = Lists.newArrayList();
	
		//判断推荐列表里是否有存在的素材，一本素材展3天
		FodderRecommend queryBean = new FodderRecommend();
		queryBean.setMaxInvalidDate(today);
		queryBean.setEndDate(DateUtils.addDays(today, 3));
		
		List<FodderRecommend> list = fodderRecommendService.findList(queryBean);
		if(list != null && list.size() > 0){
			pageCont = new Long(list.size());
			for (FodderRecommend fod : list) {
				fodderMapList.add(fodderRecommendService.convertFodderToMap(fod, officeId));
			}
		}else {
			final int groupNum = fodderRecommendService.getMaxGroupNum();   //所属第几组
			pageNo = groupNum;                                              //添加的话按照第一列添加
			
			//构建查询的参数
			Fodder fodder = new Fodder();
			fodder.setBeginDate(DateUtils.parseDate(DateUtils.beforDate(intDay)+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
			fodder.setEndDate(DateUtils.parseDate(DateUtils.beforDate(1)+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
			
			//先根据充值排序查询百分之50,10条
			int foudPageSize =  (int)(fodderPageSize * coefficient);
			
			//构建返回值
			Page<Fodder> page = new Page<Fodder>(pageNo,foudPageSize);
			Page<Fodder> fodderPage = findRecommendFodderPage(page, fodder);
			
			//剩下50%的条目为最近三天入库的小说
			Page<Fodder> pageNew = new Page<Fodder>(pageNo, fodderPageSize-foudPageSize);
			Fodder newFodderQueryBean = new Fodder();
			newFodderQueryBean.setBeginDate(DateUtils.getMinDateTimeOfDate(DateUtils.addDays(new Date(), -3)));
			newFodderQueryBean.setEndDate(new Date());
			Page<Fodder> fodderPageNew = findNewFodderPage(pageNew, newFodderQueryBean);
			
			if(fodderPage.getList() == null){
				fodderPage.setList(new ArrayList<Fodder>());
			}
			if(fodderPageNew.getList() == null){
				fodderPageNew.setList(new ArrayList<Fodder>());
			}
			//新入库小说不够的话用没有浏览记录的素材补上
			if(fodderPageNew.getCount() < fodderPageSize-foudPageSize){
				Page<Fodder> noViewPage = new Page<Fodder>(pageNo, (fodderPageSize-foudPageSize) - (int)pageNew.getCount());
				fodder.setEndDate(DateUtils.addDays(fodder.getEndDate(), -2));
				Page<Fodder> noViewFodderPage = findNoViewHisFodder(noViewPage, fodder);
				if(noViewFodderPage.getCount() > 0){
					fodderPage.setCount(fodderPage.getCount() + noViewFodderPage.getCount());
					fodderPage.getList().addAll(noViewFodderPage.getList());
				}
			}
			
			if(fodderPageNew.getList() != null){
				fodderPage.getList().addAll(fodderPageNew.getList());
			}
			
			final List<Fodder> fodderList = fodderPage.getList();
			for (Fodder fod : fodderList) {
				fodderMapList.add(convertFodderToMapGenId(fod, officeId));
			}
			
			//TODO 在此处需要开启一个线程，把推荐的入库
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Office toOffice = new Office(officeId);
					//设置过期时间为今天加上4天
					Date invalidDate = DateUtils.addDays(today, 3);
					for (Fodder fod : fodderList) {
						//循环保存三天，每次展示都查询当天的
						for (int i = 1; i < 4; i++) {
							fodderRecommendService.save(new FodderRecommend(fod, toOffice, invalidDate, groupNum+1, DateUtils.addDays(today, i)));
						}
					}
				}
			});
			thread.start();
			pageCont = fodderPageNew.getCount() + fodderPage.getCount();
		}
		
		resultDataMap.put(JsonFieldConst.PAGE_COUNT, pageCont);
		resultDataMap.put(JsonFieldConst.LIST, fodderMapList);
		
		return ServiceUtil.getMessage(RespCodeEnum.U0, resultDataMap);
	}
	
	
//	/**
//	 * 查询推荐的列表
//	 * @param para
//	 * @return
//	 * @throws ParseException 
//	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public String findRecommendList(int pageNo, int pageSize, final String officeId,
//			int fodderPageSize,int intDay,double coefficient) throws ParseException {
//		final Date today = DateUtils.getMinDateTimeOfDate(new Date());
//		Long pageCont = new Long(0);
//		Map resultDataMap = Maps.newHashMap();
//		List<Map> fodderMapList = Lists.newArrayList();
//	
//		//判断推荐列表里是否有存在的素材，一本素材展3天
//		FodderRecommend queryBean = new FodderRecommend();
//		queryBean.setBeginDate(DateUtils.getMinDateTimeOfDate(DateUtils.addDays(new Date(), -1)));
//		queryBean.setEndDate(DateUtils.getMaxDateTimeOfDate(DateUtils.addDays(new Date(), -1)));
//		
//		List<FodderRecommend> list = fodderRecommendService.findList(queryBean);
//		if(list != null && list.size() > 0){
//			pageCont = new Long(list.size());
//			for (FodderRecommend fod : list) {
//				fodderMapList.add(fodderRecommendService.convertFodderToMap(fod, officeId));
//			}
//		}
//		resultDataMap.put(JsonFieldConst.PAGE_COUNT, pageCont);
//		resultDataMap.put(JsonFieldConst.LIST, fodderMapList);
//		
//		return ServiceUtil.getMessage(RespCodeEnum.U0, resultDataMap);
//	}
	
	
	/**
	 * 查询推荐的列表
	 * @param para
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String findRecommendList(String toOfficeId) throws ParseException {
		Map resultDataMap = Maps.newHashMap();
		List<FodderRecommend> list = fodderRecommendService.findList(new FodderRecommend());
		
		List<Map> fodderMapList = Lists.newArrayList();
		if(list != null && list.size() > 0){
			for (FodderRecommend fod : list) {
				fodderMapList.add(fodderRecommendService.convertFodderToMap(fod, toOfficeId));
			}
		}
		resultDataMap.put(JsonFieldConst.PAGE_COUNT, list.size());
		resultDataMap.put(JsonFieldConst.LIST, fodderMapList);
		
		return ServiceUtil.getMessage(RespCodeEnum.U0, resultDataMap);
	}
	
	/**
	 * 转换fodder为map  map不包含素材内容，是用于list查看的
	 * @param fodder
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map convertFodderToMapGenId(Fodder fodder, String officeId) {
		Map fodderMap = Maps.newHashMap();
		fodderMap.put("id", fodder.getId());
		fodderMap.put("title", fodder.getTitle());
		fodderMap.put("titleImageUrl", Global.downloadArchiveURL+"?url="+fodder.getTitleImage());
		fodderMap.put("viewCount", fodder.getViewcount());
		fodderMap.put("officeName", fodder.getOffice().getName());
		fodderMap.put("href", "http://www.content.vip/system/html/materialPage.html?fodderId="+fodder.getId()+"&officeId="+officeId);
		fodderMap.put("createDate", DateUtils.formatDate(fodder.getCreateDate(), "yyyy-MM-dd"));
		fodder.setNewId(IdGen.uuid());
		
		return fodderMap;
	}
	
	/**
	 * 转换fodder为map  map不包含素材内容，是用于list查看的
	 * @param fodder
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map convertFodderToMap(Fodder fodder, String officeId) {
		Map fodderMap = Maps.newHashMap();
		fodderMap.put("id", fodder.getId());
		fodderMap.put("title", fodder.getTitle());
		fodderMap.put("titleImageUrl", Global.downloadArchiveURL+"?url="+fodder.getTitleImage());
		fodderMap.put("viewCount", fodder.getViewcount());
		fodderMap.put("officeName", fodder.getOffice().getName());
		fodderMap.put("href", "http://www.content.vip/system/html/materialPage.html?fodderId="+fodder.getId()+"&officeId="+officeId);
		fodderMap.put("createDate", DateUtils.formatDate(fodder.getCreateDate(), "yyyy-MM-dd"));
		
		return fodderMap;
	}
	/**
	 * 转换fodder为map  map不包含素材内容，是用于list查看的
	 * @param fodder
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map convertFodderToMap(RecommendFodder fodder, String officeId) {
		Map fodderMap = Maps.newHashMap();
		fodderMap.put("id", fodder.getId());
		fodderMap.put("title", fodder.getTitle());
		fodderMap.put("titleImageUrl", Global.downloadArchiveURL+"?url="+fodder.getTitleImage());
		fodderMap.put("viewCount", fodder.getViewcount());
		fodderMap.put("officeName", fodder.getOffice().getName());
		fodderMap.put("href", "http://www.content.vip/system/html/materialPage.html?fodderId="+fodder.getId()+"&officeId="+officeId);
		fodderMap.put("createDate", DateUtils.formatDate(fodder.getCreateDate(), "yyyy-MM-dd"));
		
		return fodderMap;
	}
	
}