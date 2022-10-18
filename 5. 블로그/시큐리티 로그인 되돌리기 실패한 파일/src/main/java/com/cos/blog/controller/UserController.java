package com.cos.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/**으로 허용
// 그냥 주소가 /이면 index.jsp 허용
// static 이하에 있는 /js/**, /css/**, /image/** 허용

@Controller
public class UserController {
	
//	@GetMapping("user/joinForm")
	// 스프링 시큐리티 사용(경로에 user 대신 auth) -> header.jsp에서 주소 변경 필요
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}

//	@GetMapping("user/loginForm")
	// 스프링 시큐리티 사용(경로에 user 대신 auth) -> header.jsp에서 주소 변경 필요
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
}