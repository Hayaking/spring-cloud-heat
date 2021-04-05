package com.security.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import pojo.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class JWTUtil {
    private static final ThreadLocal<User> userContext = new ThreadLocal<>();
    // 秘钥
    private static final String SECRET = "HEAT_QWR!#$DXE!@$TFSEASDSAF";
    /**
     * 过期时间 单位为秒
     **/
    private static final long EXPIRATION = 1800L;

    public static String createToken(User user) {
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        return JWT.create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("id", user.getId())//userId
                .withClaim("userName", user.getUsername())//userName
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(new Date()) //签发时间
                .sign( Algorithm.HMAC256(SECRET));
    }

    /**
     * 校验token并解析token
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    public static User getInfo() {
        return userContext.get();
    }

    public static void setInfo(User user) {
        userContext.set( user );
    }

}
