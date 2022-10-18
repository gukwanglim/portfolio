package com.cos.blog.repositroy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.User;

// 자동으로 bean에 등록된다 = @Repositroy를 생략해도 된다.
public interface UserRepositroy extends JpaRepository<User, Integer> {
	
	// SELECT * FROM user WHERE username=1?;
	Optional<User> findByUsername(String username);
	
	// JPA Naming 쿼리
	// SELECT * FROM user WHERE username = ?1 AND password = ?2; 와 같이 된다.
	// User findByUsernameAndPassword(String username, String password);
	
	// 또는 아래 방법으로 사용할 수 있다
//	@Query(value = "SELECT * FROM user WHERE username = ?1 AND password = ?2", nativeQuery = true)
//	User login(String username, String password);
}
