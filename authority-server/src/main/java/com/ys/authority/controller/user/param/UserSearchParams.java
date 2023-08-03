package com.ys.authority.controller.user.param;

import com.ys.authority.entity.params.PageHelperParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class UserSearchParams extends PageHelperParams {
    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "登录账号")
    private String name;

    @ApiModelProperty(value = "用户真实姓名")
    private String trueName;

    @ApiModelProperty(value = "业务id")
    private Integer serviceId;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "角色名")
    private String roleName;

    @ApiModelProperty(value = "性别：1男,0女")
    private Integer sex;

    @ApiModelProperty(value = "手机号码")
    private String mobileNumber;

    @ApiModelProperty(value = "邮箱")
    private String email;
}
