package com.demo.foundation.web;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 方法执行日志切面。环绕所有标注 {@link Loggable}、或 Spring MVC 映射注解
 * （{@link PostMapping}/{@link GetMapping}/{@link PutMapping}/{@link DeleteMapping}）的方法，
 * 且未被 {@link Loggdisable} 关闭者，统一打印请求 URL、方法、参数、响应与耗时；
 * 抛异常时以 error 级别记录。{@code log.enable.detail} 控制是否输出参数与响应。
 */
@Aspect
@Component
public class LoggingInterceptor {

    private final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private static final String UNKNOWN_URL = "Unknown URL";

    @Value("${log.enable.detail:true}")
    private boolean enableDetail;

    @Around("(@annotation(Loggable)||" +
            "@annotation(org.springframework.web.bind.annotation.GetMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.PutMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)) && !@annotation(Loggdisable)")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            logRequestAndResponse(joinPoint, result, executionTime);
            return result;
        } catch (Exception ex) {
            long executionTime = System.currentTimeMillis() - startTime;
            logException(joinPoint, ex, executionTime);
            throw ex;
        }
    }

    private Method getInterceptMethod(ProceedingJoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return this.getDeclaredMethod(targetClass, signature);
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
    }

    private void logRequestAndResponse(ProceedingJoinPoint joinPoint, Object result, long executionTime) {
        String methodName = this.getMethodName(joinPoint);
        String requestParams = requestParams(joinPoint);
        String responseResult = (result != null) ? JSON.toJSONString(responseResult(result)) : "No response data";
        String requestUrl = getRequestUrl(joinPoint);
        if (enableDetail) {
            logger.info("LoggingInterceptor: URL: {} | Method: {} | Params: {} | Response: {} | Execution Time: {} ms",
                    requestUrl, methodName, requestParams, responseResult, executionTime);
        } else {
            logger.info("LoggingInterceptor: URL: {} | Method: {} | Execution Time: {} ms",
                    requestUrl, methodName, executionTime);
        }
    }

    /**
     * demo 未引入数据脱敏，响应原样返回（仅排除空值与 HttpServletResponse）。
     */
    private Object responseResult(Object result) {
        if (Objects.isNull(result) || result instanceof HttpServletResponse) {
            return "";
        }
        return result;
    }

    private void logException(ProceedingJoinPoint joinPoint, Exception ex, long executionTime) {
        String methodName = this.getMethodName(joinPoint);
        String requestParams = requestParams(joinPoint);
        String requestUrl = getRequestUrl(joinPoint);
        String errorFormat = "LoggingInterceptor: URL: {} | Exception in method: {} | Params: {} | Execution Time: {} ms| exception: {} ";
        logger.error(errorFormat, requestUrl, methodName, requestParams, executionTime, ex);
    }

    /**
     * 序列化入参：跳过 HttpServletRequest/HttpServletResponse 与 MultipartFile（含列表内）。
     * demo 未引入数据脱敏，参数原样序列化。
     */
    private String requestParams(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        List<Object> objList = new ArrayList<>(args.length);
        for (Object o : args) {
            if (o instanceof HttpServletRequest || o instanceof HttpServletResponse) {
                continue;
            }
            if (o instanceof MultipartFile) {
                continue;
            }
            boolean skip = false;
            if (o instanceof List) {
                for (int i = 0; i < ((List<?>) o).size(); i++) {
                    if (((List<?>) o).get(i) instanceof MultipartFile) {
                        skip = true;
                        break;
                    }
                }
            }
            if (skip) {
                continue;
            }
            objList.add(o);
        }
        return JSON.toJSONString(objList);
    }

    private String getRequestUrl(ProceedingJoinPoint joinPoint) {
        String baseUrl = null;
        try {
            baseUrl = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class).value()[0];
        } catch (Exception e) {
            // ignore：类无 @RequestMapping 时基址为空
        }
        try {
            String url = getMappingUrl(this.getInterceptMethod(joinPoint), baseUrl);
            if (UNKNOWN_URL.equals(url)) {
                // 可能为代理对象，尝试取接口上的映射注解
                Class<?> targetInterface = AopProxyUtils.proxiedUserInterfaces(joinPoint.getTarget())[0];
                Method method = targetInterface.getMethod(joinPoint.getSignature().getName(),
                        ((MethodSignature) joinPoint.getSignature()).getParameterTypes());
                url = getMappingUrl(method, baseUrl);
            }
            return url;
        } catch (Exception e) {
            return UNKNOWN_URL;
        }
    }

    private String getMappingUrl(Method method, String baseUrl) {
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null) {
            return buildUrl(baseUrl, postMapping.value()[0]);
        }
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null) {
            return buildUrl(baseUrl, getMapping.value()[0]);
        }
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (putMapping != null) {
            return buildUrl(baseUrl, putMapping.value()[0]);
        }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) {
            return buildUrl(baseUrl, deleteMapping.value()[0]);
        }
        return UNKNOWN_URL;
    }

    private String buildUrl(String baseUrl, String path) {
        if (StrUtil.isNotBlank(baseUrl)) {
            if (baseUrl.endsWith("/") || path.startsWith("/")) {
                return baseUrl + path;
            }
            return baseUrl + "/" + path;
        }
        return path;
    }

    private Method getDeclaredMethod(Class<?> targetClass, MethodSignature signature) {
        try {
            return targetClass.getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return this.getDeclaredMethod(targetClass.getSuperclass(), signature);
        }
    }
}
