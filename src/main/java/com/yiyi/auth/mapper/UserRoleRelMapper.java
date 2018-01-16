package com.yiyi.auth.mapper;

import com.mybatisplus.mapper.BaseMapper;
import com.yiyi.auth.model.UserRoleRel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleRelMapper extends BaseMapper<UserRoleRel> {

    @Select("select role_id from auth_user_role_rel where user_id=#{user_id}")
    public List<Long> findByUserId(Long userId);
}
