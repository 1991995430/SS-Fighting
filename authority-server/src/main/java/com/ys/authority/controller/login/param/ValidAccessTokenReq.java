package com.ys.authority.controller.login.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class ValidAccessTokenReq {
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "待添加token的用户", required = true)
    String userName;

    @ApiModelProperty(value = "用户业务id", required = true)
    @NotNull
    private Integer serviceId;

    @ApiModelProperty(value = "access-token", required = true)
    @NotEmpty
    private String accessToken;

    @ApiModelProperty(value = "有效时间", required = true)
    @NotNull
    private Integer validTime;
}
