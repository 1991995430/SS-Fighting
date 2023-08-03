package com.ys.authority.controller.menu.vo;

import com.ys.authority.mapper.dao.SysMenu;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "菜单操作数据")
public class OperateMenuVO {
    @ApiModelProperty(value = "菜单内容")
    SysMenu data;

    public RestResult<OperateMenuVO> success() {
        RestResult<OperateMenuVO> RestResult = new RestResult<>();
        RestResult.setData(this);
        return RestResult;
    }

    public RestResult<OperateMenuVO> error(int status, String message) {
        RestResult<OperateMenuVO> RestResult = new RestResult<>();
        RestResult.setStatus(status);
        RestResult.setMessage(message);
        RestResult.setData(this);
        return RestResult;
    }
}
