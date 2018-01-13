package com.yiyi.login.mapper;

import com.mybatisplus.mapper.BaseMapper;
import com.yiyi.login.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
