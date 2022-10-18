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
		String rawPassword = user.getPassword();  // 1234 원본
		String encPassword = encoder.encode(rawPassword);  // 해쉬
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepositroy.save(user);
	}
	
	@Transactional(readOnly = true)
	public User 회원찾기(String username) {
		User user = userRepositroy.findByUsername(username)
				.orElseGet(()-> {
					return new User();
				});
		return user;
	}
	
	@Transactional
	public void 회원수정(User user) {
		// 수정 시에는 영속성 컨텍스트에  User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정
		// 영속화를 위해서 User 오브젝트를 DB로부터 가져온다.
		// 영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날려주게 된다. 
		User persistance = userRepositroy.findById(user.getId())
				.orElseThrow(()-> {
					return new IllegalArgumentException("회원 찾기 실패 : 아이디를 찾을 수 없습니다.");
				});
		
		// Vaildate 체크
		if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);
			persistance.setEmail(user.getEmail());
		}
		
		// 회원 수정 종료 시 = service 종료 = 트랜젝션 종료 = 자동으로 commit 실행
		// 영속화된 persistance 객체의 변화가 감지되면 더티체킹이 되어 update문을 날려줌.
	}
	
//	@Transactional(readOnly = true) // select 할 때 트랜젝션 시작, 서비스 종료 시에 트랜젝션 종료(정합성) 
//	public User 로그인(User user) {
//		return userRepositroy.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//	}
}
