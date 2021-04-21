package com.security.security.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.security.security.service.JWTService;
import org.springframework.stereotype.Service;
import pojo.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTServiceImpl implements JWTService {
    private static final ThreadLocal<User> userContext = new ThreadLocal<>();
    // 秘钥
    private static final String SECRET = "HEAT_QWR!#$DXE!@$TFSEASDSAF";
    /**
     * 过期时间 单位为秒
     **/
    private static final long EXPIRATION = 60 * 60 * 24 * 7;

    @Override
    public String createToken(User user) {
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        return JWT.create()
                // 添加头部
                .withHeader(map)
                //可以将基本信息放到claims中
                //userId
                .withClaim("id", user.getId())
                //userName
                .withClaim("userName", user.getUsername())
                .withClaim("roleId", user.getRoleId())
                //签发时间
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 校验token并解析token
     */
    @Override
    public Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    @Override
    public User getInfo() {
        return userContext.get();
    }

    @Override
    public void setInfo(User user) {
        userContext.set( user );
    }
}
