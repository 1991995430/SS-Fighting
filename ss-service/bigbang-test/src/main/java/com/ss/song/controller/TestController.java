package com.ss.song.controller;

import com.ss.song.config.SshConfig;
import com.ss.song.mapper.DataDtoMapper;
import com.ss.song.service.AsyncService;
import com.ss.song.test.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/v1/test")
public class TestController {

    @Autowired
    private HttpServletRequest request;

    private final RestTemplate restTemplate = new RestTemplate();

    @Resource
    private DataDtoMapper dataDtoMapper;

    @Resource
    private UserService userService;

    @Resource
    private SshConfig sshConfig;

    @Resource
    private AsyncService asyncService;

    @PostMapping("/con1")
    public void con1() {

        System.out.println(sshConfig.getUsername());
        System.out.println(sshConfig.getPassword());
        System.out.println(userService.getStr1());

    }

    @PostMapping("/con2")
    private void con2() {
        System.out.println(Thread.currentThread().getName());
        asyncService.aa();
    }

    @PostMapping("/con3")
    private void con3() throws Exception {

    }
}
