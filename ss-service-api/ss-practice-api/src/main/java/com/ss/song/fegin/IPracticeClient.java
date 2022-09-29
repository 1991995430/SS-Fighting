package com.ss.song.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://localhost:8955/ss-practice", name= "ss-practice")
public interface IPracticeClient {

    @GetMapping("/ss/selectByPrimaryKey")
    String selectByPrimaryKey(@RequestParam Integer applyId);

}
