package com.ys.authority.controller.user.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UserSearchInnerReq {
    @ApiModelProperty(value = "业务id")
    @NotNull
    private Integer serviceId;

    @ApiModelProperty(value = "角色名")
    @NotEmpty
    private String roleName;
}
