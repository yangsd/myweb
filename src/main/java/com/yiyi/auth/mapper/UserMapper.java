package com.yiyi.auth.mapper;

import com.mybatisplus.mapper.BaseMapper;
import com.yiyi.auth.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from auth_user where login_id=#{login_id}")
    public User findByLoginId(@Param("login_id") String loginId);
}
