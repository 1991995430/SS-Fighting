package com.workspace.ssomanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan(basePackages = {"com.workspace.ssoserve.mapper"})
@EnableCaching
@EnableOpenApi
@EnableConfigurationProperties
@EnableAsync
@EnableScheduling
@EnableDiscoveryClient
public class SsoManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsoManageApplication.class, args);
    }

}
