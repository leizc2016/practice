package com.lzc.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lvmama.pub.DistributedContext;
import com.lzc.accout.LoginService;
import com.lzc.user.bean.User;
import com.lzc.user.service.UserService;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserService userService;

	public User login(String uname, String pwd) {
		System.out.println("LoginServiceImpl get ip:"+DistributedContext.getContext().get("broserIP"));
		System.out.println("call login service,ready to get user...");
		User user = userService.getUser(uname, pwd);
		System.out.println("get user successful!");
		return user;
	}

}
