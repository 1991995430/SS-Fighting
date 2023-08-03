package com.ys.authority.controller.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "用户对象")
public class UserInfoVO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "真实姓名")
    private String trueName;

    @ApiModelProperty(value = "业务id")
    private Integer serviceId;

    @ApiModelProperty(value = "用户角色id")
    private Integer roleId;

    @ApiModelProperty(value = "角色id列表")
    private List<Integer> roleIdList;

    @ApiModelProperty(value = "性别：1男,0女")
    private Integer sex;

    @ApiModelProperty(value = "用户手机号码")
    private String mobileNumber;

    @ApiModelProperty(value = "用户电子邮箱")
    private String email;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "状态（1生效，0失效）")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private Integer addUser;

    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色名称列表")
    private List<String> roleNameList;

    @ApiModelProperty(value = "是否超级管理员")
    private String adminFlag;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty(value = "有效期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validTime;

    @ApiModelProperty(value = "部门号")
    private Integer deptId;

    @ApiModelProperty(value = "部门编号")
    private String deptCode;
}
