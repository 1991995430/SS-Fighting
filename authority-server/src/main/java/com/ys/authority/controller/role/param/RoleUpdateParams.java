package com.ys.authority.controller.role.param;

import com.ys.authority.constant.ConstDB;
import com.ys.authority.mapper.dao.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class RoleUpdateParams implements Serializable {
    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty(value = "角色ID", required = true)
    private Integer id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "是否超级用户")
    private Integer adminFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态 1 有效 0无效")
    private Integer status;

    public SysRole buildRoleObject() {
        SysRole sysRole = new SysRole();
        sysRole.setId(id);
        sysRole.setName(name);
        if (adminFlag != null) {
            if (adminFlag == ConstDB.ADMIN_FLAG_NO || adminFlag == ConstDB.ADMIN_FLAG_YES) {
                sysRole.setAdminFlag(adminFlag);
            }
        }
        sysRole.setRemark(remark);
        sysRole.setStatus(status);
        return sysRole;
    }
}
