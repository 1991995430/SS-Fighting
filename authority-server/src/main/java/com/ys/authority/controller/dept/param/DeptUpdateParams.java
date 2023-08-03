package com.ys.authority.controller.dept.param;

import com.ys.authority.mapper.dao.SysDept;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author spike
 */
@Data
@ApiModel
public class DeptUpdateParams  {
    @NotEmpty(message = "部门名称不为空")
    @ApiModelProperty(value = "部门名称", required = true)
    private String name;
    @NotNull(message = "部门id不为空")
    @ApiModelProperty(value = "部门id", required = true)
    private Integer id;
    @NotNull(message = "部门描述不为空")
    @ApiModelProperty(value = "部门描述", required = true)
    private String description;

}
