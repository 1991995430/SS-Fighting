package com.workspace.ssoserve.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class AuthParamDto implements Serializable {
    @ApiModelProperty(value = "用户名，登录时使用", required = true)
    private String userName;

    @ApiModelProperty(value = "用户密码，登录时使用，携带密文", required = true)
    private String password;

    @ApiModelProperty(value = "登录戳，客户端生成，并在获取验证码及登录时携带", required = true)
    private String loginStamp;

    @ApiModelProperty(value = "验证码，登录时使用", required = true)
    private String verifyCode;

    @ApiModelProperty(value = "用户业务id", required = true)
    private Integer serviceId;

    /**
     * 内部使用，端侧的user-agent字段
     */
    private String userAgent;

    private String redirectUri;

    /**
     * 内部使用，可以直接添加指定的access-token
     */
    private String exceptAccessToken;

    /**
     * 内部使用，可以直接指定有效时间
     */
    private Integer exceptValidTime;
}
