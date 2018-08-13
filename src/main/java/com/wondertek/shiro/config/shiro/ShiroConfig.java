package com.wondertek.shiro.config.shiro;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * shiro 配置
 */
@Configuration
public class ShiroConfig {

    public static final Logger log = LoggerFactory.getLogger(ShiroConfig.class);

    //配置shiro启动bean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {

        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        //配置登录URL和登录成功的URL
        bean.setLoginUrl("/login");
        bean.setSuccessUrl("/home");
        bean.setUnauthorizedUrl("/unauthorized");
        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/loginUser", "anon");
        filterChainDefinitionMap.put("/logout*", "anon");
        filterChainDefinitionMap.put("/error","anon");
        filterChainDefinitionMap.put("/logout", "logout"); //登出操作
        filterChainDefinitionMap.put("/index","authc");//表示需要认证才可以访问
        filterChainDefinitionMap.put("/user","authc, roles[user]");
        filterChainDefinitionMap.put("/admin","authc, roles[admin]");
        filterChainDefinitionMap.put("/*", "authc");
        filterChainDefinitionMap.put("/**", "authc");
        filterChainDefinitionMap.put("/*.*", "authc");

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    @Bean
    public SecurityManager securityManager(ShiroRealm shiroReam) {
        log.info("-------> shiro已加载");
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        //shiroRealm设置密码加密方式
        shiroReam.setCredentialsMatcher(credentialsMatcher());
//        manager.setAuthenticator(modularRealmAuthenticator());
        //添加多个realm认证
        List<Realm> realms = new ArrayList<>();
        realms.add(shiroRealm());
//        realms.add(secondShiroRealm());
        manager.setRealms(realms);
        manager.setCacheManager(ehCacheManager());
        return manager;
    }


    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager manager = new EhCacheManager();
//        manager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return manager;
    }

    /**
     * 多个realm的认证策略
     * @return
     */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
//        List<Realm> realms = new ArrayList<>();
//        realms.add(shiroRealm());
//        realms.add(secondShiroRealm());
//        authenticator.setRealms(realms);
        return authenticator;
    }

    /**
     * 配置shiro 的realm
     * @return
     */
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm realm = new ShiroRealm();
        realm.setCredentialsMatcher(credentialsMatcher());
        return realm;
    }

    @Bean
    public SecondShiroRealm secondShiroRealm() {
        SecondShiroRealm secondShiroRealm = new SecondShiroRealm();
        secondShiroRealm.setCredentialsMatcher(credentialsMatcherSHA1());
        return secondShiroRealm;
    }

    /**
     * 密码加密方式
     * Md5 加密
     * @return
     */
    @Bean(name="credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("MD5");
        //加密次数，默认为1
        matcher.setHashIterations(1);
        return matcher;
    }

    @Bean(name="credentialsMatcherSHA1")
    public CredentialsMatcher credentialsMatcherSHA1() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("SHA1");
        //加密次数，默认为1
        matcher.setHashIterations(1);
        return matcher;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
 }
