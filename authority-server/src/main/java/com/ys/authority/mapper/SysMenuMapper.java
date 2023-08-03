package com.ys.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.authority.mapper.dao.SysMenu;
import org.apache.ibatis.annotations.Select;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    @Select("SELECT COUNT(id) FROM sys_menu WHERE service_id = #{serviceId} and menu_item = #{menu} and status = 1")
    int isSameMenu(Integer serviceId, String menu);
}
