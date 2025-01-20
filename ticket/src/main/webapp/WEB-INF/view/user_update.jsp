<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="ticket.model.dto.UserDto" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>會員資料修改</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
	</head>
	
	<style>
	</style>
	<body>
	<!-- menu bar include -->
	<%@include file="/WEB-INF/view/menu.jspf" %>
	
		<div style="padding: 15px;" class="profile">
			<form class="pure-form profile-form" method="post" action="/ticket/user/update">
				<fieldset>
					<legend>會員資料修改</legend>
					<p>帳號: <input type="text" name="username" value="${ userDto.username }" required /></p>
					<p class="text-start" style="margin-left: 27px;">密碼:<a href="/ticket/user/update/password" class="pure-button">修改密碼</a></p>
					<p>手機: <input type="tel" name="phonenumber" value="${ userDto.userPhonenumber }" readonly></p>
					<p>電郵: <input type="email" name="email" value="${ userDto.userEmail }" required /></p>
					<c:if test="${ userDto.userRole == 'ROLE_ADMIN' }">
					<p>權限: <select name="role">
							<option value="ROLE_ADMIN" ${ userDto.userRole.equals("ROLE_ADMIN")?"selected":"" }>>ADMIN</option>
							<option value="ROLE_USER" ${ userDto.userRole.equals("ROLE_USER")?"selected":"" }>>USER</option>
						 </select></p>
					</c:if>
					<div class="buttons-container">
						<a href="#" onclick="return deleteUser('${ userDto.userId }');" class="button-error pure-button">註銷</a>
						<button type="submit" class="button-success pure-button" >修改</button>
					</div>
				</fieldset>
			</form>
		</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
	<script src="/ticket/js/user.js"></script>
	</body>
</html>