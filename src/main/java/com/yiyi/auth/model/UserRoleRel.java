package com.yiyi.auth.model;

import com.mybatisplus.annotations.TableName;
import com.yiyi.core.BaseModel;

/**
 * @author sdyang
 * @create 2018-01-16 9:52
 **/
@TableName("auth_user_role_rel")
public class UserRoleRel extends BaseModel<UserRoleRel> {

    private Long user_id;//用户主键

    private Long role_id;//角色主键

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }
}
