/**
 * 
 */
package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.bus.dao.CustomerDetailDao;
import com.jeeplus.modules.bus.entity.CustomerDetail;
import com.jeeplus.modules.bus.entity.example.CustomerDetailExample;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月18日
 * @version V1.0
 *
 */
@Service
@Transactional
public class CustomerDetailService {
	@Autowired
	private CustomerDetailDao customerDetailDao;
	
	
	public int countByExample(CustomerDetailExample example) {
		return customerDetailDao.countByExample(example);
	}

	
	public int deleteByExample(CustomerDetailExample example) {
		return customerDetailDao.deleteByExample(example);
	}

	
	public int deleteByPrimaryKey(Integer id) {
		return customerDetailDao.deleteByPrimaryKey(id);
	}

	
	public int insert(CustomerDetail record) {
		return customerDetailDao.insert(record);
	}

	
	public int insertSelective(CustomerDetail record) {
		return customerDetailDao.insertSelective(record);
	}

	
	public List<CustomerDetail> selectByExample(CustomerDetailExample example) {
		return customerDetailDao.selectByExample(example);
	}

	
	public CustomerDetail selectByPrimaryKey(Integer id) {
		return customerDetailDao.selectByPrimaryKey(id);
	}

	
	public int updateByExampleSelective(CustomerDetail record, CustomerDetailExample example) {
		return customerDetailDao.updateByExampleSelective(record, example);
	}

	
	public int updateByExample(CustomerDetail record, CustomerDetailExample example) {
		return customerDetailDao.updateByExample(record, example);
	}

	
	public int updateByPrimaryKeySelective(CustomerDetail record) {
		return customerDetailDao.updateByPrimaryKeySelective(record);
	}

	
	public int updateByPrimaryKey(CustomerDetail record) {
		return customerDetailDao.updateByPrimaryKey(record);
	}
 
}
