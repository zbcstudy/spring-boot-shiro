package com.wondertek.shiro.service.impl;

import com.wondertek.shiro.dao.UserDao;
import com.wondertek.shiro.model.User;
import com.wondertek.shiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findUserByUserName(String userName) {
        return userDao.findByUserName(userName);
    }
}
