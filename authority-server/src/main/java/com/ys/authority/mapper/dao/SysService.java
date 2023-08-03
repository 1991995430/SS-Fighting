package com.ys.authority.mapper.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("sys_service")
public class SysService implements Serializable {
    public static final String fId = "id";
    public static final String fName = "name";
    public static final String fRemark = "remark";
    public static final String fAddUserId = "add_user_id";
    public static final String fAddTime = "add_time";
    public static final String fUpdateTime = "update_time";
    public static final String fStatus = "status";

    private static final long serialVersionUID = 1L;

    /**
     * 业务id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业务名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 添加用户id
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
     * 状态（1生效，0失效）
     */
    private Integer status;
}
