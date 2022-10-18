package com.cos.blog.controller.api;

//import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
//import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

@RestController // 데이터만 return
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	// public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) 대신 사용
//	@Autowired
//	private HttpSession session;
	
//	@PostMapping("/api/user")
	// 스프링 시큐리티 사용(경로에 api/user 대신 auth/joinProc)
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) { // json이므로 @RequestBody 사용
		System.out.println("UserApiController : sava");
		user.setRole(RoleType.USER);
		userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	// 시큐리티 사용을 위해서 주석처리
//	@PostMapping("/api/user/login")
//	public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) {
//		System.out.println("UserApiController : login");
//		User principal = userService.로그인(user);     // principal(접근주체)
//		
//		if(principal != null) {
//			session.setAttribute("principal", principal);
//		}
		
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
}
