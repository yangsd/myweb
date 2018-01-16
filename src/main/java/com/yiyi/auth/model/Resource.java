package com.yiyi.auth.model;

import com.mybatisplus.annotations.TableName;
import com.yiyi.core.BaseModel;

/**
 * 资源
 * @author sdyang
 * @create 2018-01-11 10:12
 **/
@TableName("auth_resource")
public class Resource extends BaseModel<Resource> {

    public static Long parentCode = 0L;

    private String code;

    private String name; // 资源名称

    private ResourceType type = ResourceType.menu; // 资源类型

    private String url; // 资源路径

    private String description;//资源描述

    private String icon;//资源图标

    private Long parent_id;//父节点

    private Boolean enable = Boolean.TRUE;//状态

    private int order_num;//排序号

    public static enum ResourceType {
        menu("菜单"), button("按钮"), root("根节点");

        private final String info;

        private ResourceType(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }

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

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
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
