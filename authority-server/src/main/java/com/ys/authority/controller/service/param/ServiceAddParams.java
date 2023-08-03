package com.ys.authority.controller.service.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel
public class ServiceAddParams implements Serializable {
    @NotBlank(message = "服务名称不能为空")
    @ApiModelProperty(value = "服务名称", required = true)
    private String name;

    @ApiModelProperty(value = "服务说明")
    private String remark;
}
