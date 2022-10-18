//package com.cos.blog.config.auth;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import com.cos.blog.model.User;
//
//// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 오브젝트를 스프링 시큐리티의 고유한 세션 저장소에 저장 해준다.
//public class PrincipalDetail implements UserDetails {
//	
//	private User user;  // 콤포지션(class가 객체를 품고 있는 것)
//	
//	public PrincipalDetail(User user) {
//		this.user = user;
//	}
//
//	@Override
//	public String getPassword() {
//		return user.getPassword();
//	}
//
//	@Override
//	public String getUsername() {
//		return user.getUsername();
//	}
//
//	// 계정이 만료되지 않았는지를 return.(true: 만료X)
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	// 계정의 잠김 상태를 return.(true: 잠기지 않음)
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	// 비밀번호의 만료 상태를 return.(true: 만료X)
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	// 계정이 활성화(사용가능)인지 return.(true: 활성화)
//	@Override
//	public boolean isEnabled() {
//		return true;
//	}
//	
//	// 계정이 가지고 있는 권한 목록을 return.(권한이 여러 개 있을 수 있어서 루프를 돌아야하는데 우리는 한 개만)
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		
//		Collection<GrantedAuthority> collectors = new ArrayList<>();
//		
//		// java는 오브젝트를 담을 순 있지만, 메서드를 바로 사용할 수 없다.(때문에 collectors.add(new GrantedAuthority() {}와 같이 사용)
////		collectors.add(new GrantedAuthority() {
////			
////			@Override
////			public String getAuthority() {
////				// TODO Auto-generated method stub
////				return "ROLE_" + user.getRole();   // ROLE_USER의 형식으로 return 되며 spring에서 role을 받을 때, ROLE_을 사용해야한다(규칙)
////			}
////		});
//		
//		// java에 오브젝트를 담았기 때문에 간단하게 lambda 식으로 사용(GrantedAuthority() 안에는 하나의 메소드만 존재)
//		collectors.add(()->{return "ROEL_" + user.getRole();});
//		
//		return collectors;
//	}
//}
