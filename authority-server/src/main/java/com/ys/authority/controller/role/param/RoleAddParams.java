package com.ys.authority.controller.role.param;


import com.ys.authority.constant.ConstDB;
import com.ys.authority.mapper.dao.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class RoleAddParams implements Serializable {
    @NotNull(message = "业务名称不能为空")
    @ApiModelProperty(value = "业务名称", required = true)
    private Integer serviceId;

    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称", required = true)
    private String name;

    @ApiModelProperty(value = "是否超级管理员", required = true)
    private Integer adminFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    public SysRole buildRoleObject() {
        SysRole sysRole = new SysRole();
        sysRole.setName(name);
        sysRole.setAdminFlag(ConstDB.ADMIN_FLAG_NO);
        if (adminFlag != null) {
            if (adminFlag == ConstDB.ADMIN_FLAG_NO || adminFlag == ConstDB.ADMIN_FLAG_YES) {
                sysRole.setAdminFlag(adminFlag);
            }
        }
        sysRole.setServiceId(serviceId);
        sysRole.setRemark(remark);
        return sysRole;
    }
}