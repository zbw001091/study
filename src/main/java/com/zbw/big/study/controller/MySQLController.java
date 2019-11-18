package com.zbw.big.study.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbw.big.study.dao.User;
import com.zbw.big.study.service.UserService;

@Controller
public class MySQLController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/getAllUsers")
	public String getAllUsers() {
		List<User> list = userService.getAllUsers();
		System.out.println("list.size():" + list.size());
		return "success";
	}
}
