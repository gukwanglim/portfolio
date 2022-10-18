package com.cos.blog.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.blog.model.User;
import com.cos.blog.repositroy.UserRepositroy;

@Service  // Bean 등록
public class PrincipalDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepositroy userRepositroy;
	
	//스프링이 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데 password 부분 처리는 알아서 하게 된다.
	// 때문에 username이 DB에 존재하는지만 확인해주면 된다.(loadUserByUsername())
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User principal = userRepositroy.findByUsername(username)
				.orElseThrow(()->{
					return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + username);
				});
		return new PrincipalDetail(principal);  // 시큐리티 세션에 유저 정보가 저장
		
	}
}
