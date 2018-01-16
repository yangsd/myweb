package com.yiyi.auth.model;

import com.mybatisplus.annotations.TableName;
import com.yiyi.core.BaseModel;

/**
 * @author sdyang
 * @create 2018-01-16 9:53
 **/
@TableName("auth_role_resource_rel")
public class RoleResourceRel  extends BaseModel<RoleResourceRel> {

    private Long role_id;//角色主键

    private Long resource_id;//资源主键

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long getResource_id() {
        return resource_id;
    }

    public void setResource_id(Long resource_id) {
        this.resource_id = resource_id;
    }
}
