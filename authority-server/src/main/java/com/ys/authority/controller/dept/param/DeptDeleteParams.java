package com.ys.authority.controller.dept.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
/**
 * @author spike
 */
@Data
@ApiModel
public class DeptDeleteParams {
    @NotEmpty(message = "部门编码不为空")
    @ApiModelProperty(value = "部门编码", required = true)
    private String code;
}
