package com.comall.songshu.service;

import com.comall.songshu.repository.VisitorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liugaoyu on 2017/4/20.
 */
@Service
public class VisitorsService {

    @Autowired
    private VisitorsRepository visitorsRepository;

    public Object[] getVisitors(){
        return visitorsRepository.findAll().toArray();
    }
}
