package com.ss.song.service;

import com.ss.song.model.User;

import java.util.List;

public interface UserService {

    List<User> selectById(Integer id);

    void updateByExample(User user);

}
