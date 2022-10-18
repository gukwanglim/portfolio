package com.cos.blog.controller;

//import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class UserController {
	
	
	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	public UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
//	@GetMapping("user/joinForm")
	// 스프링 시큐리티 사용 -> header.jsp에서 주소 변경 필요
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}

//	@GetMapping("user/loginForm")
	// 스프링 시큐리티 사용 -> header.jsp에서 주소 변경 필요
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm(@AuthenticationPrincipal PrincipalDetail principal) {
		return "user/updateForm";
	}
	
	@GetMapping("/auth/kakao/callback")
//	public @ResponseBody String kakaoCallback(String code) { // 데이터를 return 해주는 컨트롤러 함수
	public String kakaoCallback(String code) { // 아래의 return "redirect:/";를 실행하기 위해 @ResponseBody 삭제
		
		// POST 방식으로 key=value 데이터를 요청(카카오 쪽으로)
		// 이때 필요한 라이브러리는 RestTemplate이다.  (Retrofit2, OkHttp 등등)
		RestTemplate rt = new RestTemplate();
		
		// HttpHeaders 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		// 현재 전송한 http Body 데이터가 key=value 형태라는 것을 알려줌
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		// value는 사실 변수화 시켜서 넣는 것이 좋지만 현재는 그냥 넣는다.
		params.add("grant_type", "authorization_code");
		params.add("client_id", "bed06e49451d9cae93d8276f4329a1ae");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);
		
		// HttpHeaders와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<>(params, headers);
		
		// Http 요청하기(POST 방식), response 변수의 응답을 받음
		ResponseEntity<String> response = rt.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoTokenRequest,
			String.class
		);
		
		// Gson, Json Simple, ObjeckMapper 등의 라이브러리를 사용하여 json 데이터를 담는다.
		// 이 중에 ObjectMapper는 기본으로 내장되어 있음.
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		
		// json 데이터를 java에서 처리하기 위해 java object로 변경한 것
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());	
		
		// POST 방식으로 key=value 데이터를 요청(카카오 쪽으로)하기 위해 RestTemplate 라이브러리 사용.
		RestTemplate rt2 = new RestTemplate();
		
		// HttpHeaders 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		// 현재 전송한 http Body 데이터가 key=value 형태라는 것을 알려줌
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpHeaders와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
				new HttpEntity<>(headers2);
		
		// Http 요청하기(POST 방식), response 변수의 응답을 받음
		ResponseEntity<String> response2 = rt2.exchange(
			"https://kapi.kakao.com/v2/user/me",
			HttpMethod.POST,
			kakaoProfileRequest2,
			String.class
		);
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		
		// json 데이터를 java에서 처리하기 위해 java object로 변경한 것
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("kakao id : " + kakaoProfile.getId());
		System.out.println("kakao email : " + kakaoProfile.getKakao_account().getEmail());
		
		// 카카오로 블로그에 로그인할 때, 필요한 id와 email 정보를 구상
		// password의 경우에는 카카오 로그인이기 때문에 굳이 물어볼 필요성이 없다고 판단
		// 카카오로 로그인 시, 바로 유저 정보로 구성하기 위함
		System.out.println("blog server username : " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
		System.out.println("blog server email : " + kakaoProfile.getKakao_account().getEmail());
		// username과 email만 가지고 로그인이 되는 것을 방지하기 위한 임시 password
//		UUID garbagePassword = UUID.randomUUID(); 
		System.out.println("blog server password : " + cosKey);
		
		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
				.password(cosKey)
				.email(kakaoProfile.getKakao_account().getEmail())
				.oauth("kakao")
				.build();
		
		// 가입자 혹은 비가입자를 체크하여 회원가입을 진행
		User originUser = userService.회원찾기(kakaoUser.getUsername());
		
		// 만약, 해당 사용자가 존재하지 않으면 자동으로 회원가입을 진행
		if(originUser.getUsername() == null) {
			userService.회원가입(kakaoUser);
		}
		
		// 회원가입 혹은 해당 사용자가 이미 존재한다면 자동 로그인 진행(이전의 세션에 로그인 정보 변경하던 코드 사용(UserApiController.java))
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), kakaoUser.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
				
		// 로그인이 완료되면 / 주소로 이동하는데 이때, 위의 @GetMapping("/auth/kakao/callback")에서 
		// @ResponseBody 부분을 지워야 한다.(그래야만 viewResolve를 호출하여 파일을 찾아간다.)
		return "redirect:/";
	}
}