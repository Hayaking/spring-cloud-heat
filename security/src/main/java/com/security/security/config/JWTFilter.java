package com.security.security.config;

import com.auth0.jwt.interfaces.Claim;
import com.security.security.utils.JWTUtil;
import pojo.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "JwtFilter", urlPatterns = "/*")
public class JWTFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        response.setCharacterEncoding("UTF-8");
        String uri = request.getRequestURI();
        if (uri.startsWith( "/login" )) {
            chain.doFilter(request, response);
            return;
        }
        //获取 header里的token
        final String token = request.getHeader("Authorization");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
        }
        // Except OPTIONS, other request should be checked by JWT
        else {
            if (token == null) {
                response.getWriter().write("没有token！");
                return;
            }

            Map<String, Claim> userData = JWTUtil.verifyToken(token);
            if (userData == null) {
                response.getWriter().write("token不合法！");
                return;
            }
            Integer id = userData.get("id").asInt();
            String name = userData.get("userName").asString();
            User user = new User() {{
                setId( id );
                setUsername( name );
            }};
            JWTUtil.setInfo( user );
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
    }
}
