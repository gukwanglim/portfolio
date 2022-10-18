package com.cos.blog.model;

import java.security.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
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
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob  // 대용량 데이터
	private String content;   // 섬머노트라는 라이브러리를 사용할 예정. <html>태그가 섞여서 디자인된
	
	@ColumnDefault("0")
	private int conut;   // 조회수
	
	@ManyToOne     // Many = Board, One = User -> 한 명의 user는 여러 개의 board를 쓸 수 있다.
	@JoinColumn(name = "userId")
	private User user; // DB는 오브젝트를 저장할 수 없다
	
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER)  // mappedBy -> 연관관계의 주인이 아니다(FK가 아니다)
	private List<Reply> reply;
	
	@CreatedDate
	private Timestamp createDate;

}
