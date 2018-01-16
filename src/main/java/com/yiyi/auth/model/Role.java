package com.yiyi.auth.model;

import com.mybatisplus.annotations.TableName;
import com.yiyi.core.BaseModel;

/**
 * 角色
 * @author sdyang
 * @create 2018-01-11 10:12
 **/
@TableName("auth_role")
public class Role extends BaseModel<Role>{

    private String code;//编码

    private String name; // 角色标识 程序中判断使用,如"admin"

    private String description; // 角色描述

    private Boolean enable = Boolean.TRUE; // 是否可用

    private int order_num;//排序号

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }
}
