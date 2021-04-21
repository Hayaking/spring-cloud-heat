package com.security.security.config;

import com.auth0.jwt.interfaces.Claim;
import com.security.security.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@WebFilter(filterName = "JwtFilter", urlPatterns = "/*")
public class JWTFilter implements Filter {
    @Autowired
    private JWTService jwtService;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setCharacterEncoding("UTF-8");
        String uri = request.getRequestURI();
        if (uri.startsWith("/base/login")) {
            chain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            if (token == null) {
                response.getWriter().write("没有token！");
                return;
            }

            Map<String, Claim> userData = jwtService.verifyToken(token);
            if (userData == null) {
                response.getWriter().write("token不合法！");
                return;
            }
            Integer id = userData.get("id").asInt();
            String name = userData.get("userName").asString();
            Integer roleId = userData.get("roleId").asInt();
            User user = new User() {{
                setId(id);
                setUsername(name);
                setRoleId(roleId);
            }};
            jwtService.setInfo(user);

        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
