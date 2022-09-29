package com.ss.song.fegin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class PracticeClient implements IPracticeClient {

    @Value("${name}")
    private String userName;

    @Override
    public String selectByPrimaryKey(Integer applyId) {
        return "userName:" + userName + "----" + "applyId:" +applyId;
    }
}
