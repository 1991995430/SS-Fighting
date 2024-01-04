package com.ss.song;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan(basePackages = {"com.ss.song.mapper"})
@EnableAsync
public class BigBangApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BigBangApplication.class, args);
    }

}
