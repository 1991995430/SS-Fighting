package com.ys.authority.controller.role.vo;

import com.ys.authority.mapper.dao.SysRole;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "角色操作数据")
public class OperateRoleVO implements Serializable {
    @ApiModelProperty(value = "角色内容")
    SysRole sysRole;

    public RestResult<OperateRoleVO> success() {
        RestResult<OperateRoleVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }

    public RestResult<OperateRoleVO> error(int status, String message) {
        RestResult<OperateRoleVO> result = new RestResult<>();
        result.setStatus(status);
        result.setMessage(message);
        result.setData(this);
        return result;
    }
}
