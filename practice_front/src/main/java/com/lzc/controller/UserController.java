package com.lzc.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//import com.lzc.accout.LoginService;
import com.lzc.bean.User;
import com.lzc.user.service.UserService;

@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("login")
	public String login(String uname,
			String pwd, Model model) {
		User user = userService.getUser(uname, pwd);
		if (user != null) {
			model.addAttribute("user", user);
			System.out.println(user.toString());
			return "main";
		} else {
			return "login";
		}
		
	}

}
