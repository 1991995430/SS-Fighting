package com.ys.authority.mapper.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysConfig implements Serializable {
    public static final String fId = "id";
    public static final String fName = "name";
    public static final String fValue = "value";
    public static final String fChineseName = "chinese_name";

    public static final String configUpdateRoleUrl = "update_role_url";

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String value;

    private String chineseName;
}
