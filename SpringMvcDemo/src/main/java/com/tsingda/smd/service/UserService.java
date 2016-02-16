package com.tsingda.smd.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tsingda.smd.model.User;

public interface UserService {
    int deleteByPrimaryKey(String ids);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String ids);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectFromRedis(String key) throws JsonParseException, JsonMappingException, IOException;

    void saveToRedis(User user);
}
