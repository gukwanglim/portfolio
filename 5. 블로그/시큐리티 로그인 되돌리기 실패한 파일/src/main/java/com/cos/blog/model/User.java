package com.cos.blog.model;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@DynamicInsert  -> insert 할 때, null인 필드를 제외해준다
@Entity     // User class가 자동으로 데이터 베이스 테이블에 생성
@EntityListeners(AuditingEntityListener.class)
public class User {

	@Id        // Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)       // 프로젝트에서 연결된  DB의 넘버링 전략을 따라간다.
	private int id;     // 시퀀스, auto_incrememt
	
	@Column(nullable = false, length = 30, unique = true)
	private String username;   // 아이디
	
	@Column(nullable = false, length = 100)  // 해쉬로 변경하여 암호화하기 위해
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	
//	@ColumnDefault("user")
	@Enumerated(EnumType.STRING)
	private RoleType role;    // Enum을 사용하는게 좋다(도메인을 사용하기 위해서) -> admin, user, manager를 분간할 수 있게 된다.
	
	@CreatedDate   // 시간이 자동으로 입력
	private Timestamp createDate;
	
}
