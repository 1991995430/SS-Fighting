package com.ss.song.controller;

import com.ss.song.fegin.IPracticeClient;
import com.ss.song.service.SsService;
import com.ss.song.vo.CtUser;
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

    @Value("${id}")
    private String userId;

    private final IPracticeClient iPracticeClient;

    private final SsService ssService;

    @Autowired
    public TestController(IPracticeClient iPracticeClient, SsService ssService) {
        this.iPracticeClient = iPracticeClient;
        this.ssService = ssService;
    }

    @GetMapping("/con1")
    public CtUser con1(@RequestParam String aa) {


        return iPracticeClient.selectByPrimaryKey(6789);
    }

    @GetMapping("/con2")
    public void con2(@RequestParam Integer id) {
        ssService.updateDiffService(id);
    }

    @GetMapping("/con3")
    public void con3(@RequestParam Integer id) {
        ssService.updateSameService(id);
    }

}
