package com.tsingda.smd.service;

import com.tsingda.smd.model.User;

public interface UserService {
    int deleteByPrimaryKey(String ids);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String ids);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}
