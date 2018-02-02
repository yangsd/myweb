package com.yiyi.auth.controller;

import com.yiyi.auth.service.LoginService;
import com.yiyi.auth.service.ResourceService;
import com.yiyi.core.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sdyang
 * @create 2018-01-11 10:28
 **/
@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private ResourceService resourceService;


    /**
     * 登录页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"","/login"}, method = {RequestMethod.GET})
    public String loginPage(Model model) {
        return "login.html";
    }


    /**
     * 用户登录
     *
     * @param loginid
     * @param password
     * @param model
     * @return
     */
    @RequestMapping(value = "/toLogin", method = {RequestMethod.POST})
    public String toLogin(Model model, @RequestParam("loginid")String loginid, @RequestParam("password")String password) {

        if (StringUtils.isEmpty(loginid)) {
            model.addAttribute("error_msg", "请输入用户名");
            return "login.html";
        }

        if (StringUtils.isEmpty(password)) {
            model.addAttribute("error_msg", "请输入密码");
            return "login.html";
        }

        BaseResult result = loginService.login(loginid,password);
        if (!result.isSuccess()) {
            model.addAttribute("error_msg", result.getMessage());
            logger.error(result.getMessage());
            return "login.html";
        }

        return "redirect:index";
    }
    /**
     * 首页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "index.html";
    }


    /**
     * 获取菜单
     *
     * @return
     */
    @RequestMapping(value = "/menu", method = {RequestMethod.GET})
    @ResponseBody
    public String getMenu() {

        Subject subject = SecurityUtils.getSubject();

        if(subject == null){
            return "";
        }

        String loginid = (String)subject.getPrincipal();

        if(StringUtils.isEmpty(loginid)){
            return "";
        }

        String menu = resourceService.getMenuByLoginId(loginid);

        logger.info(menu);

        return menu;
    }

    /**
     * 退出登录
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/logout", method = {RequestMethod.GET})
    public String logout(Model model) {
        loginService.logout();
        return "login.html";
    }

    /**
     * 踢出用户
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/kickout", method = {RequestMethod.GET})
    public String kickout(Model model) {
        model.addAttribute("msg", "您已在其它地方登录");
        return "login.html";
    }

    /**
     * 获取ip地址
     * @param request
     * @return
     */
    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
