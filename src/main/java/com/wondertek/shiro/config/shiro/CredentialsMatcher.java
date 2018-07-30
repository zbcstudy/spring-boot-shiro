package com.wondertek.shiro.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.context.annotation.Configuration;

/**
 * 密码加密自己实现类，此处未用到
 */
@Configuration
public class CredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        String PassWord = new String(uToken.getPassword());

        String dbPassWord = (String) info.getCredentials();
        return this.equals(PassWord, dbPassWord);
    }
}
