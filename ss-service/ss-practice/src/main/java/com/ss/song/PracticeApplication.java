package com.ss.song;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class},
        scanBasePackages = {"com.ss.song.*"})
@ServletComponentScan
@EnableFeignClients(basePackages = "com.ss.song.*")
@EnableDiscoveryClient
@MapperScan("com.ss.song.mapper")
@EnableAutoDataSourceProxy
public class PracticeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PracticeApplication.class, args);
    }

}
