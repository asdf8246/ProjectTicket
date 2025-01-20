<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="ticket.model.dto.UserDto" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>會員資訊</title>
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
					<legend>會員資訊</legend>
					<p>帳號: <input type="text" name="username" value="${ userDto.username }" class="updateProfile" readonly /></p>
					<p>手機: <input type="tel" name="phonenumber" value="${ userDto.userPhonenumber }" class="updateProfile" readonly></p>
					<p>電郵: <input type="email" name="email" value="${ userDto.userEmail }" class="updateProfile" readonly /></p>
					<a href="/ticket/user/update" class="button-secondary pure-button" >前往修改</a>
				</fieldset>
			</form>
		</div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
	</body>
</html>