package com.ss.song.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import scala.annotation.meta.param;

import javax.annotation.Resource;

/**
 * author shangsong 2023/11/27
 */
@RestController
@RequestMapping("/v1/kafka")
public class KafkaController {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/con1")
    public void con1(@RequestParam String msg) {

        kafkaTemplate.send("ss", msg);

    }

}
