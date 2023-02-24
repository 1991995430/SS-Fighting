package com.workspace.ssomanage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.workspace.ssomanage.entity.pojo.TokenInfo;
import com.workspace.ssomanage.mapper.SysUserMapper;
import com.workspace.ssomanage.mapper.dao.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class SysUserService {

    private final StringRedisTemplate accessTokenStringRT;

    @Resource
    private SysUserMapper sysUserMapper;

    private static Gson gson = new Gson();

    @Autowired
    public SysUserService(StringRedisTemplate accessTokenStringRT) {
        this.accessTokenStringRT = accessTokenStringRT;
    }

    public SysUser testUser() {
        return new SysUser();
    }

    public String getToken(String userName) {
        return "";
    }

}
