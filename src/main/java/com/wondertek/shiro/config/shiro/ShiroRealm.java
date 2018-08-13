package com.wondertek.shiro.config.shiro;

import com.wondertek.shiro.controller.LoginController;
import com.wondertek.shiro.model.Permission;
import com.wondertek.shiro.model.Role;
import com.wondertek.shiro.model.User;
import com.wondertek.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class ShiroRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private UserService userService;

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("-----------first-------------");
        //获取用户输入的token
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        String username = uToken.getUsername();
        User user = userService.findUserByUserName(username);
        log.info("get user: "+ user);
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }

        // 添加盐值加密,盐值必须唯一，在此处使用用户名作为盐值
        ByteSource value = ByteSource.Util.bytes(user.getUserName());

        return new SimpleAuthenticationInfo(user,user.getPassWord(),value,this.getClass().getName());
    }

    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //1 获取登录的用户的信息
//        User user = (User) principals.fromRealm(this.getClass().getName()).iterator().next();
        //2 利用登录的用户信息验证用户的权限（有可能需要查询数据库）
        User principal = (User) principals.getPrimaryPrincipal();
        User loginUser = null;
        if (principal != null) {
            loginUser = userService.findUserByUserName(principal.getUserName());
        }

        List<String> permissions = new ArrayList<>();
        Set<Role> roles = loginUser.getRoleList();
        if (loginUser != null) {
            Set<Role> roleList = loginUser.getRoleList();
            Set<String> roleName = new HashSet<>();
            for (Role role : roleList) {
                roleName.add(role.getRoleName());
            }
            log.info(roleName.toString());
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleName);
            info.addStringPermissions(permissions);

            //返回
            return info;
        }
        log.info("get roles: "+ roles.toString());
//        if (roles.size() > 0) {
//            for (Role role : roles) {
//                Set<Permission> permissionList = role.getPermissionList();
//                for (Permission permission : permissionList) {
//                    permissions.add(permission.getPermissionName());
//                }
//            }
//        }
        for (String permission : permissions) {
            System.out.println(permission);
        }
        //3 创建SimpleAuthorizationInfo，设置其roles属性
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissions);

        //返回
        return info;
    }

    /**
     * 生成密码
     * @param args
     */
    public static void main(String[] args) {
        String hashAlgorithmName = "MD5";
        Object credentials = "123456";
        Object salt = ByteSource.Util.bytes("user");
        int hashIterations = 1;
        Object simpleHash = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(simpleHash);
    }
}
