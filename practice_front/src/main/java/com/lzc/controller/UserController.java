package com.lzc.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//import com.lzc.accout.LoginService;
import com.lzc.user.bean.User;

@Controller
@RequestMapping("/user/")
public class UserController {

	/*@Autowired
	private LoginService loginService;*/

	@RequestMapping("login")
	public String login(String uname,
			String pwd, Model model) {
		//User user = loginService.login(uname, pwd);
		User user =new User();
		if (user != null) {
			model.addAttribute("user", user);
			return "main";
		} else {
			return "login";
		}
		
	}

}
