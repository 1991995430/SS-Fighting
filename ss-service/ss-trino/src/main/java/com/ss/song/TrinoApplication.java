package com.ss.song;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(basePackages = {"com.ss.song.mapper"})
public class TrinoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TrinoApplication.class, args);
    }

}
