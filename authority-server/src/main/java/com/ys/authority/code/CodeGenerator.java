package com.ys.authority.code;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;


import java.net.URL;
import java.util.Objects;

@Slf4j
public class CodeGenerator {
    //代码生成器
    private static AutoGenerator mpg = new AutoGenerator();

    //全局配置
    private static GlobalConfig gc = new GlobalConfig();

    //作者、包名、去除表前缀
    private static final String author = "mxm";
    private static final String package_name = "com.ys.authority";
    private static final String TABLE_PREFIX = "";

    //数据库
    //private static final String url = "jdbc:mysql://127.0.0.1:3306/authority_manage?useUnicode=true&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String url = "jdbc:mysql://127.0.0.1:3306/yuqing?useUnicode=true&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=CONVERT_TO_NULL";

    private static final String driverName = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "root";
    private static final String password = "root";
    private static final String table_name = "yuqing_trend";

    public static void main(String[] args) {
        // 数据源配置
        setDataSource();

        // 全局配置
        setGlobalConfig();
        setStrategy();

        //执行
        mpg.execute();

        // 策略配置
    }

    private static void setStrategy() {
        StrategyConfig strategy = new StrategyConfig();

        // 类名：Tb_userController -> TbUserController
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 属性名：start_time -> startTime
        //strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // lombok 代替 setter/getter等方法
        strategy.setEntityLombokModel(true);
        // 设置Controller为RestController
        strategy.setRestControllerStyle(true);
        //由数据库该表生成
        strategy.setInclude(table_name);
        //去除表前缀
        strategy.setTablePrefix(TABLE_PREFIX);
        mpg.setStrategy(strategy);
    }

    private static void setGlobalConfig() {
        URL urlPath = Thread.currentThread().getContextClassLoader().getResource("");
        String projectPath = Objects.requireNonNull(urlPath).getPath().replace("target/classes", "src/main/java");

        gc.setOutputDir(projectPath);//代码生成位置
        gc.setFileOverride(true);//覆盖已有文件
        gc.setAuthor(author);
        // gc.setSwagger2(false);
        gc.setIdType(IdType.AUTO);//主键ID类型
        // gc.setDateType(DateType.ONLY_DATE);//设置时间类型为Date
        mpg.setGlobalConfig(gc);
        PackageConfig pc = new PackageConfig();// 包配置
        pc.setParent(package_name);
        mpg.setPackageInfo(pc);
    }

    public static int openSSH(String host, String mysqlHost, int mysqlPort, int localPort, String username, String password) throws Exception {
        JSch jSch = new JSch();
        Session session = jSch.getSession(username, host, 22);
        session.setPassword(Base64Utils.decodeFromString(password));
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        System.out.println(session.getServerVersion());
        int assinged_port = session.setPortForwardingL(localPort, mysqlHost, mysqlPort);
        System.out.println("localhost:" + assinged_port);
        return assinged_port;

    }

    private static void setDataSource() {
        try {
            //openSSH("10.202.253.229", "10.202.233.16", 4346, 3306, "xukaihua", "W1dvQWlKaURvbmdNaW5nMTMxNCFd");
        } catch (Exception e) {
            log.error("", e);
        }

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        dsc.setDriverName(driverName);
        dsc.setUsername(userName);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);
    }
}

