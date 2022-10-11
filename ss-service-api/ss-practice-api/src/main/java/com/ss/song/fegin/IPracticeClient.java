package com.ss.song.fegin;

import com.ss.song.vo.CtUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://localhost:8955/ss-practice", name= "ss-practice")
public interface IPracticeClient {

    @GetMapping(value= "/ss/selectByPrimaryKey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = "application/json;charset=UTF-8")
    CtUser selectByPrimaryKey(@RequestParam Integer applyId);

    @GetMapping(value= "/ss/updateByPrimaryKey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = "application/json;charset=UTF-8")
    void updateByPrimaryKey(@RequestParam Integer id);
}
