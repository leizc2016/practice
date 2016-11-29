package com.lzc.user.dao;

import com.lzc.bean.User;

public interface UserDao {
	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	User getUser(String uname, String pwd);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);
}