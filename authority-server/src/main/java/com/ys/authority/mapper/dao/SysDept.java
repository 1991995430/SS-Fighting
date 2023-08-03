package com.ys.authority.mapper.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dept")
public class SysDept implements Serializable {
    public static final String fDeptId = "id";
    public static final String fDeptName = "name";
    public static final String fDeptCode = "code";
    public static final String fAddTime = "add_time";
    public static final String fDepth = "depth";
    public static final String fDescription = "description";

    private static final long serialVersionUID = 1L;

    /**
      部门id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 部门编码
     */
    private String code;
    /**
     * 添加时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addTime;
    /**
     * 部门所在层级
     * */
    private  Integer depth;
    private  String description;
}
