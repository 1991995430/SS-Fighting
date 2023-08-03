package com.ys.authority.entity.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class PageHelperParams implements Serializable {
    @ApiModelProperty(value = "页码，当前页（导出时忽略）", required = true)
    private int pageNum;

    @ApiModelProperty(value = "每页条数（导出时忽略）", required = true)
    private int pageSize;
}
