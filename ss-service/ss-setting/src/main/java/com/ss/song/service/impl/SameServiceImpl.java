package com.ss.song.service.impl;

import com.ss.song.mapper.UserMapper;
import com.ss.song.model.User;
import com.ss.song.service.SameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SameServiceImpl implements SameService {

    private final UserMapper userMapper;

    @Autowired
    public SameServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void updateByPrimKey(Integer id) {

        User user = new User();
        user.setId(id);
        user.setName("第二个服务修改555555");
        user.setAddress("江苏省spring");
        User user1 = userMapper.selectByPrimaryKey(1);
        System.out.println(user1.getName());
        userMapper.updateByPrimaryKey(user);

        System.out.println("第二个服务。。。。。");
        throw new RuntimeException("测试抛异常");
    }
}
