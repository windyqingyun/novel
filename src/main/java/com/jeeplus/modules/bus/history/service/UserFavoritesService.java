/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.ListUtils;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.entity.Fodder;
import com.jeeplus.modules.bus.history.dao.UserFavoritesDao;
import com.jeeplus.modules.bus.history.entity.UserFavorites;
import com.jeeplus.modules.bus.service.FodderService;
import com.jeeplus.modules.bus.utils.JsonFieldConst;
import com.jeeplus.modules.sys.entity.User;

/**
 * 用户收藏记录Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserFavoritesService extends CrudService<UserFavoritesDao, UserFavorites> {

	@Autowired
	private FodderService fodderService;
	
	public UserFavorites get(String id) {
		return super.get(id);
	}
	
	public List<UserFavorites> findList(UserFavorites userFavorites) {
		return super.findList(userFavorites);
	}
	
	public Page<UserFavorites> findPage(Page<UserFavorites> page, UserFavorites userFavorites) {
		return super.findPage(page, userFavorites);
	}
	
	@Transactional(readOnly = false)
	public void save(UserFavorites userFavorites) {
		super.save(userFavorites);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserFavorites userFavorites) {
		super.delete(userFavorites);
	}
	
	@Transactional(readOnly = false)
	public void deleteByIds(String[] ids) {
		for (String id : ids) {
			delete(new UserFavorites(id));
		}
	}
	
	@Transactional(readOnly = false)
	public String insert(String userId, String fodderId){
		UserFavorites userFavorites = new UserFavorites();
		User createUser = new User(userId);
		Date nowDate = new Date();
		userFavorites.setCreateBy(createUser);
		userFavorites.setUpdateBy(createUser);
		userFavorites.setCreateDate(nowDate);
		userFavorites.setUpdateDate(nowDate);
		
		userFavorites.setFodderId(fodderId);
		userFavorites.setId(IdGen.uuid());
		
		dao.insert(userFavorites);
		
		return ServiceUtil.getMessage(RespCodeEnum.U0, (Object)userFavorites.getId());
	}
	
	/**
	 * 判断是否存在某个收藏
	 * @param userId
	 * @param fodderId
	 * @return
	 */
	public UserFavorites existsFavorite(String userId, String fodderId){
		UserFavorites userFavorites = new UserFavorites();
		userFavorites.setCreateBy(new User(userId));
		userFavorites.setFodderId(fodderId);
		
		List<UserFavorites> list = findList(userFavorites);
		if(ListUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	public String findList(String userId, String officeId){
		Map resultMap = Maps.newHashMap();
		
		if(StringUtils.isNotBlank(userId)){
			UserFavorites userFavorites = new UserFavorites();
			userFavorites.setCreateBy(new User(userId));
			//如果officeId不为空，需要按照来源机构筛选收藏的素材
			if(StringUtils.isNotBlank(officeId)){
				userFavorites.setOfficeId(officeId);
			}
			
			List<UserFavorites> list = findList(userFavorites);
	
			List<Map> favoritesMapList = Lists.newArrayList();
			if(ListUtils.isNotEmpty(list)){
				for (UserFavorites favorite : list) {
					favoritesMapList.add(convertFavoriteToMap(favorite));
				}
			}
			resultMap.put(JsonFieldConst.LIST, favoritesMapList);
		}else{
			resultMap.put(JsonFieldConst.LIST, null);
		}
		
		return ServiceUtil.getMessage(RespCodeEnum.U0, resultMap);
	}
	
	public Map convertFavoriteToMap(UserFavorites userFavorite) {
		Map userFavoriteMap = Maps.newHashMap();
		Fodder fodder = userFavorite.getFodder();
		
		userFavoriteMap.put("id", userFavorite.getId());
		userFavoriteMap.put("fodder.id", userFavorite.getFodderId());
		userFavoriteMap.put("fodder.title", fodder.getTitle());
		userFavoriteMap.put("fodder.titleImageUrl", Global.downloadArchiveURL+"?url="+fodder.getTitleImage());
		userFavoriteMap.put("fodder.viewCount", fodder.getViewcount());
		userFavoriteMap.put("fodder.officeName", fodder.getOffice().getName());
		userFavoriteMap.put("createDate", DateUtils.formatDate(userFavorite.getCreateDate(), "yyyy.MM.dd HH:mm"));
		
		return userFavoriteMap;
	}
}