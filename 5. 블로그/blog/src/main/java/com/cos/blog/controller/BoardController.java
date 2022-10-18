package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.blog.service.BoardService;

//import com.cos.blog.config.auth.PrincipalDetail;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;

	// /WEB-INF/views/index.jsp를 이용하여 해당 파일을 찾아옴.(application.yml에서 설정)
	// 로그인에 성공한 경우 @AuthenticationPrincipal PrincipalDetail principal을 이용하여 id를 받아올 수 있음.
		// public String index(@AuthenticationPrincipal PrincipalDetail principal) 사용.
	@GetMapping({"", "/"})
	public String index(Model model, @PageableDefault(size = 4, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		model.addAttribute("boards", boardService.글목록(pageable));
//		System.out.println("user id : " + principal.getUsername());
		return "index";
	}
	
	@GetMapping("/board/{id}")
	public String findById(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/detail";
	}
	
	// USER 권한이 필요
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "/board/saveForm";
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/updateForm";
	}
}
