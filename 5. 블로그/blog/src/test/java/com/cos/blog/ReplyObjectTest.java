package com.cos.blog;

import org.junit.jupiter.api.Test;

import com.cos.blog.model.Reply;

public class ReplyObjectTest {
	
	@Test
	public void ToStringTest() {
		Reply reply = Reply.builder()
				.id(1)
				.user(null)
				.board(null)
				.content("toString_test")
				.build();
		
		// 오브젝트 출력 시, toString()이 자동 호출됨. 
		System.out.println(reply);
	}
}
