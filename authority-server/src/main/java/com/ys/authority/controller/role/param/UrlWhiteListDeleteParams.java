package com.ys.authority.controller.role.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UrlWhiteListDeleteParams {
    @NotNull(message = "url白名单条目idid不能为空")
    @ApiModelProperty(value = "url白名单条目id", required = true)
    Integer id;
}
