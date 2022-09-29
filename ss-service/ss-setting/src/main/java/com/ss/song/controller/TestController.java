package com.ss.song.controller;

import com.ss.song.fegin.IPracticeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ss/test")
public class TestController {

    @Value("${name}")
    private String userName;

    @Autowired
    private IPracticeClient iPracticeClient;

    @Value("${id}")
    private String userId;
    @GetMapping("/con1")
    public String con1(@RequestParam String aa) {
        String s = iPracticeClient.selectByPrimaryKey(6789);
        return userId + " :::" + userName + "--fegin返回：" + s;
    }

}
