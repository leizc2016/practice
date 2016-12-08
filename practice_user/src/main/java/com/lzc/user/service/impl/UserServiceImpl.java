package com.lzc.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lzc.bean.User;
import com.lzc.user.dao.UserDao;
import com.lzc.user.service.UserService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private static Log log = LogFactory.getLog(UserService.class);

	@Autowired
	private UserDao userDao;

	public User getUser(String uname, String pwd) {

		log.info("开始获取到用户...");
		User user = userDao.getUser(uname, pwd);
		log.info("获取用户完成！");
		
		return user;

	}

}
