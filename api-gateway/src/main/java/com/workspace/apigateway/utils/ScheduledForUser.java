package com.workspace.apigateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledForUser {
    /**
     * 每小时做一次汇总统计，存入redis
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void authUserCheck() {
        log.info("Summary statistics work started.");
        // authService.thirdUserCheck();
    }
}
