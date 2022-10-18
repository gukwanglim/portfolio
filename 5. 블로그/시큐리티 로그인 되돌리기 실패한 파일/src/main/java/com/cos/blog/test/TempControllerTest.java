package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller               // 데이터가 아닌 파일을 return하기 위해서 @Controller 사용
public class TempControllerTest {

	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("tempHome()");
		return "/home.html";
	}
	
	@GetMapping("/temp/jsp")
	public String tempJSP() {
		System.out.println("tempJSP()");
		return "test";
	}
	
}
