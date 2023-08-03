package com.ys.authority.mapper.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liubizhen
 * @since 2021-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("role_url_white_list")
public class RoleUrlWhiteList implements Serializable {
    public static final String fId = "id";
    public static final String fRoleId = "role_id";
    public static final String fServiceId = "service_id";
    public static final String fUrl = "url";

    private static final long serialVersionUID=1L;

    /**
     * 表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * url地址
     */
    private String url;

    /**
     * 服务id
     */
    private Integer serviceId;


}
