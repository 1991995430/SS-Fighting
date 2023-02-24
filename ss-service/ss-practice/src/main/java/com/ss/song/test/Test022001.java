package com.ss.song.test;

import com.ss.song.vo.LoginResultVO;
import com.ss.song.vo.RestResult;

import java.time.LocalDateTime;

public class Test022001 {

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().plusMinutes(1L).getMinute());

        LoginResultVO loginResultVO = gets().getData();
        System.out.println(loginResultVO.getId());
    }

    public static RestResult<LoginResultVO> gets() {
        LoginResultVO resultVO = new LoginResultVO();
        resultVO.setId(12);
        resultVO.setEmail("199199");
        return resultVO.success();
    }

}
