package com.ys.authority.controller.service.param;

import com.ys.authority.entity.params.PageHelperParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class ServiceSearchParams extends PageHelperParams {
    @ApiModelProperty(value = "服务ID")
    private Integer id;

    @ApiModelProperty(value = "服务名称")
    private String name;

    // 测试用，不做api说明
    private String remark;
}
