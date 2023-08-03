package com.ys.authority.mapper;

import com.ys.authority.mapper.dao.SysService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mxm
 * @since 2020-12-03
 */
public interface SysServiceMapper extends BaseMapper<SysService> {

    @Select("SELECT COUNT(id) FROM sys_service WHERE id = #{serviceId} and name = '超级服务' and status = 1")
    int isSuperService(Integer serviceId);

    @Select("SELECT COUNT(id) FROM sys_service WHERE name = #{serviceName} and status = 1")
    int isSameService(String serviceName);
}
