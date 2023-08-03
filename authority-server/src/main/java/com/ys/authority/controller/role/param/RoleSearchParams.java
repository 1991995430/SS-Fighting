package com.ys.authority.controller.role.param;

import com.ys.authority.constant.ConstDB;
import com.ys.authority.mapper.dao.SysRole;
import com.ys.common.bean.PageHelperReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class RoleSearchParams extends PageHelperReq {
    @ApiModelProperty(value = "角色ID")
    private Integer id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    public SysRole buildRoleObj() {
        SysRole sysRole = new SysRole();
        sysRole.setId(id);
        sysRole.setName(name);
        sysRole.setStatus(ConstDB.RECORD_VALID);
        return sysRole;
    }
}
