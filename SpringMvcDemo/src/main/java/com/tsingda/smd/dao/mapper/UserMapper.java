package com.tsingda.smd.dao.mapper;

import com.tsingda.smd.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(String ids);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String ids);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    int countAll();
}