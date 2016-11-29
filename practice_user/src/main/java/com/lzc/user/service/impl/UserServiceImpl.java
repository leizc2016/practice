package com.lzc.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lzc.bean.User;
import com.lzc.user.dao.UserDao;
import com.lzc.user.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	public User getUser(String uname, String pwd) {

		User user = userDao.getUser(uname, pwd);

		return user;

	}

}
