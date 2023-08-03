package com.ys.authority.controller.role.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class MenuWhiteListUpdateParams {
    @NotNull
    @ApiModelProperty(value = "角色ID", required = true)
    private Integer roleId;

    @NotNull
    @ApiModelProperty(value = "业务ID", required = true)
    private Integer serviceId;

    @NotBlank(message = "菜单内容不能为空")
    @ApiModelProperty(value = "菜单内容", required = true)
    private String menu;
}
