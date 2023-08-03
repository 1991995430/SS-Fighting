package com.ys.authority.controller.role.param;

import com.ys.authority.entity.params.PageHelperParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class MenuWhiteListSearchParams extends PageHelperParams {
    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "服务id")
    private Integer serviceId;

    @ApiModelProperty(value = "角色列表")
    private List<Integer> roleIdList;
}
