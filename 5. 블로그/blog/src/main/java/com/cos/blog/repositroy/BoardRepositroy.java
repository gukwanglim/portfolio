package com.cos.blog.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.Board;

// 자동으로 bean에 등록된다 = @Repositroy를 생략해도 된다.
public interface BoardRepositroy extends JpaRepository<Board, Integer> {
	
}
