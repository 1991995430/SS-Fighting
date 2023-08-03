package com.ys.authority.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.authority.constant.ConstDB;
import com.ys.authority.mapper.SysRoleMapper;
import com.ys.authority.mapper.dao.SysRole;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SysRoleCache {
    @Resource
    private SysRoleMapper sysRoleMapper;

    private Map<Integer, SysRole> sysRoleMap = new HashMap<>();

    public String getRoleName(Integer id) {
        SysRole sysRole = sysRoleMap.get(id);
        if (sysRole == null) {
            return "Unknown Role " + id.toString();
        }
        return sysRole.getName();
    }

    public String getRoleAdminFlag(Integer id) {
        SysRole sysRole = sysRoleMap.get(id);
        if (sysRole == null) {
            return "Unknown Role " + id.toString();
        }
        return sysRole.getAdminFlag().toString();
    }

    public void flushRoleNameMap() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysRole.fStatus, ConstDB.RECORD_VALID);
        List<SysRole> sysRoleList = sysRoleMapper.selectList(queryWrapper);
        sysRoleMap.clear();
        for (SysRole r : sysRoleList) {
            sysRoleMap.put(r.getId(), r);
        }
    }
}
