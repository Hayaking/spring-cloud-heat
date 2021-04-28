package com.consumer.consumer.config;

import annotation.LogInfo;
import com.consumer.consumer.mapper.mysql.LogMapper;
import com.consumer.consumer.service.SecurityClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pojo.Log;
import pojo.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author haya
 */
@EnableAspectJAutoProxy(exposeProxy=true)
@Aspect
@Component
public class LogAspect {
    @Resource
    private LogMapper logMapper;
    @Autowired
    private SecurityClient securityClient;
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2,
            2,
            3,
            TimeUnit.MINUTES,
            new LinkedBlockingDeque<>()
    );

    @Pointcut("@annotation(annotation.LogInfo)")
    public void pointcut() { }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            result = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        User user = securityClient.getUserInfo();
        // 获取request
        HttpServletRequest request = getHttpServletRequest();
        // 保存日志
        pool.execute(()-> {
            saveLog(point, time, user, request);
        });
        return result;
    }


    private void saveLog(ProceedingJoinPoint joinPoint, long time, User user, HttpServletRequest request) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = new Log();
        LogInfo logInfo = method.getAnnotation(LogInfo.class);
        // 注解上的描述
        log.setType(logInfo.type());
        log.setRemarks(logInfo.remark());
        log.setOperation(logInfo.value());
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");
        if (logInfo.getParam()) {
            // 请求的方法参数值
            Object[] args = joinPoint.getArgs();
            // 请求的方法参数名称
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);
            if (args != null && paramNames != null) {
                StringBuilder params = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    params.append("  ").append(paramNames[i]).append(": ").append(args[i]);
                }
                log.setParams(params.toString());
            }
        }

        // 设置IP地址
        log.setIp(getIpAddr(request));
        log.setUserId(user.getId());
        log.setUserName(user.getUsername());
        log.setTime(time);
        log.setCreateDate(new Date());
        logMapper.insert(log);
    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader( "x-forwarded-for" );
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
            ip = request.getHeader( "Proxy-Client-IP" );
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
            ip = request.getHeader( "WL-Proxy-Client-IP" );
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals( ip ) ? "127.0.0.1" : ip;
    }
}
