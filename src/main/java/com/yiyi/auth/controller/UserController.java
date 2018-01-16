package com.yiyi.auth.controller;

import com.yiyi.auth.model.User;
import com.yiyi.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sdyang
 * @create 2018-01-11 10:28
 **/
@Controller
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "test",method = {RequestMethod.GET})
    @ResponseBody
    public String insertTestUser(){
        User user = new User();
        user.setLoginid("admin");
        user.setPassword("admin");
        user.setUsername("管理员");
        user.setStatus(1);
        userService.save(user);
        return "success";
    }
}
