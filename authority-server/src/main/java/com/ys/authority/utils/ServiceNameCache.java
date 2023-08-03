package com.ys.authority.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.authority.constant.ConstDB;
import com.ys.authority.mapper.SysServiceMapper;
import com.ys.authority.mapper.dao.SysService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceNameCache {
    @Resource
    private SysServiceMapper sysServiceMapper;

    private Map<Integer, String> serviceNameMap = new HashMap<>();

    public void flushServiceMap() {
        QueryWrapper<SysService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysService.fStatus, ConstDB.RECORD_VALID);
        List<SysService> sysServiceList = sysServiceMapper.selectList(queryWrapper);
        serviceNameMap.clear();
        for (SysService s : sysServiceList) {
            serviceNameMap.put(s.getId(), s.getName());
        }
    }

    public String getServiceName(Integer id) {
        String result = serviceNameMap.get(id);
        if (result == null) {
            result = "Unknown Service " + id.toString();
        }
        return result;
    }
}
