package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repositroy.UserRepositroy;

// spring이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌(IoC를 해준다.)
@Service
public class UserService {

	@Autowired
	private UserRepositroy userRepositroy;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	public void 회원가입(User user) {
		String rawPassword = user.getPassword();  // 1234 원문
		String encPassword = encoder.encode(rawPassword);  // 해쉬
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepositroy.save(user);
	}
	
//	@Transactional(readOnly = true) // select 할 때 트랜젝션 시작, 서비스 종료 시에 트랜젝션 종료(정합성) 
//	public User 로그인(User user) {
//		return userRepositroy.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//	}
}
