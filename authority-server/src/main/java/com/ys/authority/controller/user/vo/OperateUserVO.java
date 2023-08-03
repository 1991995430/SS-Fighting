package com.ys.authority.controller.user.vo;

import com.ys.authority.mapper.dao.SysUser;
import com.ys.authority.mapper.dao.SysUserAssociateRole;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "用户对象操作数据")
public class OperateUserVO implements Serializable {
    @ApiModelProperty(value = "服务内容")
    SysUser sysUser;

    @ApiModelProperty(value = "角色内容")
    List<SysUserAssociateRole> userAssociateRoleIDList;

    public RestResult<OperateUserVO> success() {
        RestResult<OperateUserVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }

    public RestResult<OperateUserVO> error(int status, String message) {
        RestResult<OperateUserVO> result = new RestResult<>();
        result.setStatus(status);
        result.setMessage(message);
        result.setData(this);
        return result;
    }
}
