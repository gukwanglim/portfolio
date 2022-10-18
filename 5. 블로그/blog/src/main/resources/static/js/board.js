let index = {
	init: function() {
		$("#btn-save").on("click", ()=> {
			this.save();
		});
		
		$("#btn-delete").on("click", ()=> {
			this.deleteById();
		});
		
		$("#btn-update").on("click", ()=> {
			this.update();
		});
		
		$("#btn-reply-save").on("click", ()=> {
			this.replySave();
		});
		
		// 시큐리티를 사용하여 아래 방식의 login을 사용하지 않음
//		$("#btn-login").on("click", ()=> {
//			this.login();
//		});
	},
	
	save: function() {
		// alert("user의 save 함수 호출됨");
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};
		// console.log(data);
		
		// ajax 통신을 이용하여 3개의 데이터(username, password, email)를 json으로 변경하여 insert 요청
		$.ajax({
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data), // http body 데이터
			contentType: "application/json; charset=utf-8", // body 데이터가 어떤 타입인지(MIME) 
			dataType: "json" // 요청을 서버로 하여 응답이 왔을 때 기본적으로 대부분이 문자열. 만약, 생긴게 json이라면 javascript 오브젝트로 변경 
		}).done(function(resp) {
			alert("글쓰기가 완료되었습니다.");
			// console.log(resp);
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		}); 
	},
	
	deleteById: function() {
		var id = $("#id").text();
		
		$.ajax({
			type: "DELETE",
			url: "/api/board/"+id,
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(resp) {
			alert("삭제가 완료되었습니다.");
			// console.log(resp);
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		}); 
	},
	
	update: function() {
		
		let id = $("#id").val();
		
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};

		$.ajax({
			type: "PUT",
			url: "/api/board/"+id,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json" 
		}).done(function(resp) {
			alert("수정 완료되었습니다.");
			// console.log(resp);
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		}); 
	},
	
	replySave: function() {

		let data = {
			// dto를 사용할 때
			userId: $("#userId").val(),
			boardId: $("#boardId").val(),
			content: $("#reply-content").val()
		};
		
		// console.log(data);
		
		// dto를 사용하지 않았을 때
		//let boardId = $("#boardId").val();
		
		$.ajax({
			type: "POST",
			url: `/api/board/${data.boardId}/reply`,
			data: JSON.stringify(data), 
			contentType: "application/json; charset=utf-8",
			dataType: "json" 
		}).done(function(resp) {
			alert("댓글 작성이 완료되었습니다.");
			location.href = `/board/${data.boardId}`;
		}).fail(function(error) {
			alert(JSON.stringify(error));
		}); 
	},
	
	replyDelete: function(boardId, replyId) {
		$.ajax({
			type: "DELETE",
			url: `/api/board/${boardId}/reply/${replyId}`,
			dataType: "json" 
		}).done(function(resp) {
			alert("댓글을 삭제하였습니다.");
			location.href = `/board/${boardId}`;
		}).fail(function(error) {
			alert(JSON.stringify(error));
		}); 
	},
	
	// 시큐리티를 사용하여 아래 방식의 login을 사용하지 않음
//	login: function() {
//		let data = {
//			username: $("#username").val(),
//			password: $("#password").val(),
//		};
//		
//		$.ajax({
//			type: "POST",
//			url: "/api/user/login",
//			data: JSON.stringify(data), // http body 데이터
//			contentType: "application/json; charset=utf-8", // body 데이터가 어떤 타입인지(MIME)
//			dataType: "json" // 요청을 서버로 하여 응답이 왔을 때 기본적으로 대부분이 문자열. 만약, 생긴게 json이라면 javascript 오브젝트로 변경 
//		}).done(function(resp) {
//			alert("로그인이 완료되었습니다.");
//			location.href = "/";
//		}).fail(function(error) {
//			alert(JSON.stringify(error));
//		}); 
//	}
}

index.init();