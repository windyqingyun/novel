package com.jeeplus.modules.bus.dao;

import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.Customer;
import com.jeeplus.modules.bus.entity.example.CustomerExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface CustomerDao {
    int countByExample(CustomerExample example);

    int deleteByExample(CustomerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Customer record);

    int insertSelective(Customer record);

    List<Customer> selectByExample(CustomerExample example);

    Customer selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Customer record, @Param("example") CustomerExample example);

    int updateByExample(@Param("record") Customer record, @Param("example") CustomerExample example);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);
    
    
    /** 根据openid查询用户是否已绑定 */
    int isExitsCustomerByOpenidWithOpenidType(@Param("openid") String openid, @Param("openidType") Integer openidType);
    
    /** 根据openid查询用户 */
    Customer selectByOpenidWithOpenidType(@Param("openid") String openid, @Param("openidType") Integer openidType);

}