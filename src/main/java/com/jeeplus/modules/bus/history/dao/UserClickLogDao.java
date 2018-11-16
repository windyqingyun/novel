package com.jeeplus.modules.bus.history.dao;

import com.jeeplus.modules.bus.history.entity.example.UserClickLogExample;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.history.entity.UserClickLog;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface UserClickLogDao {
    int countByExample(UserClickLogExample example);

    int deleteByExample(UserClickLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserClickLog record);

    int insertSelective(UserClickLog record);

    List<UserClickLog> selectByExample(UserClickLogExample example);

    UserClickLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserClickLog record, @Param("example") UserClickLogExample example);

    int updateByExample(@Param("record") UserClickLog record, @Param("example") UserClickLogExample example);

    int updateByPrimaryKeySelective(UserClickLog record);

    int updateByPrimaryKey(UserClickLog record);
}