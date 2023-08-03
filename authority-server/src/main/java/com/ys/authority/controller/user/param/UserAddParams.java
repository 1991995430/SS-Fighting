package com.ys.authority.controller.user.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel
public class UserAddParams implements Serializable {
    @NotBlank(message = "登录账号不能为空")
    @ApiModelProperty(value = "登录账号", required = true)
    private String name;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码(密文)", required = true)
    private String password;

    @NotBlank(message = "真实姓名不能为空")
    @ApiModelProperty(value = "用户真实姓名", required = true)
    private String trueName;

    @NotNull(message = "所属业务不能为空")
    @ApiModelProperty(value = "所属业务id", required = true)
    private Integer serviceId;

    @NotNull(message = "角色不能为空")
    @ApiModelProperty(value = "角色id)", required = true)
    private List<Integer> roleIdList;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(value = "性别：1男,0女", required = true)
    private Integer sex;

    @ApiModelProperty(value = "用户手机号码")
    private String mobileNumber;

    @ApiModelProperty(value = "用户电子邮箱")
    private String email;

    @ApiModelProperty(value = "有效时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validTime;
    @ApiModelProperty(value = "部门编号")
    private Integer deptId;
    @ApiModelProperty(value = "部门编码")
    private String deptCode;

}
