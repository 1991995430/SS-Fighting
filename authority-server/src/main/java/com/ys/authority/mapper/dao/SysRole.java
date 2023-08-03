package com.ys.authority.mapper.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("sys_role")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysRole implements Serializable {
    public static final String fId = "id";
    public static final String fServiceId = "service_id";
    public static final String fName = "name";
    public static final String fAddUserId = "add_user_id";
    public static final String fRemark = "remark";
    public static final String fStatus = "status";
    public static final String fDeptId = "dept_id";

    private static final long serialVersionUID = 1L;

    /**
     * 角色id,主键,自动增长
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业务id，创建后不允许修改
     */
    private Integer serviceId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 超级管理员标记，0-不是，1-是
     */
    private Integer adminFlag;

    /**
     * 创建者id
     */
    private Integer addUserId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（1生效，0失效）
     */
    private Integer status;
}
