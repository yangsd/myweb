package com.yiyi.auth.service;

import com.yiyi.core.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author sdyang
 * @create 2018-01-16 10:59
 **/
@Service
public class LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Value("${expire}")
    private int expire;

    public BaseResult login(String userId, String password) {
        BaseResult result = new BaseResult();
        //先退出登录
        SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());

        // 登录后存放进shiro token
        UsernamePasswordToken token = new UsernamePasswordToken(userId, password);

        Subject subject = SecurityUtils.getSubject();
        String msg = null;
        try {
            subject.login(token);
        } catch (UnknownAccountException ue) {
            msg = "用户名或者密码错误";
        } catch (IncorrectCredentialsException ie) {
            msg = "用户名或者密码错误";
        } catch (LockedAccountException le) {
            msg = "用户被锁定";
        } catch (ExcessiveAttemptsException ee) {
            msg = "您尝试登录的次数过多，请" + expire / 60 + "分钟后再试";
        } catch (UnsupportedTokenException use) {
            msg = "不支持这种验证方式";
        } catch (UnauthorizedException ae){
            msg = "没有访问权限";
        }catch (ShiroException se) {
            msg = se.toString();
            logger.error(se.toString());
        }
        if (StringUtils.isNotBlank(msg)) {
            result.setSuccess(false);
            result.setMessage(msg);
        }
        return result;
    }

    public boolean logout() {
        String loginId = (String) SecurityUtils.getSubject().getPrincipal();
        if( loginId != null) {
            SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
        }
        return true;
    }

    public Boolean isLogin() {
        Subject subject = SecurityUtils.getSubject();
        if(subject != null){
            return true;
        }
        return false;
    }

}
