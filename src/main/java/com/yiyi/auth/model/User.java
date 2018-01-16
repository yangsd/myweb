package com.yiyi.auth.model;

import com.mybatisplus.annotations.TableName;
import com.yiyi.core.BaseModel;

/**
 * 用户
 * @author sdyang
 * @create 2018-01-11 10:12
 **/
@TableName("auth_user")
public class User extends BaseModel<User>{

    private String loginid;// 登录帐号

    private String password;// 密码

    private String username;// 用户姓名

    private String mobile;//手机号码

    private String weixinid;//微信号

    private String email;// 邮箱

    private String salt; // 加密密码的盐

    private int status=1 ;// 状态：默认启用1，停用-1，锁定0

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWeixinid() {
        return weixinid;
    }

    public void setWeixinid(String weixinid) {
        this.weixinid = weixinid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCredentialsSalt() {
        return username + salt;
    }
}
