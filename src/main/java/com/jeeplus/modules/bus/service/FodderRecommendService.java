/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.modules.bus.dao.FodderRecommendDao;
import com.jeeplus.modules.bus.entity.FodderRecommend;

/**
 * 推荐素材列表Service
 * @author zhangsc
 * @version 2018-03-14
 */
@Service
@Transactional(readOnly = true)
public class FodderRecommendService extends CrudService<FodderRecommendDao, FodderRecommend> {

	public FodderRecommend get(String id) {
		return super.get(id);
	}
	
	public List<FodderRecommend> findList(FodderRecommend fodderRecommend) {
		return super.findList(fodderRecommend);
	}
	
	public Page<FodderRecommend> findPage(Page<FodderRecommend> page, FodderRecommend fodderRecommend) {
		return super.findPage(page, fodderRecommend);
	}
	
	@Transactional(readOnly = false)
	public void save(FodderRecommend fodderRecommend) {
		fodderRecommend.setUpdateDate(new Date());
		fodderRecommend.setCreateDate(fodderRecommend.getUpdateDate());
		dao.insert(fodderRecommend);
		//super.save(fodderRecommend);
	}
	
	@Transactional(readOnly = false)
	public void delete(FodderRecommend fodderRecommend) {
		super.delete(fodderRecommend);
	}
	
	public int getMaxGroupNum(){
		Integer num = dao.getMaxGroupNum();
		if(num != null){
			return num;
		}
		return 0;
	}
	
	/**
	 * 转换fodder为map  map不包含素材内容，是用于list查看的
	 * @param fodder
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map convertFodderToMap(FodderRecommend fodderRecommend, String officeId) {
		Map fodderMap = Maps.newHashMap();
		fodderMap.put("id", fodderRecommend.getId());
		fodderMap.put("title", fodderRecommend.getFodder().getTitle());
		fodderMap.put("titleImageUrl", Global.downloadArchiveURL+"?url="+fodderRecommend.getFodder().getTitleImage());
		fodderMap.put("viewCount", fodderRecommend.getFodder().getViewcount());
		fodderMap.put("officeName", fodderRecommend.getOffice().getName());
		fodderMap.put("href", "http://www.content.vip/system/html/materialPage.html?fodderId="+fodderRecommend.getFodder().getId()+"&officeId="+officeId);
		fodderMap.put("createDate", DateUtils.formatDate(fodderRecommend.getFodder().getCreateDate(), "yyyy-MM-dd"));
		
		return fodderMap;
	}
	
	
}