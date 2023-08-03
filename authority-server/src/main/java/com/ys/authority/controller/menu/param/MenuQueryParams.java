package com.ys.authority.controller.menu.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MenuQueryParams {
    @ApiModelProperty(value = "业务ID,为空时查询所有能查到的数据", required = true)
    private Integer serviceId;
}
