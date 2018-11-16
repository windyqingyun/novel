package com.jeeplus.modules.task;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.response.ResponseCode;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.utils.JedisUtil;
import com.jeeplus.common.utils.JedisUtils;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.enums.BookCategoryEnum;
import com.jeeplus.modules.bus.enums.SexEnum;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.service.CarouselService;

@Service
@EnableScheduling
@Lazy(false)
public class ClearBookCacheTask {

	private static final Logger logger = LoggerFactory.getLogger(ClearBookCacheTask.class);

	@Autowired
	public BookService bookService;
	
	@Autowired
	public CarouselService carouselService;

	/**
	 * 每个月1号2:20开始 每隔3天执行一次
	 * 清理书籍缓存
	 */
	
//	@Scheduled(cron = "0 0/3 * * * ? ")
	@Scheduled(cron = "0 29 2 1/3 * ?")
	public void clearBookAll() {
		logger.info("清理任务start");
		long t1 = System.currentTimeMillis();
		Long delBatch = JedisUtil.delBatch(JedisUtils.BOOK_PREFIX + "*");
		logger.info("delBatch : {}", delBatch);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long t2 = System.currentTimeMillis();
		logger.info("清理任务sussessful, 用时: {} 秒", (t2 - t1) / 1000);
		
	}
	
	
	/**
	 * 缓存书籍
	 */
	@Scheduled(cron = "0 30 2 1/3 * ?")
	public void cacheBookAll() {
		long t3 = System.currentTimeMillis();
		Integer cacheTime = 3600 * Integer.parseInt(Global.getConfig("redis.cache.time"));
		
		logger.info("cache任务start");
		Integer finePrePage = Integer.parseInt(Global.getConfig("gyf.pre.fine.prePage"));
		Integer finePrePageSize = Integer.parseInt(Global.getConfig("gyf.pre.fine.prePageSize"));
		Integer finePageSize = Integer.parseInt(Global.getConfig("gyf.fine.pageSize"));
		Integer fineWithPage = Integer.parseInt(Global.getConfig("gyf.pre.fine.withPage"));

		cacheBook(BookCategoryEnum.FINE.getDesc(), String.valueOf(SexEnum.FEMALE.getCode()), finePrePage, finePrePageSize, fineWithPage,
				finePageSize, cacheTime);
		cacheBook(BookCategoryEnum.FINE.getDesc(), String.valueOf(SexEnum.MALE.getCode()), finePrePage, finePrePageSize, fineWithPage,
				finePageSize, cacheTime);
		
		Integer popularityPrePage = Integer.parseInt(Global.getConfig("gyf.pre.popularity.prePage"));
		Integer popularityPrePageSize = Integer.parseInt(Global.getConfig("gyf.pre.popularity.prePageSize"));
		Integer popularityPageSize = Integer.parseInt(Global.getConfig("gyf.popularity.pageSize"));
		Integer popularityWithPage = Integer.parseInt(Global.getConfig("gyf.pre.popularity.withPage"));
		
		cacheBook(BookCategoryEnum.POPULARITY.getDesc(), String.valueOf(SexEnum.FEMALE.getCode()), popularityPrePage, popularityPrePageSize, popularityWithPage,
				popularityPageSize, cacheTime);
		cacheBook(BookCategoryEnum.POPULARITY.getDesc(), String.valueOf(SexEnum.MALE.getCode()), popularityPrePage, popularityPrePageSize, popularityWithPage,
				popularityPageSize, cacheTime);
		
		Integer hotsellPrePage = Integer.parseInt(Global.getConfig("gyf.pre.hotsell.prePage"));
		Integer hotsellPrePageSize = Integer.parseInt(Global.getConfig("gyf.pre.hotsell.prePageSize"));
		Integer hotsellPageSize = Integer.parseInt(Global.getConfig("gyf.hotsell.pageSize"));
		Integer hotsellWithPage = Integer.parseInt(Global.getConfig("gyf.pre.hotsell.withPage"));
		
		cacheBook(BookCategoryEnum.HOT_SELL.getDesc(), String.valueOf(SexEnum.FEMALE.getCode()), hotsellPrePage, hotsellPrePageSize, hotsellWithPage,
				hotsellPageSize, cacheTime);
		cacheBook(BookCategoryEnum.HOT_SELL.getDesc(), String.valueOf(SexEnum.MALE.getCode()), hotsellPrePage, hotsellPrePageSize, hotsellWithPage,
				hotsellPageSize, cacheTime);
		
		Integer newbookPrePage = Integer.parseInt(Global.getConfig("gyf.pre.newbook.prePage"));
		Integer newbookPrePageSize = Integer.parseInt(Global.getConfig("gyf.pre.newbook.prePageSize"));
		Integer newbookPageSize = Integer.parseInt(Global.getConfig("gyf.newbook.pageSize"));
		Integer newbookWithPage = Integer.parseInt(Global.getConfig("gyf.pre.newbook.withPage"));
		
		cacheBook(BookCategoryEnum.NEW_BOOK.getDesc(), String.valueOf(SexEnum.FEMALE.getCode()), newbookPrePage, newbookPrePageSize, newbookWithPage,
				newbookPageSize, cacheTime);
		cacheBook(BookCategoryEnum.NEW_BOOK.getDesc(), String.valueOf(SexEnum.MALE.getCode()), newbookPrePage, newbookPrePageSize, newbookWithPage,
				newbookPageSize, cacheTime);
		long t4 = System.currentTimeMillis();
		logger.info("cache任务sussessful, 用时: {} 秒", (t4 - t3) / 1000);
	}

	// [1,2,3,4,5,6,7,8,9,10], 11, 12, 13...
	// 其中中括号内每一页对应 1页prePageSize条, 1页pageSize两种, 括号外每页pageSize
	// 随后对括号内随机排序, 按索引顺序缓存前prePage页每页prePageSize条的页, 剩余的按索引顺序缓存每页pageSize条的页
	// 括号外缓存每页pageSize条的页
	private void cacheBook(String category, String condition, Integer prePage, Integer prePageSize, Integer withPage, Integer pageSize, Integer cacheTime) {
		List<Integer> indexsTemp = Lists.newArrayList();
		List<Integer> indexsMoreTemp = Lists.newArrayList();
		Map<String, ServerResponse<List<Book>>> datasTemp = Maps.newConcurrentMap();
		
		Integer pageNo = 0;
		withPage++;
		
		ServerResponse<List<Book>> sersverResponse;
		String pre = JedisUtils.BOOK_PREFIX + condition + ":" + category + ":";
		boolean flag = true;
		
		do {
			pageNo++;
			
			if (pageNo < withPage) {
				// datasTemp中首页每页的每一页, 必须对应非首页的每页pageSize条, 否则不能参与排序(受前端分页限制, 非首页每页需要pageSize才进行查询下一页数据)
				// 查询非首页每页数据
				sersverResponse = list(category, condition, pageNo, pageSize);
				if (sersverResponse != null && sersverResponse.getData().size() - pageSize == 0) { //// notice!!
					indexsMoreTemp.add(pageNo);
					datasTemp.put(pageNo + "" + pageSize, sersverResponse);
					
					// 查询首页每页数据
					sersverResponse = list(category, condition, pageNo, prePageSize);
					if (sersverResponse != null && sersverResponse.getData().size() - prePageSize == 0) { //// notice!!
						indexsTemp.add(pageNo);
						datasTemp.put(pageNo + "" + prePageSize, sersverResponse);
						
					}else {
						// 当首页的某一页不足prePageSize时, 非首页当前页执行缓存操作, 之后从参与排序的算法中移除
						// 缓存
						flag = setBook2Cache(pre + pageNo + pageSize, datasTemp.get(pageNo + "" + pageSize), cacheTime);
						if (!flag) {
							pageNo--;
						}
						// 移除
						indexsMoreTemp.remove(pageNo);
						datasTemp.remove(pageNo + "" + pageSize);
					}
					
				} else {
					// 当非首页第一页就不足pageSize时, 执行缓存首页第一页
					if (pageNo == 1) {
						setBook2Cache(pre + pageNo + prePageSize, sersverResponse, cacheTime);
					}
					
					// 当非首页的某一页不足pageSize时, 执行缓存操作
					flag = setBook2Cache(pre + pageNo + pageSize, sersverResponse, cacheTime);
					if (!flag) {
						pageNo--;
					}
				}
				
			} else {
				// 缓存withPage之后的页, 非首页
				sersverResponse = list(category, condition, pageNo, pageSize);
				flag = setBook2Cache(pre + pageNo + pageSize, sersverResponse, cacheTime);
				if (!flag) {
					pageNo--;
				}
			}
			
			sersverResponse = null;
		} while (flag);
		
		/**
		 * [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
		 * [6, 3, 2, 0, 8, 1, 7, 5, 4, 9]
		 * [1, 7, 9, 4, 6, 0, 2, 5, 3, 8]
		 * ...
		 */
		Collections.shuffle(indexsTemp);	// 前withPage页顺序打乱
	
		int indexsMoreSize = indexsMoreTemp.size();
		int indexSize = indexsTemp.size();
		for(int i = 0; i < indexsMoreSize; i++) { 
			if (i < indexSize) {
				Integer pageNoTemp = indexsTemp.get(i);
				String keyPart = pageNoTemp + "" + pageSize;
				// 按索引顺序缓存前prePage页, 做首页的缓存
				if (i < prePage) { 
					// 前prePage页, 每页prePageSize条
					String preKeyPart = pageNoTemp + "" + prePageSize;
					setBook2Cache(pre + indexsMoreTemp.get(i) + prePageSize, datasTemp.get(preKeyPart), cacheTime);
					
					// 前prePage页, 每页pageSize条
					setBook2Cache(pre + indexsMoreTemp.get(i) + pageSize, datasTemp.get(keyPart), cacheTime);
					
					// 轮播
//					if (i == 0 && BookCategoryEnum.FINE.getDesc().equals(category)) {
//						List<Book> bookList = sersverResponse.getData();
//						if (bookList.size() > 3) 
//						for (int j = 0; j < 4; j++) {
//							Carousel carousel = new Carousel();
//							carousel.setId(j+1);
//							carousel.setUrl(bookList.get(j).getId());
//							carousel.setAlt(bookList.get(j).getName());
//							carousel.setRemark(bookList.get(j).getImageUrl());
//							carouselService.updateByPrimaryKeySelective(carousel);
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								logger.error("sleep error ...");
//								e.printStackTrace();
//							}
//							logger.info("任务sussessful : {}", i+1);
//						}
//					}
				// 剩余的prePage页到withPage页, 按索引顺序, 每页pageSize条
				} else {
					setBook2Cache(pre + indexsMoreTemp.get(i) + pageSize, datasTemp.get(keyPart), cacheTime);
				}
				
			}else {
				indexsMoreTemp.removeAll(indexsTemp);
				Integer pageNoTemp2 = indexsMoreTemp.get(i-indexSize);
				String keyPart2 = pageNoTemp2 + "" + pageSize;
				sersverResponse = datasTemp.get(keyPart2);
				setBook2Cache(pre + indexsMoreTemp.get(i) + pageSize, sersverResponse, cacheTime);
			}
			
		}
		
		indexsMoreTemp = null;
		indexsTemp = null;
		datasTemp = null;
	}
	
	
	private Boolean setBook2Cache(String key, ServerResponse<List<Book>> sersverResponse,  Integer cacheTime) {
		if (sersverResponse == null || !sersverResponse.isSuccess()) {
			return false;
		}
		List<Book> list = sersverResponse.getData();
		if (list == null || list.size() < 1) {
			return false;
		} 

		JedisUtils.setObject(key, sersverResponse, cacheTime);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	
	private ServerResponse<List<Book>> list(String category, String condition, Integer pageNo, Integer pageSize) {
		// 男频 or 女频
		Book book = new Book();
		if (String.valueOf(SexEnum.FEMALE.getCode()).equals(condition)) {
			book.setCondition(SexEnum.FEMALE.getCode());
		} else if (String.valueOf(SexEnum.MALE.getCode()).equals(condition)) {
			book.setCondition(SexEnum.MALE.getCode());
		} else {
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		Page<Book> page = new Page<Book>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setCycle(false);

		// 精品推荐
		if (BookCategoryEnum.FINE.getDesc().equals(category)) {
			// 按照精品条件筛选(一周内充值排行)
			page = bookService.findPageByFine(page, book);
		}
		// 人气榜 书籍列表 (根据点击数)
		else if (BookCategoryEnum.POPULARITY.getDesc().equals(category)) {
			page = bookService.findPageByPopularity(page, book);
		}
		// 畅销榜 书籍列表 (根据每本书购买的人数)
		else if (BookCategoryEnum.HOT_SELL.getDesc().equals(category)) {
			page = bookService.findPageByHotsell(page, book);
		}
		// 新书专区 书籍列表 (根据上传时间距离当前日期小于30天)
		else if (BookCategoryEnum.NEW_BOOK.getDesc().equals(category)) {
			page = bookService.findPageByNewbook(page, book);
		} else {
			return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return ServerResponse.createBySuccess(page.getList());
	}

}
