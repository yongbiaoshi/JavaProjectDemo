package com.tsingda.smd.service.impl;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tsingda.smd.dao.mapper.UserMapper;
import com.tsingda.smd.model.User;
import com.tsingda.smd.service.UserService;
import com.tsingda.smd.util.JsonUtil;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource(name = "redisTemplate")
    private RedisOperations<String, User> userOps;
    
    @Override
    public int deleteByPrimaryKey(String ids) {
        return userMapper.deleteByPrimaryKey(ids);
    }

    @Override
    public int insert(User record) {
        return userMapper.insert(record);
    }

    @Override
    public int insertSelective(User record) {
        return userMapper.insertSelective(record);
    }

    @Override
    public User selectByPrimaryKey(String ids) {
        return userMapper.selectByPrimaryKey(ids);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateByPrimaryKey(User record) {
        int i = userMapper.updateByPrimaryKey(record);
        System.out.println(i / 0);
        return i;
    }

    @Override
    public User selectFromRedis(String key) throws JsonParseException, JsonMappingException, IOException {
        Object obj = userOps.opsForValue().get(key);
        return JsonUtil.changeType(obj, User.class);
    }

    @Override
    public void saveToRedis(User user) {
        userOps.opsForValue().set(user.getIds(), user);
    }
    
}
