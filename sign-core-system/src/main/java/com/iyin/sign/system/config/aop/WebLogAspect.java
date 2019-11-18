package com.iyin.sign.system.config.aop;

import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: WebLogAspect
 * @Description: web日志切面类
 * @Author: 唐德繁
 * @CreateDate: 2018/9/12 0012 下午 12:16
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/9/12 0012 下午 12:16
 * @Version: 0.0.1
 */
@Aspect
@Component
public class WebLogAspect {

    private static Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.iyin.sign.system.controller.*.*(..)) && !execution(public * com.iyin.sign.system.controller.VerifyController.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("URL : {}", request.getRequestURL());
        log.info("HTTP_METHOD : {}", request.getMethod());
        log.info("IP {}: ", request.getRemoteAddr());
        log.info("CLASS_METHOD : {}.{}" , joinPoint.getSignature().getDeclaringTypeName() , joinPoint.getSignature().getName());
        log.info("ARGS : {}" , joinPoint.getArgs());

    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        log.info("RESPONSE : {}" , ret);
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        StopWatch watch = new StopWatch();
        watch.start();
        obj = joinPoint.proceed(args);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        watch.stop();
        log.info("CLASS_METHOD : {} COSTTIME : {}", methodName, watch.getTime());

        return obj;
    }

}
