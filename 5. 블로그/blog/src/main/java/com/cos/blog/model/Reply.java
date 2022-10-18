package com.cos.blog.model;


import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Lob  // 대용량 데이터
	private String content;
	
	@ManyToOne   // 여러 개의 답변의 하나의 게시글에 존재할 수 있다
	@JoinColumn(name = "boardId")
	private Board board;
	
	@ManyToOne  // 여러 개의 답변은 한 명의 유저가 쓸 수 있다
	@JoinColumn(name = "userId")
	private User user;
	
	@CreatedDate
	private Timestamp createDate;

	@Override
	public String toString() {
		return "Reply [id=" + id + ", content=" + content + ", board=" + board + ", user=" + user + ", createDate="
				+ createDate + "]";
	}

	// BoardService.java의 댓글쓰기()에서 .builder()를 사용하지 않는 방법
//	public void update(User user, Board board, String content) {
//		setUser(user);
//		setBoard(board);
//		setContent(content);
//	}
	
	

}
