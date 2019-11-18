package com.zbw.big.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbw.big.study.service.AsyncService;

@Controller
public class AsyncController {

	@Autowired
	private AsyncService asyncService;
	
	@RequestMapping("/asyncTask")
	public String asyncTask() {
		asyncService.executeAsync();
		return "success";
	}
}
