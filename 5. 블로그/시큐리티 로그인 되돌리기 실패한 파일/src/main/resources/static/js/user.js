let index = {
	init: function() {
		$("#btn-save").on("click", ()=> {
			this.save();
		});
		
		// 시큐리티를 사용하여 아래 방식의 login을 사요하지 않음
		//$("#btn-login").on("click", ()=> {
		//	this.login();
		//});
	},
	
	save: function() {
		// alert("user의 save 함수 호출됨");
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};
		// console.log(data);
		
		// ajax 통신을 이용하여 3개의 데이터(username, password, email)를 json으로 변경하여 insert 요청
		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data), // http body 데이터
			contentType: "application/json; charset=utf-8", // body 데이터가 어떤 타입인지(MIME)
			dataType: "json" // 요청을 서버로 하여 응답이 왔을 때 기본적으로 대부분이 문자열. 만약, 생긴게 json이라면 javascript 오브젝트로 변경 
		}).done(function(resp) {
			alert("회원가입이 완료되었습니다.");
			// console.log(resp);
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		}); 
	},
	
	// 시큐리티를 사용하여 아래 방식의 login을 사요하지 않음
	//login: function() {
	//	// alert("user의 save 함수 호출됨");
	//	let data = {
	//		username: $("#username").val(),
	//		password: $("#password").val(),
	//	};
		
	//	$.ajax({
	//		type: "POST",
	//		url: "/api/user/login",
	//		data: JSON.stringify(data), // http body 데이터
	//		contentType: "application/json; charset=utf-8", // body 데이터가 어떤 타입인지(MIME)
	//		dataType: "json" // 요청을 서버로 하여 응답이 왔을 때 기본적으로 대부분이 문자열. 만약, 생긴게 json이라면 javascript 오브젝트로 변경 
	//	}).done(function(resp) {
	//		alert("로그인이 완료되었습니다.");
	//		location.href = "/";
	//	}).fail(function(error) {
	//		alert(JSON.stringify(error));
	//	}); 
	//}
}

index.init();