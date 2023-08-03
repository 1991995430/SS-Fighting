package com.ys.authority.mapper.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("role_menu_white_list")
public class RoleMenuWhiteList implements Serializable {
    public static final String fId = "id";
    public static final String fRoleId = "role_id";
    public static final String fServiceId = "service_id";
    public static final String fMenu = "menu";

    private static final long serialVersionUID = 1L;
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
     * 服务id
     */
    private Integer serviceId;

    /**
     * url地址
     */
    private String menu;
}
