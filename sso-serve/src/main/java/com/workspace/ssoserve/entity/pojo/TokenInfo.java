package com.workspace.ssoserve.entity.pojo;

import com.workspace.ssoserve.mapper.dao.SysUser;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TokenInfo {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户基本信息
     */
    private SysUser sysUserInfo;

    /**
     * 当前token结束时间
     */
    private LocalDateTime overTime;

    /**
     * token超时时间，放这里用于刷新token有效期用
     */
    private int timeout;

    /**
     * 用户的User-Agent信息
     */
    private String userAgent;
}
