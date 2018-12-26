package com.jeeplus.modules.bus.dao;

import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.SearchLog;

import java.util.List;

/**
 * 搜索记录接口
 */
@MyBatisDao
public interface SearchLogDao{

    void insert(SearchLog searchLog);

    void updateByKey(SearchLog searchLog);

    List<SearchLog> findList(SearchLog searchLog);

    void isExisLog(SearchLog searchLog);
}
