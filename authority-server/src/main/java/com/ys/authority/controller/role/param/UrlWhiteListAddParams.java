package com.ys.authority.controller.role.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UrlWhiteListAddParams {
    @NotNull(message = "角色id不能为空")
    @ApiModelProperty(value = "角色id", required = true)
    private Integer roleId;

    @NotNull(message = "业务id不能为空")
    @ApiModelProperty(value = "业务id", required = true)
    private Integer serviceId;

    @NotEmpty(message = "url不能为空")
    @ApiModelProperty(value = "url", required = true)
    private String url;
}
