package com.cos.blog.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.Reply;

public interface ReplyRepositroy extends JpaRepository<Reply, Integer> {

	@Query(value="INSERT INTO reply(userId, boardId, content, createDate) VALUES(?1, ?2, ?3, now());", nativeQuery = true)
	void mSave(int userId, int boardId, String content);
	
//	// 영상에서 위 코드를 사용하면 오류가 발생하여 다른 방식으로 한 것을 사용
//	@Modifying    // @Modifying는 int 데이터만을 받을 수 있다.
//	@Query(value="INSERT INTO reply(userId, boardId, content, createDate) VALUES(?1, ?2, ?3, now());", nativeQuery = true)
//	int mSave(int userId, int boardId, String content);
//	// 기본적으로 JDBC는 insert가 update나 delete를 실행하게 되면 업데이트 된 행의 개수를 return 해준다.
//	// 만약, 1이 return 된다면 1개가 save, 0이 return 되면 save 없음, -1이 return 되면 오류 발생
}
