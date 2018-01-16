package com.yiyi.auth.mapper;

import com.mybatisplus.mapper.BaseMapper;
import com.yiyi.auth.model.RoleResourceRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleResourceRelMapper extends BaseMapper<RoleResourceRel> {

    @Select("select resource_id from auth_role_resource_rel where role_id=#{role_id}")
    public List<Long> findByRoleId(@Param("role_id") Long roleId);
}
