package com.security.security.config;

import annotation.LogInfo;
import com.security.security.mapper.LogMapper;
import msg.Message;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pojo.Log;
import pojo.User;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @author haya
 */
@Aspect
@Component
public class LogAspect {
    @Resource
    private LogMapper logMapper;
    @Autowired
    private Map<Serializable, Object> pool;

    @Pointcut("@annotation(annotation.LogInfo)")
    public void pointcut() {
    }

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
        // 保存日志
        saveLog( point, time );
        return result;
    }


    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = new Log();
        LogInfo logInfo = method.getAnnotation( LogInfo.class );
        // 注解上的描述
        log.setType( logInfo.type() );
        log.setRemarks( logInfo.remark() );
        log.setOperation( logInfo.value() );
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.setMethod( className + "." + methodName + "()" );
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames( method );
        if (args != null && paramNames != null) {
            String params = "";
            for (int i = 0; i < args.length; i++) {
                params += "  " + paramNames[i] + ": " + args[i];
            }
            log.setParams( params );
        }
        // 获取request
        HttpServletRequest request = getHttpServletRequest();
        // 设置IP地址

        log.setIp( getIpAddr( request ) );
        // 模拟一个用户名
        for (Cookie cookie : request.getCookies()) {
            if ("JSESSIONID".equals( cookie.getName() ) && cookie.getValue() != null){
                if (pool.get( cookie.getValue() ) != null) {
                    System.out.println( pool.get( cookie.getValue() ) );
                    User user = (User) pool.get( cookie.getValue() );
                    log.setUserId( user.getId() );
                }
            }
        }
//        log.setUserId(0);
        log.setTime( time );
        log.setCreateDate( new Date() );
        // 保存系统日志
        logMapper.insert( log );
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
