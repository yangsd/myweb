package com.yiyi.auth.controller;

import com.yiyi.auth.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author sdyang
 * @create 2018-01-11 10:28
 **/
@Controller
public class ResourceController {

    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService resourceService;

}
