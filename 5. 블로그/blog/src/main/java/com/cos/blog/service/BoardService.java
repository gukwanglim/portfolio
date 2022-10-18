package com.cos.blog.service;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
//import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repositroy.BoardRepositroy;
import com.cos.blog.repositroy.ReplyRepositroy;

import lombok.RequiredArgsConstructor;
//import com.cos.blog.repositroy.UserRepositroy;

// spring이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌(IoC를 해준다.)
@Service
@RequiredArgsConstructor
public class BoardService {
	
	// @RequiredArgsConstructor와 final을 사용하여 @Autowired를 대체
	private final BoardRepositroy boardRepositroy;
	private final ReplyRepositroy replyRepositroy;

	// @Autowired의 원리(@Autowired가 동작하는 방식)
//	private BoardRepositroy boardRepositroy;
//	private ReplyRepositroy replyRepositroy;
//	
//	public BoardService(BoardRepositroy bRepo, ReplyRepositroy rRepo) {
//		this.boardRepositroy = bRepo;
//		this.replyRepositroy = rRepo;
//	}

	// @Autowired를 사용
//	@Autowired
//	private BoardRepositroy boardRepositroy;
//	
//	@Autowired
//	private ReplyRepositroy replyRepositroy;
	
//	@Autowired
//	private UserRepositroy userRepositroy;
	
	@Transactional
	public void 글쓰기(Board board, User user) { // title, content만 받아옴
		board.setCount(0);          // count 정보 넣기(직접 입력)
		board.setUser(user);
		boardRepositroy.save(board);
	}
	
	@Transactional(readOnly = true)
	public Page<Board> 글목록(Pageable pageable) {
		return boardRepositroy.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepositroy.findById(id)
				.orElseThrow(()-> {
					return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다.");
				});
	}
	
	@Transactional
	public void 삭제하기(int id) {
		boardRepositroy.deleteById(id);
	}
	
	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepositroy.findById(id)
				.orElseThrow(()-> {
					return new IllegalArgumentException("글 찾기 실패 : 아이디를 찾을 수 없습니다.");
				}); // 영속화 완료
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수로 종료 시(Service가 종료될 때) 트랜젝션이 종료된다. 이때 더티체킹 - 자동 업데이트 진행. DB flush
	}

	
	// dto를 사용하고 영속화는 ReplyRepositroy에서 만든 msave()로 대신 사용할 때
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
		replyRepositroy.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
	}
	
	@Transactional
	public void 댓글삭제(int replyId) {
		replyRepositroy.deleteById(replyId);
	}
	
//	// @Modifying를 사용할 때. return 값 확인해보기
//	@Transactional
//	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
//		int result = replyRepositroy.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
//		System.out.println("BoardService : " + result);
//	}
	
//	// dto를 사용할 때
//	@Transactional
//	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
//		
//		// 영속화
//		User user = userRepositroy.findById(replySaveRequestDto.getUserId())
//				.orElseThrow(()-> {
//					return new IllegalArgumentException("댓글 작성 실패 : 유저 id를 찾을 수 없습니다.");
//				});
//		
//		Board board = boardRepositroy.findById(replySaveRequestDto.getBoardId())
//				.orElseThrow(()-> {
//					return new IllegalArgumentException("댓글 작성 실패 : 게시글 id를 찾을 수 없습니다.");
//				});
//		
//		Reply reply	= Reply.builder()
//				.user(user)
//				.board(board)
//				.content(replySaveRequestDto.getContent())
//				.build();
//
//		// .builder() 사용하지 않고 만들기
////		Reply reply = new Reply();
////		reply.update(user, board, replySaveRequestDto.getContent());
//		
//		replyRepositroy.save(reply);
//	}
	
	// dto를 사용하지 않았을 때
//	@Transactional
//	public void 댓글쓰기(User user, int boardId, Reply requestReply) {
//		
//		Board board = boardRepositroy.findById(boardId)
//				.orElseThrow(()-> {
//					return new IllegalArgumentException("댓글 작성 실패 : 게시글 id를 찾을 수 없습니다.");
//				});
//		
//		requestReply.setUser(user);
//		requestReply.setBoard(board);
//		
//		replyRepositroy.save(requestReply);
//	}
}
