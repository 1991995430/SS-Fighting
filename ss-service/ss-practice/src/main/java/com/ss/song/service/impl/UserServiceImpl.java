package com.ss.song.service.impl;

import com.ss.song.mapper.UserMapper;
import com.ss.song.model.User;
import com.ss.song.model.UserCriteria;
import com.ss.song.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public List<User> selectById(Integer id) {
        UserCriteria userExample = new UserCriteria();
        userExample.createCriteria().andIdEqualTo(id);
        return new ArrayList<User>();
    }
}
