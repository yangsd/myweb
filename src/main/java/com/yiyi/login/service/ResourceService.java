package com.yiyi.login.service;

import com.yiyi.login.controller.UserController;
import com.yiyi.login.mapper.ResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sdyang
 * @create 2018-01-11 10:29
 **/
@Service
public class ResourceService {

    private static Logger logger = LoggerFactory.getLogger(ResourceService.class);

    @Autowired
    private ResourceMapper resourceMapper;

}
