package com.ss.song.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ss/circular")
public class PracticeController {

    @Value("${name}")
    private String userName;

    @Value("${id}")
    private String userId;
    @GetMapping("/con1")
    public String con1(@RequestParam String aa) {
        return userId + " :::" + userName;
    }

}
