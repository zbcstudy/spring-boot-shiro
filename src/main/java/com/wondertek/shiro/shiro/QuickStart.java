package com.wondertek.shiro.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * shiro Demo
 */
public class QuickStart {

    public static final Logger log = LoggerFactory.getLogger(QuickStart.class);
    public static void main(String[] args) {

        IniSecurityManagerFactory iniSecurityManagerFactory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager instance = iniSecurityManagerFactory.getInstance();

        SecurityUtils.setSecurityManager(instance);

        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();

        //测试使用session
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "akey");

        String value = (String) session.getAttribute("someKey");

        log.info("----> set the someKey value: " + value);

        //测试当前的用户是否已经被认证,即是否已经登录
        UsernamePasswordToken token = null;
        if (!currentUser.isAuthenticated()) {
            //将用户名和密码封装为UsernamePasswordToken对象
            token = new UsernamePasswordToken("lonestarr", "vespa");
            token.setRememberMe(true);

            try {
                currentUser.login(token);

            } catch (UnknownAccountException e) {
                //用户名错误
                log.info("----> there is no user with username of " + token.getPrincipal());
                return;
            } catch (IncorrectCredentialsException e) {
                //密码错误
                log.info("----> password for account " + token.getPrincipal() + " is incorrect");
                return;
            } catch (LockedAccountException e) {
                //账户被锁定
                log.info("----> the account for username " + token.getPrincipal() + "was locked");
                return;
            }

        }

        log.info("---->"+token.getUsername() + "  logged in success");

        //测试用户是否有角色
        if (currentUser.hasRole("goodguy")) {
            log.info("May has the role with you");
        } else {
            log.info(token.getPrincipal() + " no the role with the goodguy");
            return;
        }

        //test a typed permission (not instance-level)
        // 测试用户是否具备某一个行为. 调用 Subject 的 isPermitted() 方法。
        if (currentUser.isPermitted("lightsaber:weild")) {
            log.info("----> You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        //a (very powerful) Instance Level permission:
        // 测试用户是否具备某一个行为.
        if (currentUser.isPermitted("user:delete:zhangsan")) {
            log.info("----> You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }


        log.info("user is loggin: "+ currentUser.isAuthenticated());

        //登出
        currentUser.logout();

        log.info("user is loggin: "+ currentUser.isAuthenticated());
    }
}
