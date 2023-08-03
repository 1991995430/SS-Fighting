package com.ys.authority.controller.user.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户报表查询参数
 */
@Data
@ApiModel
public class UserDataMonthParams implements Serializable {
    @ApiModelProperty(value = "月份，yyyy-MM")
    private String month;
}
