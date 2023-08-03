package com.ys.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.authority.mapper.dao.SysUserAssociateRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserAssociateRoleMapper extends BaseMapper<SysUserAssociateRole> {
    @Insert({"<script>",
            "INSERT INTO ",
            "sys_user_associate_role",
            "(user_id,role_id,add_time,status)",
            "values",
            "<foreach collection='SysUserAssociateRoleList' item='item' separator=','>",
            "(#{item.userId},#{item.roleId},#{item.addTime},#{item.status})",
            "</foreach>",
            "</script>"})
    int insertList(@Param("SysUserAssociateRoleList") List<SysUserAssociateRole> SysUserAssociateRoleList);

}
