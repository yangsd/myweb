package com.yiyi.login.controller;

import com.yiyi.login.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author sdyang
 * @create 2018-01-11 10:28
 **/
@Controller
public class RoleController {

    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;
}
