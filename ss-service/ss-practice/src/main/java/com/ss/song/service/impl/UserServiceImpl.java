package com.ss.song.service.impl;

import com.ss.song.mapper.UserMapper;

import com.ss.song.model.User;
import com.ss.song.model.UserCriteria;
import com.ss.song.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> selectById(Integer id) {
        UserCriteria userExample = new UserCriteria();
        userExample.createCriteria().andIdEqualTo(id);
        return userMapper.selectByExample(userExample);
    }

    @Override
    @Transactional
    public void updateByExample(User user) {
        UserCriteria userExample = new UserCriteria();
        userExample.createCriteria().andIdEqualTo(user.getId());
        userMapper.updateByExample(user, userExample);
    }
}
