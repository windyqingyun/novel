package com.jeeplus.modules.bus.dao;

import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.BookUser;

@MyBatisDao
public interface BookUserDao {


    int insert(BookUser record);

    BookUser selectByPrimaryKey(String id);

    int update(BookUser record);

    BookUser findUserByUnionid(String unionid);
}
