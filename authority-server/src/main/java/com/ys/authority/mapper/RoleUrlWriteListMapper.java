package com.ys.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.authority.mapper.dao.RoleUrlWhiteList;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liubizhen
 * @since 2021-05-22
 */
public interface RoleUrlWriteListMapper extends BaseMapper<RoleUrlWhiteList> {
    @Select("SELECT COUNT(id) FROM role_url_white_list WHERE role_id = #{roleId} and service_id = #{serviceId} and url = #{url} ")
    int isSameUrlWhiteList(Integer roleId, Integer serviceId, String url);
}
