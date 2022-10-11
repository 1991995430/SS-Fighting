package com.ss.song.controller;

import com.ss.song.model.User;
import com.ss.song.rest.RestResponse;
import com.ss.song.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ss/circular")
public class PracticeController {

    private final UserService userService;

    @Value("${name}")
    private String userName;

    @Value("${id}")
    private String userId;

    public PracticeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/con1")
    public String con1(@RequestParam String aa) {
        return userId + " :::" + userName;
    }

    @GetMapping("/con2")
    public RestResponse con2(@RequestParam Integer id) {
        List<User> userList = userService.selectById(id);
        userList.get(0).setName(userName);
        return RestResponse.RestResponseBuilder.createSuccessBuilder().setResult(userList).buidler();
    }

}
