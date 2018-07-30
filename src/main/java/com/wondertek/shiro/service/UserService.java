package com.wondertek.shiro.service;


import com.wondertek.shiro.model.User;

public interface UserService {

    User findUserByUserName(String userName);
}
