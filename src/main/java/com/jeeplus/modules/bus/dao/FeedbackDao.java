package com.jeeplus.modules.bus.dao;

import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.Feedback;

@MyBatisDao
public interface FeedbackDao {

    void insert(Feedback feedback);
}
