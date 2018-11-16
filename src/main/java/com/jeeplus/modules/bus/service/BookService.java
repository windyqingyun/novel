/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.response.ResponseCode;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.dao.BookDao;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.entity.Customer;
import com.jeeplus.modules.bus.laikan.service.LaikanChapterHisService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;
import com.jeeplus.modules.bus.utils.JsonFieldConst;
import com.jeeplus.modules.bus.vo.BookChapterVo;
import com.jeeplus.modules.bus.vo.BookVo;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 书籍Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class BookService extends CrudService<BookDao, Book> {

	@Autowired
	private BookChapterService bookChapterService;
	@Autowired
	private LaikanChapterHisService laikanChapterHisService;
	
	public Book get(String id) {
		return super.get(id);
	}
	
	public List<Book> findList(Book book) {
		return super.findList(book);
	}
	
	public Page<Book> findPage(Page<Book> page, Book book) {
		return super.findPage(page, book);
	}
	
	@Transactional
	public void save(Book book) {
		super.save(book);
	}
	
	@Transactional(readOnly = false)
	public void delete(Book book) {
		super.delete(book);
	}

	
	/**
	 * 逻辑删除
	 * @param book
	 */
	@Transactional(readOnly = false)
	public void deleteByLogic(Book book) {
		dao.deleteByLogic(book);
	}
	
	@Transactional(readOnly = false)
	public void addClickCount(String id){
		dao.addClickCount(id);
	}
	
	/**
	 * 获取小说所属机构
	 * @param id
	 * @return
	 */
	public String getBookOfficeId(String id){
		return dao.getBookOfficeId(id);
	}
	
	
	@Transactional(readOnly = false)
	public void saveBooksFromInterface(JSONObject jsonObj) throws Exception{
		Office reqOffice = new Office(jsonObj.getString(JsonFieldConst.OFFICE_ID));
		List<Book> bookList = parseJsonToBookList(jsonObj.getJSONObject(JsonFieldConst.DATA).getString("bookList"));
		
		if(bookList != null && !bookList.isEmpty()) {
			for (Book book : bookList) {
				logger.info("保存小说:"+book.getName()+",所属小说站:"+reqOffice.getId());
				saveBookAndChapter(book, reqOffice);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void saveBookAndChapter(Book book, Office office) throws Exception{
		List<BookChapter> chapterList = book.getChapterList();

		Date now = new Date();
		book.setUpdateDate(now);
		book.setOffice(office);
		book.setOriginalId(book.getId());  //设置原id为传过来的id
		book.setId(null);                  //设置id为空
		
		//获取小说的id 有可能为空
		String id = existsBookAndrstId(book.getOffice().getId(), book.getOriginalId());
		
		//如果该小说已经存在，则做修改操作，否则做添加操作
		if(StringUtils.isNotBlank(id)){
			Book t = get(id);//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(book, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			book.setId(id);
			
			dao.update(t);
		}else {
			book.setId(IdGen.uuid());
			book.setCreateDate(now);
			dao.insert(book);
		}
		
		if(chapterList != null && !chapterList.isEmpty()){
			for (BookChapter bookChapter : chapterList) {
				bookChapter.setBookId(book.getId());
				
				//如果是来看阅读，并且originalId不是空，修改来看历史记录的对应关系
				if(office.getId().equals("100000002007") && StringUtils.isNotBlank(bookChapter.getOriginalId())){
					laikanChapterHisService.updateBookIdByChapterId(bookChapter.getOriginalId(), book.getId(), bookChapter.getChapter());
				}
				
				bookChapterService.saveChapter(bookChapter);
			}
		}
	}
	
	/**
	 * 根据officeId和原始Id判断小说是否存在，存在的话返回它的id
	 * @param officeId
	 * @param originalId
	 * @return
	 */
	public String existsBookAndrstId(String officeId, String originalId){
		return dao.existsBookAndrstId(officeId, originalId);
	}
	
	public List<Book> parseJsonToBookList(String json){
		JsonMapper jsonMapper = JsonMapper.getInstance();
		JavaType bookListType = jsonMapper.createCollectionType(List.class, Book.class);
		
		List<Book> bookList = jsonMapper.fromJson(json, bookListType);
		
		return bookList;
	}
	
	/**
	 *  精品: 最新充值总额排行榜
	 * @return
	 */
	public Page<Book> findPageByFine(Page<Book> page, Book book) {
		book.setPage(page);
		page.setList(dao.findPageByFine(book));
		return page;
	}
	
	
	/**
	 * 根据 点击量 查询人气榜
	 * @param page
	 * @param book
	 * @return
	 */
	public Page<Book> findPageByPopularity(Page<Book> page, Book book) {
		book.setPage(page);
		page.setList(dao.findPageByPopularity(book));
		return page;
	}
	
	
	/**
	 * 根据 购买记录 书籍销售 排行 (根据每本书购买的人数的多少)
	 * @param page
	 * @param book
	 * @return
	 */
	public Page<Book> findPageByHotsell(Page<Book> page, Book book) {
		book.setPage(page);
		page.setList(dao.findPageByHotsell(book));
		return page;
	}
	
	
	/**
	 * 根据上传时间距离当前日期小于30天
	 * @param page
	 * @param book
	 * @return
	 */
	public Page<Book> findPageByNewbook(Page<Book> page, Book book) {
		book.setPage(page);
		page.setList(dao.findPageByNewbook(book));
		return page;
	}
	
	/**
	 * 获取书籍详情
	 */
	public ServerResponse<BookVo> getBookDetail(HttpServletRequest request, String bookId) {
		// 校验
		Book book = get(bookId);
		if (book == null) {
			return ServerResponse.createByError("该小说暂时没找到");
		}
			
		// 最后一个章节
		BookVo bookVo = new BookVo();
		BookChapterVo bookChapterVo = new BookChapterVo();
		try {
			BeanUtils.copyProperties(bookVo, book);
			BookChapter lastBookChapter = bookChapterService.getLastBookChapterByBookId(book.getId());
			BeanUtils.copyProperties(bookChapterVo, lastBookChapter);
		} catch (Exception e) {
			logger.error("属性copy异常", e.getMessage());
			return ServerResponse.createByError(ResponseCode.SYS_ERROR.getCode(), ResponseCode.SYS_ERROR.getDesc());
		} 
		
		// 用户最后阅读章节
		BookChapterVo lastReadChapterVo = new BookChapterVo();
		Customer currentCustomer = CurrentCustomerUtil.getCurrentCustomer(request);
		if (currentCustomer != null && currentCustomer.getId() > 0) {
			BookChapter lastReadChapter = bookChapterService.getLastReadChapterByBookIdAndCustomerId(book.getId(), String.valueOf(currentCustomer.getId()));
			if (lastReadChapter != null) {
				try {
					BeanUtils.copyProperties(lastReadChapterVo, lastReadChapter);
				} catch (Exception e) {
					logger.error("属性copy异常", e.getMessage());
					return ServerResponse.createByError(ResponseCode.SYS_ERROR.getCode(), ResponseCode.SYS_ERROR.getDesc());

				}
			}
		}
		
		bookVo.setLastReadBookChapter(lastReadChapterVo);
		bookVo.setLastBookChapter(bookChapterVo);
		bookVo.setChapterTotalCount(bookChapterService.selectBookChapterCountByBookId(book.getId()));
		return ServerResponse.createBySuccess(bookVo);
	}
	
	/**
	 * 猜你喜欢
	 */
	public Page<Book> recommend(Page<Book> page, Book book) {
		book.setPage(page);
		page.setList(dao.recommend(book));
		return page;
	}
	
	/**
	 * 根据id 更新customviewcount
	 * @param book
	 * @return
	 */
	@Transactional(readOnly = false)
	public int updateCustomviewcount(Book book) {
		return dao.updateCustomviewcount(book);
	}
	
}