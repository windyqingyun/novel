package com.jeeplus.modules.bus.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.filter.encoding.CharsetParameter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.cache.GuavaCache;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.constant.Constants;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.dao.BookChapterDao;
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.entity.Customer;
import com.jeeplus.modules.bus.entity.UserWallet;
import com.jeeplus.modules.bus.enums.BookChapterEnum;
import com.jeeplus.modules.bus.history.service.UserBuychapterHistoryService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;
import com.jeeplus.modules.bus.utils.JsonFieldConst;
import com.jeeplus.modules.bus.vo.BookChapterVo;

/**
 * 小说章节内容Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class BookChapterService extends CrudService<BookChapterDao, BookChapter> {

	@Autowired
	private UserBuychapterHistoryService userBuychapterHistoryService;
	
	@Autowired
	private OfficeCoinConfigService officeCoinConfigService;
	
	@Autowired
	private UserWalletService userWalletService;
	
 
	public BookChapter get(String id) {
		return super.get(id);
	}
	
	public List<BookChapter> findList(BookChapter bookChapter) {
		return super.findList(bookChapter);
	}
	
	public Page<BookChapter> findPage(Page<BookChapter> page, BookChapter bookChapter) {
		return super.findPage(page, bookChapter);
	}
	
	@Transactional(readOnly = false)
	public void save(BookChapter bookChapter) {
		super.save(bookChapter);
	}
	
	@Transactional(readOnly = false)
	public void delete(BookChapter bookChapter) {
		super.delete(bookChapter);
	}
	
	/**
	 * 逻辑删除
	 * @param bookChapter
	 */
	@Transactional(readOnly = false)
	public void deleteByLogic(BookChapter bookChapter) {
		dao.deleteByLogic(bookChapter);
	}
	
	/**
	 * 根据officeId和原始Id判断小说章节是否存在，存在的话返回它的id
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	public String exsitsChapterAndrstId(String bookId, int chapter){
		return dao.exsitsChapterAndrstId(bookId, chapter);
	}
	
	@Transactional(readOnly = false)
	public void saveChapter(BookChapter bookChapter) {
		Date now = new Date();
		bookChapter.setUpdateDate(now);
		
		//获取小说的id 有可能为空
		String id = exsitsChapterAndrstId(bookChapter.getBookId(), bookChapter.getChapter());
		
		//如果该章节已经存在，则做修改操作，否则做添加操作
		if(StringUtils.isNotBlank(id)){
			bookChapter.setId(id);
			dao.update(bookChapter);
		}else {
			bookChapter.setId(IdGen.uuid());
			bookChapter.setCreateDate(now);
			dao.insert(bookChapter);
		}
	}
	
	
	/**
	 * 分页查询，返回json数据
	 * @param para
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String findPage(int pageNo, int pageSize, String bookId, String userId) {
		//构建查询的参数
		BookChapter bookChapter = new BookChapter();
		bookChapter.setBookId(bookId);
		bookChapter.setUserId(userId);
		
		//构建返回值
		Page<BookChapter> page = new Page<BookChapter>(pageNo, pageSize);
		Page<BookChapter> bookChapterPage = findPage(page, bookChapter);
		Map resultDataMap = Maps.newHashMap();
		resultDataMap.put(JsonFieldConst.PAGE_COUNT, bookChapterPage.getLast());
		
		List<Map> bookChapterMapList = Lists.newArrayList();
		if(bookChapterPage.getList() != null && !bookChapterPage.getList().isEmpty()) {
			for (BookChapter chapter : bookChapterPage.getList()) {
				bookChapterMapList.add(convertBookChapterToMap(chapter));
			}
			
			//放置小说阅读币后缀名称
			String coinName = officeCoinConfigService.getCoinNameByOfficeId(bookChapterPage.getList().get(0).getOffice().getId());
			resultDataMap.put("coinName", StringUtils.isNotBlank(coinName) ? coinName : "阅读币");
		}
		resultDataMap.put(JsonFieldConst.LIST, bookChapterMapList);
		
		
		
		return ServiceUtil.getMessage(RespCodeEnum.U0, resultDataMap);
	}
	
	/**
	 * 获取小说章节的详情
	 * @param para
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false)
	public String getBookChapterInfo(HttpServletRequest request, String bookId, Integer chapter, String userId) {
		//根据bookId和章节获取小说章节详情
		BookChapter bookChapter = getByChapterAndBookId(new BookChapter(bookId, chapter));
		
		Map resultDataMap = Maps.newHashMap();
		
		resultDataMap.put("id", bookChapter.getId());
		resultDataMap.put("title", bookChapter.getTitle());
		resultDataMap.put("prevChapter", bookChapter.getPrevChapter());
		resultDataMap.put("nextChapter", bookChapter.getNextChapter());
		resultDataMap.put("officeId", bookChapter.getOffice().getId());

		//如果是vip章节
		if(bookChapter.getIsvip().equals(Global.YES)) {
			userId = CurrentCustomerUtil.getCurrentCustomerId(request);
			if(!StringUtils.isBlank(userId)) {
				if(!userBuychapterHistoryService.isUserBuyBookChapter(userId, bookChapter.getBookId(), bookChapter.getChapter())) {
					//获取用户余额
					UserWallet userWallet = userWalletService.getUserWalletByOfficeId(userId, bookChapter.getOffice().getId());
					
					//如果未购买  返回小说章节价格
					putIsCanViewFlagAndMessage(resultDataMap, false, "请先购买");
					resultDataMap.put("price", bookChapter.getPrice());
					resultDataMap.put("remainCoin", userWallet.getCoin());  //剩余阅读币
					resultDataMap.put("prevChapter", bookChapter.getPrevChapter());
					resultDataMap.put("nextChapter", bookChapter.getNextChapter());
					
					//获取小说阅读币名称
					String coinName = officeCoinConfigService.getCoinNameByOfficeId(bookChapter.getOffice().getId());
					resultDataMap.put("coinName", StringUtils.isNotBlank(coinName) ? coinName : "阅读币");
					
					return ServiceUtil.getMessage(RespCodeEnum.U0, resultDataMap);
				}
			}else {
//				//如果未购买  返回小说章节价格
//				putIsCanViewFlagAndMessage(resultDataMap, false, "请先购买");
//				resultDataMap.put("price", bookChapter.getPrice());
//				resultDataMap.put("remainCoin", 0);  //剩余阅读币
//				resultDataMap.put("prevChapter", bookChapter.getPrevChapter());
//				resultDataMap.put("nextChapter", bookChapter.getNextChapter());
//				
//				//获取小说阅读币名称
//				String coinName = officeCoinConfigService.getCoinNameByOfficeId(bookChapter.getOffice().getId());
//				resultDataMap.put("coinName", StringUtils.isNotBlank(coinName) ? coinName : "阅读币");
//				
//				return ServiceUtil.getMessage(RespCodeEnum.U0, resultDataMap);
				return ServiceUtil.getMessage(RespCodeEnum.U6, "请先登录");
			}
		}
		
		putIsCanViewFlagAndMessage(resultDataMap, true, "");
		resultDataMap.put("content", bookChapter.getContent());
		return ServiceUtil.getMessage(RespCodeEnum.U0, resultDataMap);
	}
	
	/**
	 * 根据bookId和chapter获取章节详情
	 * @param bookChapter
	 * @return
	 */
	public BookChapter getByChapterAndBookId(BookChapter bookChapter) {
		if(StringUtils.isBlank(bookChapter.getBookId())) {
			throw new ServiceException(RespCodeEnum.U4, new NullPointerException("bookId不能为空"));
		}
		
		return dao.getByChapterAndBookId(bookChapter);
	}
	
	/**
	 * 根据bookId和chapter获取小说价格
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	public BigDecimal getPriceBuyChapterAndBookId(String bookId, Integer chapter){
		return dao.getPriceBuyChapterAndBookId(bookId, chapter);
	}
	
	/**
	 * 为map防止是否可以阅读的标识、消息
	 * @param dataMap
	 * @param isCanView
	 * @param message
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void putIsCanViewFlagAndMessage(Map dataMap, boolean isCanView, String message) {
		dataMap.put("isCanView", isCanView);
		dataMap.put(JsonFieldConst.MESSAGE, message);
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public Map convertBookChapterToMap(BookChapter bookChapter){
		Map map = Maps.newHashMap();
		map.put("id", bookChapter.getId());
		map.put("title", bookChapter.getTitle());
		map.put("bookId", bookChapter.getBookId());
		map.put("chapter", bookChapter.getChapter());
		map.put("isBuy", bookChapter.getIsBuy());
		map.put("isVip", Global.YES.equals(bookChapter.getIsvip()));
		map.put("price", bookChapter.getPrice());
		
		return map;
	}

	
	public Integer getMaxChapterOfBook(String bookId){
		return dao.getMaxChapterOfBook(bookId);
	}
	
	public Integer getMinChapterOfBook(String bookId){
		return dao.getMinChapterOfBook(bookId);
	}
	
	
	/**
	 * 获取小说最后一个章节 (小说详情页需要最后的更新时间)
	 * @return
	 */
	public BookChapter getLastBookChapterByBookId(String bookId) {
		return dao.getLastBookChapterByBookId(bookId);
	}
	
	/**
	 * 用户最后阅读章节
	 */
	public BookChapter getLastReadChapterByBookIdAndCustomerId(String bookId, String customerId) {
		return dao.getLastReadChapterByBookIdAndCustomerId(bookId, customerId);
	}
	
	/**
	 * 根据小说获取章节总数
	 * @param bookId
	 * @return
	 */
	public Integer selectBookChapterCountByBookId(String bookId) {
		return dao.selectBookChapterCountByBookId(bookId);
	}
	
	/**
	 * 获取用户的购买章节
	 */
	public List<Integer> findBookChapterListByCustomerId(String customerId, String bookId) {
		return userBuychapterHistoryService.selectByBookIdAndCustomerId(customerId, bookId);
	}
 
 
	private Map assembleMap(Page page, BookChapter bookChapter, String token) {
		List<Integer> chapterList = new ArrayList<>();
		if (!StringUtils.isBlank(token)) {
			String currentCustomerId = CurrentCustomerUtil.getCurrentCustomerId(token);
			if (currentCustomerId != null) {
				chapterList = findBookChapterListByCustomerId(currentCustomerId, bookChapter.getBookId());
			}
		}
		
		Map map = Maps.newConcurrentMap();
		List<BookChapterVo> list = Lists.newLinkedList(); 
		ListIterator<BookChapter> iter = page.getList().listIterator();
		while(iter.hasNext()) {
			BookChapter next = iter.next();
			BookChapterVo vo = new BookChapterVo();
			
			vo.setTitle(next.getTitle());
			// 是否免费
			String isvip = next.getIsvip();
			vo.setIsvip(isvip);
//			if (StringUtils.equals(String.valueOf(BookChapterEnum.IS_FREE.getCode()), vo.getIsvip())) {
//				vo.setId(next.getId());
//				vo.setBookId(next.getBookId());
//				vo.setChapter(next.getChapter());
//			} 
//			vo.setId(next.getId());
			vo.setBookId(next.getBookId());
			vo.setChapter(next.getChapter());
			// 登陆判断 是否已购买
			vo.setIsBuy(BookChapterEnum.IS_NOT_BUY.getDesc());
			for (Integer chapter : chapterList) {
				if (chapter - vo.getChapter() == 0) {
					vo.setIsBuy(BookChapterEnum.IS_BUY.getDesc());
					break;
				}
			}
			list.add(vo);
		}
		map.put("list", list);
		map.put("totalBookChapter", page.getCount());
		return map;
	};
	
	/**
	 * 小说章节列表
	 * @param page
	 * @param bookChapter
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ServerResponse findBookChapterPageByBookIdAndUserId(Page page, BookChapter bookChapter, String token) {
		page = super.findPage(page, bookChapter);
		Map assembleMap = assembleMap(page, bookChapter, token);
		return ServerResponse.createBySuccess(assembleMap);
	}
	
	
	public ServerResponse findBookChapterContentUserId(BookChapter bookChapter, String token) {
		BookChapterVo bookChaperWithPreNext = dao.findBookChapterContent(bookChapter);
		if (bookChaperWithPreNext != null) {
			// 1. 判断是否是免费
			if (StringUtils.equals(String.valueOf(BookChapterEnum.IS_NOT_FREE.getCode()), bookChaperWithPreNext.getIsvip())) {
				// 2. 判断用户登陆
				Customer currentCustomer = CurrentCustomerUtil.getCurrentCustomer(token);
				if (currentCustomer != null){
					Integer customerId = currentCustomer.getId();
//					String id = bookChapter.getId();
					String bookId = bookChapter.getBookId();
					Integer chapter = bookChapter.getChapter();
					// todo 3. 判断是否购买
				} else {
					return ServerResponse.createByError("请先购买");
				}
			}
			return ServerResponse.createBySuccess(bookChaperWithPreNext);
		}
		return ServerResponse.createByError("该章节内容没有找到");
	}

	
}