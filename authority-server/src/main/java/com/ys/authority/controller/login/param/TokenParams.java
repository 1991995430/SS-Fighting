package com.ys.authority.controller.login.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel
public class TokenParams implements Serializable {
    @NotBlank(message = "待验证的token不能为空")
    @ApiModelProperty(value = "待验证的token", required = true)
    private String token;
}
