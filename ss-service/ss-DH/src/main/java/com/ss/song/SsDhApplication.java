package com.ss.song;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class},
        scanBasePackages = {"com.ss.song.*"})
@ServletComponentScan
@MapperScan("com.ss.song.mapper")
@EnableAutoDataSourceProxy
public class SsDhApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SsDhApplication.class, args);
    }

}
