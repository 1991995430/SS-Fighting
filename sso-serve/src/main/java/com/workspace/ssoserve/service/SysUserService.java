package com.workspace.ssoserve.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.workspace.ssoserve.dto.TestRequestDto;
import com.workspace.ssoserve.entity.pojo.TokenInfo;
import com.workspace.ssoserve.mapper.SysUserMapper;
import com.workspace.ssoserve.mapper.dao.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysUser.fName, "admin");
        queryWrapper.eq(SysUser.fStatus, 1);
        SysUser dbSysUser = sysUserMapper.selectOne(queryWrapper);
        return dbSysUser;
    }

    public String getToken(String userName) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysUser.fName, userName);
        queryWrapper.eq(SysUser.fStatus, 1);
        SysUser dbSysUser = sysUserMapper.selectOne(queryWrapper);
        TokenInfo tokenInfo = new TokenInfo();
        if (dbSysUser != null) {
            tokenInfo.setUserId(dbSysUser.getId());
            tokenInfo.setSysUserInfo(dbSysUser);
            tokenInfo.setOverTime(LocalDateTime.now().plusMinutes(1));
        }
        Duration du = Duration.between(LocalDateTime.now(), tokenInfo.getOverTime());
        accessTokenStringRT.opsForValue().set(token, gson.toJson(tokenInfo), du);
        String tokenInfos = accessTokenStringRT.opsForValue().get(token);
        TestRequestDto testRequestDto = new TestRequestDto();
        testRequestDto.setUserName("shangsong");
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject("http://192.168.3.120:19100/sso-manage/ssomanage/user/getKvByToken", testRequestDto, String.class);

        return result;
    }

}
