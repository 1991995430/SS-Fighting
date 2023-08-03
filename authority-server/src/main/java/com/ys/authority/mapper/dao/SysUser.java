package com.ys.authority.mapper.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author mxm
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser implements Serializable {
    public static final String fId = "id";
    public static final String fName = "name";
    public static final String fTrueName = "true_name";
    public static final String fServiceId = "service_id";
    public static final String fSex = "sex";
    public static final String fMobileNumber = "mobile_number";
    public static final String fEmail = "email";
    public static final String fStatus = "status";
    public static final String fValidTime = "valid_time";
    public static final String fDeptId = "dept_id";
    public static final String fDeptCode = "dept_code";
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 有效期
     */
    private LocalDateTime validTime;

    /**
     * 用户部门id
     */
    private Integer deptId;

    /**
     * 用户部门编码
     */
    private String deptCode;

}
