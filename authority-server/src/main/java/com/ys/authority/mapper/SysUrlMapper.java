package com.ys.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.authority.mapper.dao.SysUrl;
import org.apache.ibatis.annotations.Select;

public interface SysUrlMapper extends BaseMapper<SysUrl> {
    @Select("SELECT COUNT(id) FROM sys_url WHERE service_id = #{serviceId} and url_item = #{url} and status = 1")
    int isSameUrl(Integer serviceId, String url);
}
