package com.yiyi.auth.service;

import com.myutil.RandomUtil;
import com.yiyi.auth.mapper.RoleMapper;
import com.yiyi.auth.mapper.UserMapper;
import com.yiyi.auth.mapper.UserRoleRelMapper;
import com.yiyi.auth.model.Role;
import com.yiyi.auth.model.User;
import com.yiyi.auth.shiro.PasswordHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author sdyang
 * @create 2018-01-11 10:29
 **/
@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleRelMapper userRoleRelMapper;


    // 根据登录名查找用户
    public User findByLoginid(String loginId) {
        return userMapper.findByLoginId(loginId);
    }


    // 根据登录名查找角色
    public Set<String> findRoles(String loginId) {

        User user = this.findByLoginid(loginId);

        if (user == null) {
            return Collections.emptySet();
        }

        List<Long> roleIds = userRoleRelMapper.findByUserId(user.getId());

        if (roleIds == null || roleIds.size()==0) {
            return Collections.emptySet();
        }

        Set<String> roles = new HashSet<String>();

        for (Long roleId : roleIds) {
            Role role = roleMapper.selectById(roleId);
            if (role != null) {
                roles.add(role.getCode());
            }
        }
        return roles;
    }

    public Set<String> findPermissions(String loginid) {
        return this.findRoles(loginid);
    }


    /**
     * 保存用户
     * @param user
     * @author sdyang
     * @date   2016年2月23日 上午11:45:14
     */
    public void save(User user){
        if(user.getId() == null){
            PasswordHelper.encryptPassword(user);
            user.setCreate_time(new Date());
            userMapper.insert(user);
        }else{
            User u = userMapper.selectById(user.getId());
            u.setUsername(user.getUsername());
            u.setEmail(user.getEmail());
            u.setStatus(user.getStatus());
            u.setWeixinid(user.getWeixinid());
            u.setMobile(user.getMobile());
            PasswordHelper.encryptPassword(u);
            u.setModify_time(new Date());

            userMapper.updateById(u);
        }
    }

    // 修改密码
    public void changePassword(Long userId, String newPassword) {
        User user = userMapper.selectById(userId);
        user.setPassword(newPassword);
        PasswordHelper.encryptPassword(user);
        userMapper.updateById(user);
    }

    //重置密码
    public String resetPassword(Long id){
        User user = userMapper.selectById(id);
        String password = RandomUtil.getRandomKey();//随机密码
        user.setPassword(password);
        PasswordHelper.encryptPassword(user);
        userMapper.updateById(user);
        return password;
    }


}
