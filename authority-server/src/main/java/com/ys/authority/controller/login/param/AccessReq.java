package com.ys.authority.controller.login.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class AccessReq {
    @NotBlank
    @ApiModelProperty(value = "access-token", required = true)
    private String token;

    @NotBlank
    @ApiModelProperty(value = "url", required = true)
    private String url;
}
