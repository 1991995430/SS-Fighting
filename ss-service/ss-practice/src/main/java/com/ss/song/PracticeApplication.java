package com.ss.song;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class},
        scanBasePackages = {"com.ss.*"})
@ServletComponentScan
@EnableFeignClients(basePackages = "com.ss.*")
@EnableDiscoveryClient
public class PracticeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PracticeApplication.class, args);
    }

}
