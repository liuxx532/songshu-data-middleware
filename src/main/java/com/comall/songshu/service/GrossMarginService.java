package com.comall.songshu.service;

import com.comall.songshu.repository.GrossMarginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liugaoyu on 2017/4/20.
 */
@Service
public class GrossMarginService {
    @Autowired
    private GrossMarginRepository grossMarginRepository;

    public Object[] getGrossMargin(){
        return  grossMarginRepository.findAll().toArray();
    }

}