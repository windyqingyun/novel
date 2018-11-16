package com.jeeplus.modules.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.entity.Carousel;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.service.CarouselService;

@Service
@EnableScheduling
@Lazy(false)
public class SelectFineBookTask {
	
	private Logger logger = LoggerFactory.getLogger(SelectFineBookTask.class);
	
	@Autowired
	public BookService bookService;
	
	@Autowired
	public CarouselService carouselService;
	

	/**
	 * 轮播图定时任务
	 */
	@Scheduled(cron = "0 30 1 ? * 2")
	public void selectFineBookTask() {
		logger.info("任务start");
		long t1 = System.currentTimeMillis();
		Book book = new Book();
		Page<Book> page = new Page<Book>();
		page.setPageNo(1);
		page.setPageSize(4);
		page.setCycle(false);
		page = bookService.findPageByFine(page, book);
		List<Book> list = page.getList();
		if (list.size() > 3) {
			for (int i = 0; i < 4; i++) {
				Carousel carousel = new Carousel();
				carousel.setId(i+1);
				carousel.setUrl(list.get(i).getId());
				carousel.setAlt(list.get(i).getName());
				carousel.setRemark(list.get(i).getImageUrl());
				carouselService.updateByPrimaryKeySelective(carousel);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error("sleep error ...");
					e.printStackTrace();
				}
				logger.info("任务sussessful : {}", i+1);
			}
			long t2 = System.currentTimeMillis();
			logger.info("任务sussessful, 用时: {} 秒", (t2-t1)/1000);
		} else {
			logger.info("任务未执行");
		}
	}
	
}
