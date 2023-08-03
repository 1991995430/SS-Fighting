package com.ys.authority.controller.user.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel
public class UserEditPasswordParams {
    @NotEmpty
    @ApiModelProperty(value = "用户原密码")
    private String oldPassword;
    @NotEmpty
    @ApiModelProperty(value = "用户新密码")
    private String newPassword;
    @NotEmpty
    @ApiModelProperty(value = "确认新密码")
    private String confirmNewPassword;
}
