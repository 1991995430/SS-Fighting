package com.ys.authority.aspect;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ys.authority.bean.AddLogParam;
import com.ys.authority.utils.RequestUtil;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.LocalDateTime;


/**
 * 日志切面类
 *
 * @author suwenbao
 * @since 2022/8/5
 */
@Aspect
@Component
@Order(3)
@Slf4j
public class LogAspect {

    private final static String GET = "get";

    @Value("${log-server.url}")
    private String URL;

    @Pointcut("execution(* com.ys..*.controller..*.*(..))")
    public void logRecord() {
    }

    // @Around("logRecord()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] objects = proceedingJoinPoint.getArgs();
        AddLogParam addLogParam = setLog(objects, proceedingJoinPoint);
        Object result = proceedingJoinPoint.proceed();
        addLogParam.setEndTime(LocalDateTime.now());
        Duration duration = Duration.between(addLogParam.getBeginTime(), addLogParam.getEndTime());
        addLogParam.setCallTime((int) duration.toMillis());
        if (ObjectUtils.isEmpty(result)) {
            addLogParam.setReturnCode(" ");
        }
        Gson gson = new Gson();
        String resultStr = gson.toJson(result);
        addLogParam.setReturnCode(resultStr);
        //使用RestTemplate发送http请求，请求方式为Post
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(URL, addLogParam, RestResult.class);
        return result;
    }

    private AddLogParam setLog(Object[] objects, ProceedingJoinPoint proceedingJoinPoint) throws UnsupportedEncodingException {
        //获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        assert servletRequestAttributes != null;
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        AddLogParam addLogParam = new AddLogParam();
        addLogParam.setRemoteAddr(RequestUtil.getIpAddr(httpServletRequest));
        addLogParam.setMethod(httpServletRequest.getMethod());
        addLogParam.setRequestUri(httpServletRequest.getRequestURI());
        String message = URLDecoder.decode("userInfo:" + httpServletRequest.getHeader
                ("userInfo"), "UTF-8");
        if (!message.equals("userInfo:null")) {
            JSONObject jsonObject = JSONObject.parseObject(message.substring(9));
            addLogParam.setUserId(Integer.parseInt(jsonObject.getString("id")));
            addLogParam.setUserName(jsonObject.getString("name"));
            addLogParam.setServiceId(Integer.parseInt(jsonObject.getString("serviceId")));
        }
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation log = method.getAnnotation(ApiOperation.class);
            addLogParam.setOperations(log.value());
        }

        if (httpServletRequest.getMethod().equalsIgnoreCase(GET)) {
            addLogParam.setParams(httpServletRequest.getQueryString());
        } else {
            StringBuilder builder = new StringBuilder();
            for (Object obj : objects) {
                builder.append(obj.toString());
            }
            addLogParam.setParams(builder.toString());
        }
        addLogParam.setBeginTime(LocalDateTime.now());

        return addLogParam;
    }
}
