package com.yiyi.auth.mapper;

import com.mybatisplus.mapper.BaseMapper;
import com.yiyi.auth.model.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RoleMapper extends BaseMapper<Role> {

    @Select("select * from auth_role where user_id=#{user_id}")
    public Role findByUserId(@Param("user_id") Long userId);
}
