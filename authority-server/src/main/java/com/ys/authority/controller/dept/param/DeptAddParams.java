package com.ys.authority.controller.dept.param;

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
public class DeptAddParams  {
    @NotEmpty(message = "父类编码不为空")
    @ApiModelProperty(value = "部门编码", required = true)
    private String code;
    @NotEmpty(message = "部门名不为空")
    @ApiModelProperty(value = "部门名", required = true)
    private String name ;
    @NotNull(message = "部门层级不能为空")
    @ApiModelProperty(value = "部门层级", required = true)
    private Integer depth ;
    @NotNull(message = "部门描述")
    @ApiModelProperty(value = "部门层级", required = true)
    private String description ;

}
