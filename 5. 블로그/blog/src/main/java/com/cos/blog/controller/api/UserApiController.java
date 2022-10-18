package com.cos.blog.controller.api;

//import javax.servlet.http.HttpSession;

//import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

@RestController // 데이터만 return
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) 대신 사용
//	@Autowired
//	private HttpSession session;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) { // json이므로 @RequestBody 사용
		System.out.println("UserApiController : sava");
		userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user) {
		userService.회원수정(user);
		// 여기서는 트랜젝션이 종료되기 때문에 DB의 값은 변경됨.
		// 하지만 세션값은 변경되지 않은 상태로 직접 세션값을 변경해줄 것.
		
		// 세션 등록
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

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


//@PutMapping("/user")
//public ResponseDto<Integer> update(@RequestBody User user,
//		@AuthenticationPrincipal PrincipalDetail principal,
//		HttpSession session) {
//	userService.회원수정(user);
//	// 여기서는 트랜젝션이 종료되기 때문에 DB의 값은 변경됨.
//	// 하지만 세션값은 변경되지 않은 상태로 직접 세션값을 변경해줄 것.
//	
//// 강제로 세션값을 변경
//	// UsernamePasswordAuthenticationToken 생성
//	Authentication authentication  = 
//			new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
//	
//	// SecurityContext 생성
//	SecurityContext securityContext = SecurityContextHolder.getContext();
//	securityContext.setAuthentication(authentication);
//	
//	// 회원 정보 수정 후, 로그인 상태 유지
//	session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
//	return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);