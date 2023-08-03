package com.ys.authority.jobs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.authority.mapper.SysConfigMapper;
import com.ys.authority.mapper.dao.SysConfig;
import com.ys.authority.service.UrlWhiteListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@EnableScheduling
@Slf4j
@Component
public class UpdateRoleUrlJob {
    @Resource
    SysConfigMapper sysConfigMapper;

    @Resource
    UrlWhiteListService urlWhiteListService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void updateRoleWhiteUrl() {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysConfig.fName, SysConfig.configUpdateRoleUrl);
        queryWrapper.eq(SysConfig.fValue, "1");
        SysConfig config = sysConfigMapper.selectOne(queryWrapper);
        if (config == null) {
            return;
        }
        urlWhiteListService.urlWriteListInit();
        config.setValue("0");
        sysConfigMapper.updateById(config);
    }
}
