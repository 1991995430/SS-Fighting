package com.workspace.ssoserve.controller;

import com.workspace.ssoserve.dto.AuthParamDto;
import com.workspace.ssoserve.dto.TestRequestDto;
import com.workspace.ssoserve.mapper.dao.SysUser;
import com.workspace.ssoserve.service.LoginService;
import com.workspace.ssoserve.service.SysUserService;
import com.workspace.ssoserve.utils.annotation.LoginAuthIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private final LoginService loginService;

    @Resource
    HttpServletResponse response;

    public TestController(LoginService loginService) {
        this.loginService = loginService;
    }

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

    @ApiOperation(value = "获取验证码")
    @PostMapping("/getVerifyCodeImage")
    @LoginAuthIgnore
    public void getVerifyCodeImage(@RequestBody @Valid @ApiParam(required = true) AuthParamDto authParams) {
        log.info("###[getVerifyCodeImage]AuthParam:{}", authParams);
        String loginStamp = authParams.getLoginStamp();
        if (loginStamp == null) {
            response.setStatus(400);
            return;
        }
        loginService.getVerifyCodeImage(loginStamp, response);
    }

}
