package com.lzc.user.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.lvmama.pub.DistributedContext;
import com.lzc.user.bean.User;
import com.lzc.user.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	public User getUser(String uname, String pwd) {
		System.out.println("UserServiceImpl get ip:"+DistributedContext.getContext().get("broserIP"));
		if (StringUtils.isEmpty(pwd)) {
			return null;
		}

		User user = new User();
		user.setUname(uname);
		user.setPwd(pwd);
		user.setName(uname + "(小明)");
		System.out.println(user.toString());
		return user;
	}

}
