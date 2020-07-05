package com.haya.zuul.filter;

import com.haya.zuul.handler.Handler;
import com.haya.zuul.handler.HandlerExecutor;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author haya
 */
//@Component
public class Filter extends ZuulFilter {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HandlerExecutor executor;

    @Override
    public Object run() throws ZuulException {
//        RequestContext context = RequestContext.getCurrentContext();
//        HttpServletRequest request = context.getRequest();
//        String uri = request.getRequestURI();
//        System.out.println(uri);
//        boolean res = executor.exec( uri, context );
//        context.setSendZuulResponse( res );
//        restTemplate.
//        context.setResponseStatusCode( 200 );
        return null;
//        String username = request.getParameter( "username" );// 获取请求的参数
//        if (null != username && username.equals( "chhliu" )) {// 如果请求的参数不为空，且值为chhliu时，则通过
//            ctx.setSendZuulResponse( true );// 对该请求进行路由
//            ctx.setResponseStatusCode( 200 );
//            ctx.set( "isSuccess", true );// 设值，让下一个Filter看到上一个Filter的状态
//            return null;
//        } else {
//            ctx.setSendZuulResponse( false );// 过滤该请求，不对其进行路由
//            ctx.setResponseStatusCode( 401 );// 返回错误码
//            ctx.setResponseBody( "{\"result\":\"username is not correct!\"}" );// 返回错误内容
//            ctx.set( "isSuccess", false );
//            return null;
//        }
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


}
