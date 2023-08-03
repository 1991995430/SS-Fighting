package com.ss.song.controller;

import com.ss.song.common.exception.SsException;
import com.ss.song.dto.UserDto;
import com.ss.song.model.User;
import com.ss.song.rest.RestResponse;
import com.ss.song.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ss/circular")
public class PracticeController {

    private final UserService userService;

    //@Value("${name}")
    private String userName;

    //@Value("${id}")
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

    @PostMapping("/con3")
    public String con3(@RequestBody UserDto userDto) {
//        if (null == userDto.getAddress()) {
//            throw new SsException("地址为空！！！");
//        }
        return getString(userDto);
    }

    private String getString(User user) {
        //UserDto userDto = (UserDto)user;
        return "applyName::" + user.getApplyName() + "--userName::"+user.getName();
    }

}
