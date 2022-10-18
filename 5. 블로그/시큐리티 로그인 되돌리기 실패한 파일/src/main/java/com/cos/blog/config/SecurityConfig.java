package com.cos.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// 빈 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것

@Configuration   // bean 등록(IoC 관리)
@EnableWebSecurity   // 시큐리티 필터 등록 = 스프링 시큐리티가 활성화 되었는데 어떤 설정을 해당 파일에서 하겠다.
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()  // csrf 토큰 비활성화(테스트 시 걸어두는게 좋음)
			.authorizeRequests()          // request가 들어오면
				.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")  // 그것이 /auth/**의 경로를 가지고 있을 경우
				.permitAll()              // 접근을 허용한다.
				.anyRequest()             // 이것이 아닌 다른 요청은
				.authenticated()         // 인증이 되어야 한다.
			.and()
				.formLogin()
				.loginPage("/auth/loginForm");  // 인증이 필요하다면 이곳으로 간다.
//				.loginProcessingUrl("/auth/loginProc")  // 스프링 시큐리티가 해당 주소로 요청오는 login을 가로채서 대신 로그인해준다.
//				.defaultSuccessUrl("/");   // 스프링 시큐리티가 login을 끝내고 해당 주소로 이동
  //				.failureUrl("/fail");     // login이 실패하게 되면 해당 주소로 이동
				
		return http.build();
	}
}

// 시큐리티가 대신 로그인 해주는데 password를 가로채기 때문에 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.
// 하지만 spring 2.7 부터는 이러한 과정을 작성해주지 않고 SecurityFilterChain을 이용하면 문제없이 작동?