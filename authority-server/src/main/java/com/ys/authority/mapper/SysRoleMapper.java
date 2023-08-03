package com.ys.authority.mapper;

import com.ys.authority.mapper.dao.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mxm
 * @since 2020-12-03
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select({"<script>",
            "SELECT COUNT(id)",
            " FROM sys_role",
            " WHERE id IN",
            "<foreach collection='roleIdList' item='id' open='(' separator=',' close=')'>",
            " #{id}",
            "</foreach>",
            " AND admin_flag = 1",
            " AND status = 1",
            "</script>"})
    int isAdmin(@Param("roleIdList") List<Integer> roleIdList);

    @Select("SELECT COUNT(id) FROM sys_role WHERE service_id = #{serviceId} AND name = #{roleName} AND status = 1")
    int isSameRole(Integer serviceId, String roleName);
}
