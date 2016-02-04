package com.tsingda.smd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.tsingda.smd.dao.UserMapper;
import com.tsingda.smd.model.User;
import com.tsingda.smd.service.UserService;

@Component
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    
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

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }

}
