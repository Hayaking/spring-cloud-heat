package com.security.security.config.shiro;

import com.security.security.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import pojo.User;

import java.util.HashSet;

/**
 * @author haya
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 登录成功后 获取角色、权限等信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) getAvailablePrincipal( principalCollection );
        return new SimpleAuthorizationInfo() {{
            setRoles( new HashSet<String>() {{
                add( user.getRole().getName() );
            }} );
        }};
    }

    /**
     * 登录验证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //拿到账号 拿到密码
        String name = (String) token.getPrincipal();
        String passWord = new String( (char[]) token.getCredentials() );
        User user = userService.get( name, passWord );
        return user == null ? null : new SimpleAuthenticationInfo( user, user.getPassword(), getName() );
    }
}
