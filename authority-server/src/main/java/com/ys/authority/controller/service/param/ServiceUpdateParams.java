package com.ys.authority.controller.service.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class ServiceUpdateParams implements Serializable {
    @NotNull(message = "服务id不能为空")
    @ApiModelProperty(value = "服务ID", required = true)
    private Integer id;

    @ApiModelProperty(value = "服务名称")
    private String name;

    @ApiModelProperty(value = "服务名称")
    private String remark;
}
