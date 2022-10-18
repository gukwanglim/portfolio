<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<h3>${reply.content}</h3>

	<button class="btn" onclick="history.back()">���ư���</button>
	<c:if test="${board.user.id == principal.user.id}">
		<a href="/board/${board.id}/updateForm" class="btn ">����</a>
		<button id="btn-delete" class="btn">����</button>
	</c:if>
	<br /> <br />

	<div>
		�� ��ȣ : <span id="id"><i>${board.id}</i></span> �ۼ��� : <span><i>${board.user.username}</i></span>
	</div>
	<br />

	<div class="form-group">
		<label for="title">Title</label>
		<h3>${board.title}</h3>
	</div>
	<hr />

	<div class="form-group">
		<label for="content">Content</label>
		<div>${board.content}</div>
	</div>
	<hr />

	<div class="card">
		<form>
			<input type="hidden" id="userId" value="${principal.user.id}" /> <input type="hidden" id="boardId" value="${board.id}" />
			<div class="card-body">
				<textarea id="reply-content" class="form-control" rows="1"></textarea>
			</div>

			<div class="card-footer">
				<button type="button" id="btn-reply-save" class="btn">���</button>
			</div>

		</form>
	</div>
	<br />

	<div class="card">
		<div class="card-header">��� ����Ʈ</div>

		<ul id="reply--box" class="list-group">

			<c:forEach var="reply" items="${board.reply}">
				<li id="reply--${reply.id}" class="list-group-item d-flex justify-content-between">
					<div>${reply.content}</div>
					<div class="d-flex">
						<div class="font-italic">�ۼ��� : ${reply.user.username} &nbsp</div>
						<c:if test="${reply.user.id == principal.user.id}">
							<button onclick="index.replyDelete(${board.id}, ${reply.id})" class="btn badge">����</button>
						</c:if>
					</div>
				</li>
			</c:forEach>

		</ul>
	</div>

</div>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>
