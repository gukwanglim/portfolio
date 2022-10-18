package com.cos.blog.controller;

//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//import com.cos.blog.config.auth.PrincipalDetail;

@Controller
public class BoardController {

	// 로그인에 성공한 경우 @AuthenticationPrincipal PrincipalDetail principal을 이용하여 id를 받아올 수 있음.
		// public String index(@AuthenticationPrincipal PrincipalDetail principal) 사용.
	@GetMapping({"", "/"})
	public String index() {
		// /WEB-INF/views/index.jsp를 이용하여 해당 파일을 찾아옴.(application.yml에서 설정)
//		System.out.println("login id : " + principal.getUsername());
		return "index";
	}
	
}
