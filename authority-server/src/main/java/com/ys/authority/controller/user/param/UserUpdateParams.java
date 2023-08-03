package com.ys.authority.controller.user.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.authority.mapper.dao.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel
public class UserUpdateParams implements Serializable {
    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户", required = true)
    private Integer id;

    @ApiModelProperty(value = "登录账号")
    private String name;

    @ApiModelProperty(value = "密码(明文)")
    private String password;

    @ApiModelProperty(value = "用户真实姓名")
    private String trueName;

    @ApiModelProperty(value = "角色id")
    private List<Integer> roleIdList;

    @ApiModelProperty(value = "性别：1男,0女")
    private Integer sex;

    @ApiModelProperty(value = "用户手机号码")
    private String mobileNumber;

    @ApiModelProperty(value = "用户电子邮箱")
    private String email;
    @ApiModelProperty(value = "部门号")
    private  Integer deptId;
    @ApiModelProperty(value = "部门编码")
    private  String deptCode;
    @ApiModelProperty(value = "有效期")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validTime;

    public SysUser buildUserObject() {
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setName(name);
        sysUser.setTrueName(trueName);
        if (password != null && !password.isEmpty()) {
            sysUser.setPassword(password);   // 密码是不是要独立一个修改接口
        }
        sysUser.setSex(sex);
        sysUser.setMobileNumber(mobileNumber);
        sysUser.setEmail(email);
        sysUser.setValidTime(validTime);
        sysUser.setDeptId(deptId);
        sysUser.setDeptCode(deptCode);
        return sysUser;
    }
}
