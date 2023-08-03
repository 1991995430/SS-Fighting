package com.ys.authority.controller.url.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UrlDeleteParams {
    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "菜单ID", required = true)
    private Integer id;
}
