package com.workspace.ssomanage.controller;

import com.workspace.ssomanage.dto.TestRequestDto;
import com.workspace.ssomanage.mapper.dao.SysUser;
import com.workspace.ssomanage.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author spike
 */
@Slf4j
@Api(tags = "测试接口")
@RestController
@RequestMapping("/sso/test")
public class TestController {
    @Resource
    private HttpServletRequest request;

    @Resource
    private SysUserService sysUserService;

    @ApiOperation(value = "queryUser")
    @PostMapping("/queryUser")
    public SysUser createDept(@RequestBody TestRequestDto testRequestDto) {
        log.info("");
        return sysUserService.testUser();
    }

    @ApiOperation(value = "getToken")
    @PostMapping("/getToken")
    public String getToken(@RequestBody TestRequestDto testRequestDto) {
        log.info("");
        String tokenInfos = sysUserService.getToken(testRequestDto.getUserName());
        return tokenInfos;
    }


}
