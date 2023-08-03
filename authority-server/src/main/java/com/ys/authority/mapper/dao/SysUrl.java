package com.ys.authority.mapper.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_url")
public class SysUrl {
    public static final String fServiceId = "service_id";
    public static final String fStatus = "status";
    /**
     * URL id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * url内容
     */
    private String urlItem;

    /**
     * 业务id
     */
    private Integer serviceId;

    /**
     * 添加人
     */
    private Integer addUserId;

    /**
     * 添加时间
     */
    private LocalDateTime addTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态（1生效，0失效）
     */
    private Integer status;
}
