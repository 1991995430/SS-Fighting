package com.ys.authority.aspect;

import com.ys.authority.utils.web.CommonHeaderInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 通用信息设置
 * @author liyj1
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class CommonInfoAspect {
    @Pointcut("execution(* com.ys..*.controller..*.*(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before() {
        //初始化通用header信息。
        CommonHeaderInfoUtil.initInfo();
    }
}
