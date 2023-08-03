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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

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
    public SysUser createDept(@RequestBody TestRequestDto testRequestDto, HttpServletRequest request, HttpServletResponse response) {
        log.info("");
        request.getHeaders("access_token");
        return sysUserService.testUser();
    }

    @ApiOperation(value = "getToken")
    @PostMapping("/getToken")
    public String getToken(@RequestBody TestRequestDto testRequestDto) {
        log.info("");
        String tokenInfos = sysUserService.getToken(testRequestDto.getUserName());
        /*try {
            response.sendRedirect("http://localhost:8082");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        return tokenInfos;
    }


    // todo 返回一次性code存到redis

    // todo

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

    /*@ApiOperation(value = "sso登录")
    @PostMapping("/login")
    @LoginAuthIgnore
    public String login(@RequestParam(value = "redirectUri", required = false) String redirectUri,
                        @RequestParam(required = false) String username,
                        @RequestParam(required = false) String password,
                        HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        if (!username.equals("ss") && password.equals("123456")) {
            request.setAttribute("errorMessage", "用户名或密码错误");
            return "http://authentication.sso.com:8080/";
        }

        return generateCodeAndRedirect(redirectUri);
    }*/

    @ApiOperation(value = "sso登录")
    @PostMapping("/login")
    @LoginAuthIgnore
    public String login(@RequestBody AuthParamDto authParamDto) throws UnsupportedEncodingException {

        if (!authParamDto.getUserName().equals("ss") && authParamDto.getPassword().equals("123456")) {
            request.setAttribute("errorMessage", "用户名或密码错误");
            return "http://authentication.sso.com:8080/";
        }

        return generateCodeAndRedirect(authParamDto.getRedirectUri());
    }

    String generateCodeAndRedirect(String redirectUri) throws UnsupportedEncodingException {
        // 生成授权码
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        return "redirect:" + authRedirectUri(redirectUri, code);
    }

    String authRedirectUri(String redirectUri, String code) throws UnsupportedEncodingException {
        StringBuilder sbf = new StringBuilder(redirectUri);
        if (redirectUri.indexOf("?") > -1) {
            sbf.append("&");
        }
        else {
            sbf.append("?");
        }
        sbf.append("code").append("=").append(code);
        return URLDecoder.decode(sbf.toString(), "utf-8");
    }
}
