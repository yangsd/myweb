package com.yiyi.auth.mapper;

import com.mybatisplus.mapper.BaseMapper;
import com.yiyi.auth.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from auth_user where loginid=#{loginid}")
    public User findByLoginId(@Param("loginid") String loginId);
}
