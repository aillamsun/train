package com.train.service.impl;

import com.train.core.service.BaseService;
import com.train.mapper.TestMapper;
import com.train.model.Test;
import com.train.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sungang on 2017/3/15.
 */
@Service
public class TestServiceImpl extends BaseService<Test> implements TestService {

    @Autowired
    private TestMapper testMapper;
}
