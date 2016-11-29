package com.lzc.user.service;

import com.lzc.bean.User;

/**
 * 用户信息接口
 * 
 * @author Administrator
 * 
 */
public interface UserService {

	public User getUser(String uname, String pwd);

}
