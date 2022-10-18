package com.cos.blog.model;

import java.security.Timestamp;

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

}
