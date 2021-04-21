package com.security.security.service;

import com.auth0.jwt.interfaces.Claim;
import pojo.User;

import java.util.Map;

public interface JWTService {
    String createToken(User user);

    Map<String, Claim> verifyToken(String token);

    User getInfo();

    void setInfo(User user);
}
