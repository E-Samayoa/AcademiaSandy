package com.academiasi.spring.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping({"/index","/","/home"})
	public String Index() {
		return"index";
	}
	
	@GetMapping("/eventos")
	public String eventos() {
		return"eventos";
	}

}
