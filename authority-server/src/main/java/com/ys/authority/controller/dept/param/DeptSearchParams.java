package com.ys.authority.controller.dept.param;

import com.ys.common.bean.PageHelperReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author spike
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class DeptSearchParams extends PageHelperReq {
    @ApiModelProperty(value = "部门编码")
    private String code;
    @ApiModelProperty(value = "部门号")
    private Integer id;
    @ApiModelProperty(value = "部门名")
    private String name;
    @ApiModelProperty(value = "部门层级")
    private Integer depth;
    @ApiModelProperty(value = "查询深度")
    private Integer searchDepth;


}
