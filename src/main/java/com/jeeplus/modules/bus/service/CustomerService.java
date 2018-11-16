/**
 * 
 */
package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.bus.dao.CustomerDao;
import com.jeeplus.modules.bus.entity.Customer;
import com.jeeplus.modules.bus.entity.example.CustomerExample;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月18日
 * @version V1.0
 *
 */
@Service
@Transactional
public class CustomerService {
	@Autowired
	private CustomerDao customerDao;

	
	public int countByExample(CustomerExample example) {
		return customerDao.countByExample(example);
	}

	
	public int deleteByExample(CustomerExample example) {
		return customerDao.deleteByExample(example);
	}

	
	public int deleteByPrimaryKey(Integer id) {
		return customerDao.deleteByPrimaryKey(id);
	}

	
	public int insert(Customer record) {
		return customerDao.insert(record);
	}

	
	public int insertSelective(Customer record) {
		return customerDao.insertSelective(record);
	}

	
	public List<Customer> selectByExample(CustomerExample example) {
		return customerDao.selectByExample(example);
	}

	
	public Customer selectByPrimaryKey(Integer id) {
		return customerDao.selectByPrimaryKey(id);
	}

	
	public int updateByExampleSelective(Customer record, CustomerExample example) {
		return customerDao.updateByExampleSelective(record, example);
	}

	
	public int updateByExample(Customer record, CustomerExample example) {
		return customerDao.updateByExample(record, example);
	}

	
	public int updateByPrimaryKeySelective(Customer record) {
		return customerDao.updateByPrimaryKeySelective(record);
	}

	
	public int updateByPrimaryKey(Customer record) {
		return customerDao.updateByPrimaryKey(record);
	}
 
	
}
