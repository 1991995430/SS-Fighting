package com.ys.authority.controller.user.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class UserGetParams implements Serializable {
    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户", required = true)
    private Integer id;
}
