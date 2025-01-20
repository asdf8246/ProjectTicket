<%@ page import="ticket.model.dto.UserDto"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>User</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
	</head>
	<body>
		<!-- menu bar include -->
		<%@include file="/WEB-INF/view/menu.jspf" %>
		
		<div>
			<form class="pure-form" method="post" action="/ticket/user/add">
				<fieldset>
					<legend>User 新增</legend>
					<p>姓名: <input type="text" name="username" placeholder="請輸入姓名" required /></p>
					<p>手機: <input type="tel" name="phonenumber" pattern="09\d{8}" placeholder="例如：0912345678" required></p> 
					<p>密碼: <input type="text" name="password" placeholder="請輸入 password" required /></p> 
					<p>電郵: <input type="email" name="email" placeholder="請輸入 email" required /></p>
					<p>權限: <select name="role">
							<option value="ROLE_ADMIN">ADMIN</option>
							<option value="ROLE_USER">USER</option>
						  </select></p>
					<button type="reset" class="button-warning pure-button">Reset</button>
					<button type="submit" class="button-success pure-button">Submit</button>	  
				</fieldset>
			</form>
			<div class="pure-form">
				<fieldset>
					<legend>User 列表</legend>
					<table class="pure-table pure-table-bordered">
						<thead>
							<tr>
								<th>ID</th><th>姓名</th><th>手機</th><th>郵件</th><th>角色(權限)</th>
								<th>修改</th><th>刪除</th>
							</tr>
						</thead>
						<c:forEach var="userDto" items="${ userDtos }">
							<tr>
								<td>${ userDto.userId }</td>
								<td>${ userDto.username}</td>
								<td>${ userDto.userPhonenumber }</td>
								<td>${ userDto.userEmail }</td>
								<td>${ userDto.userRole }</td>
								<td><a href="/ticket/user/get?userPhonenumber=${ userDto.userPhonenumber }" class="button-secondary pure-button">修改</a></td>
								<td><a href="/ticket/user/delete?userId=${ userDto.userId }" class="button-error pure-button">封鎖</a></td>
							</tr>
						</c:forEach>
					</table>
				</fieldset>
			</div>
		</div>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
	</body>
</html>