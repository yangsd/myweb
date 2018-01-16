package com.yiyi.auth.service;

import com.yiyi.auth.mapper.RoleMapper;
import com.yiyi.auth.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author sdyang
 * @create 2018-01-11 10:29
 **/
@Service
public class RoleService {

    private static Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleMapper roleMapper;

    public void save(Role role) {
        if(role.getId() == null){
            roleMapper.insert(role);
        }else{
            roleMapper.updateById(role);
        }
    }


    public Set<String> findRoles(Long... roleIds) {
        Set<String> roles = new HashSet<String>();
        for (Long roleId : roleIds) {
            Role role = roleMapper.selectById(roleId);
            if (role != null) {
                roles.add(role.getName());
            }
        }
        return roles;
    }

    public Role findRole(Long user_id) {
        return roleMapper.findByUserId(user_id);
    }


}
