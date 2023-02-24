package com.workspace.ssomanage.controller;

import com.workspace.ssomanage.dto.TestRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author spike
 */
@Slf4j
@Api(tags = "测试接口")
@RestController
@RequestMapping("/ssomanage/user")
public class TestController {
    @Resource
    private HttpServletRequest request;


    /*@ApiOperation(value = "queryUser")
    @PostMapping("/queryUser")
    public SysUser createDept(@RequestBody TestRequestDto testRequestDto) {
        log.info("");
        return sysUserService.testUser();
    }*/

    /*@ApiOperation(value = "getToken")
    @PostMapping("/getToken")
    public String getToken(@RequestBody TestRequestDto testRequestDto) {
        log.info("");
        String tokenInfos = sysUserService.getToken(testRequestDto.getUserName());
        return tokenInfos;
    }*/

    @ApiOperation(value = "getToken")
    @PostMapping("/getKvByToken")
    public String getKvByToken(@RequestBody TestRequestDto testRequestDto) {
        log.info("");
        // String tokenInfos = sysUserService.getToken(testRequestDto.getUserName());
        return "it is a good day！";
    }


}
