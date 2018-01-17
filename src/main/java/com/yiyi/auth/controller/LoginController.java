package com.yiyi.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author sdyang
 * @create 2018-01-11 10:28
 **/
@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);


    /**
     * 登录页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"","/login"}, method = {RequestMethod.GET})
    public String login(Model model) {
        return "login.html";
    }

}
