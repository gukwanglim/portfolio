package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repositroy.UserRepositroy;

// html 파일이 아니라 data를 return 해주는 Controller = RestController
@RestController
public class DummycontrollerTest {
	
	@Autowired    // 의존성 주입(DI)
	private UserRepositroy UserRepositroy;
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		
		try {
			UserRepositroy.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 id는 DB에 존재하지 않습니다.";
		}	
		
		return "삭제되었습니다. id : " + id;
	}
	
	// email, password를 수정한다면
	@Transactional // 함수 종료 시에 자동 commit 진행
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		System.out.println("id : " + id);
		System.out.println("password : " + requestUser.getPassword());
		System.out.println("email : " + requestUser.getEmail());
		
		// 이 방식을 사용하면 null 값이 들어가게 된다. (@Transactional이 존재하지 않았다.)
//		requestUser.setId(id);
//		requestUser.setUsername("test");		
//		UserRepositroy.save(requestUser);
		
		// save를 이용하여 정보를 수정하기 위해서는 이런 방식으로 코드를 작성해야한다. (@Transactional이 존재하지 않았다.)
//		User user = UserRepositroy.findById(id).orElseThrow(()->{
//			return new IllegalArgumentException("수정에 실패하였습니다.");
//		});
//		user.setPassword(requestUser.getPassword());
//		user.setEmail(requestUser.getEmail());
//		
//		UserRepositroy.save(user);
		
		// 더티 체킹
		// save를 사용하지 않으면 이렇게 작성해야한다. (@Transactional 사용)
		User user = UserRepositroy.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		return user;
	}
	
	// http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/user")
	public List<User> list() {
		return UserRepositroy.findAll();
	}
	
	// 한 페이지 당, 두 건의 데이터를 return
	@GetMapping("/dummy/user/page")
	public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		List<User> users = UserRepositroy.findAll(pageable).getContent();
		return users;
	}
	
	// {id} 주소로 파라메타를 전달 받을 수 있음
	// http://localhost:8000/blog/dummy/user/3 과 같이 들어가게 된다.
//	@GetMapping("/dummy/user/{id}")
//	public User detail(@PathVariable int id) {
//		User user = UserRepositroy.findById(id).orElseGet(new Supplier<User>() {
//			@Override
//			public User get() {
//				return new User();
//			}
//		});
//		return user;
//	}
	
	// IllegalArgumentException 사용
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		User user = UserRepositroy.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
			}
		});
		// 요청 : 웹브라우저
		// user 객체 : java object
		// 변환(웹브라우저가 이해할 수 있는 데이터) -> json
		// spring boot의 MessageConverter 작동
		return user;
	}
	
	// 람다식
//	@GetMapping("/dummy/user/{id}")
//	public User detail(@PathVariable int id) {
//		User user = UserRepositroy.findById(id).orElseThrow(()->{
//			return new IllegalArgumentException("해당 사용자가 없습니다.");
//		});
//		return user;
//	}

	// http://localhost:8000/blog/dummy/join (요청)
	// http의 body에 username, password, email 데이터를 가지고 요청
	@PostMapping("/dummy/join")
	public String join(User user) {
		
		System.out.println("id : " + user.getId());
		System.out.println("username : " + user.getUsername());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		System.out.println("role : " + user.getRole());
		System.out.println("createdate : " + user.getCreateDate());
		
		user.setRole(RoleType.USER);
		UserRepositroy.save(user);
		
		return "회원가입이 완료되었습니다.";	
	}
}
