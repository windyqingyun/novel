package com.jeeplus.modules.bus.service;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.bus.dao.FeedbackDao;
import com.jeeplus.modules.bus.entity.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class FeedbackService {
    @Autowired
    private FeedbackDao feedbackDao;

    public void insert(Feedback feedback){

        feedback.setId(IdGen.uuid());
        feedback.setCreateDate(new Date());
        feedbackDao.insert(feedback);

    }
}
