package com.lzc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/manager/")
public class ManagerController {

	@RequestMapping("main")
	public String toMain() {

		return "manager/main";

	}

}
