/**
 * 
 */
package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.bus.dao.CarouselDao;
import com.jeeplus.modules.bus.entity.Carousel;
import com.jeeplus.modules.bus.entity.example.CarouselExample;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月20日
 * @version V1.0
 *
 */
@Service
@Transactional
public class CarouselService {
	@Autowired
	private CarouselDao carouselDao;
	
	
	public int countByExample(CarouselExample example) {
		return carouselDao.countByExample(example);
	}

	
	public int deleteByExample(CarouselExample example) {
		return carouselDao.deleteByExample(example);
	}

	
	public int deleteByPrimaryKey(Integer id) {
		return carouselDao.deleteByPrimaryKey(id);
	}

	
	public int insert(Carousel record) {
		return carouselDao.insert(record);
	}

	
	public int insertSelective(Carousel record) {
		return carouselDao.insertSelective(record);
	}

	
	public List<Carousel> selectByExample(CarouselExample example) {
		return carouselDao.selectByExample(example);
	}

	
	public Carousel selectByPrimaryKey(Integer id) {
		return carouselDao.selectByPrimaryKey(id);
	}

	
	public int updateByExampleSelective(Carousel record, CarouselExample example) {
		return carouselDao.updateByExampleSelective(record, example);
	}

	
	public int updateByExample(Carousel record, CarouselExample example) {
		return carouselDao.updateByExample(record, example);
	}

	
	public int updateByPrimaryKeySelective(Carousel record) {
		return carouselDao.updateByPrimaryKeySelective(record);
	}

	
	public int updateByPrimaryKey(Carousel record) {
		return carouselDao.updateByPrimaryKey(record);
	}

}
