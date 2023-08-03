package com.ys.authority.controller.login.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class UserVO {
    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 业务id
     */
    private Integer serviceId;

    /**
     * 用户角色id
     */
    private List<Integer> roleIdList;

    /**
     * 性别：1男,0女
     */
    private Integer sex;

    /**
     * 用户手机号码
     */
    private String mobileNumber;

    /**
     * 用户电子邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态（1生效，0失效）
     */
    private Integer status;

    /**
     * 创建人
     */
    private Integer addUser;
    /**
     * 部门编号
     */
    private Integer deptId;
    /**
     * 部门编码
     */
    private String deptCode;
}
