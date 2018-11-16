package com.jeeplus.modules.bus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.CustomerDetail;
import com.jeeplus.modules.bus.entity.example.CustomerDetailExample;

@MyBatisDao
public interface CustomerDetailDao {
    int countByExample(CustomerDetailExample example);

    int deleteByExample(CustomerDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CustomerDetail record);

    int insertSelective(CustomerDetail record);

    List<CustomerDetail> selectByExample(CustomerDetailExample example);

    CustomerDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CustomerDetail record, @Param("example") CustomerDetailExample example);

    int updateByExample(@Param("record") CustomerDetail record, @Param("example") CustomerDetailExample example);

    int updateByPrimaryKeySelective(CustomerDetail record);

    int updateByPrimaryKey(CustomerDetail record);
}