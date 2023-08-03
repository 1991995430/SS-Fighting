package com.ys.authority.aspect;

import com.ys.authority.constant.ConstWeb;
import com.ys.authority.service.LoginService;
import com.ys.authority.utils.annotation.LoginAuthIgnore;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.ys.common.utils.RestResult;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@Order(2)
public class AuthenticationAspect {

    private final LoginService loginService;

    @Autowired
    public AuthenticationAspect(LoginService loginService) {
        this.loginService = loginService;
    }

    @Pointcut("execution(public * com.ys.authority.controller..*.*(..)))")
    public void loginAuth() {
    }

    @Around("loginAuth()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        if (!this.isAuth(pjp)) {
            return pjp.proceed();
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return RestResult.ret(490, "miss servlet attributes");
        }
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(ConstWeb.HeadField.accessToken);
        log.info("token:{}", token);
        if (loginService.getTokenInfo(token) == null) {
            return RestResult.ret(491, "aspect check token invalid");
        }
        return pjp.proceed();
    }

    /**
     * 判断是否有LoginAuthIgnore 注释
     *
     * @param pjp 处理切点
     * @return 是否需要认证
     */
    private boolean isAuth(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object target = pjp.getTarget();
        log.info("###Class={},method={}", target.getClass().getName(), targetMethod.getName());
        return !targetMethod.isAnnotationPresent(LoginAuthIgnore.class);
    }
}
