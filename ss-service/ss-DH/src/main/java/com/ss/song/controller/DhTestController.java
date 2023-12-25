package com.ss.song.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dh")
public class DhTestController {


    @GetMapping("/con1")
    public String con1() {
        return null;
    }


    @PostMapping("/con3")
    public String con3() {
//        if (null == userDto.getAddress()) {
//            throw new SsException("地址为空！！！");
//        }
        return null;
    }
}
