<%@ page import="ticket.model.dto.UserDto"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>修改密碼</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
		
		<style>
		</style>
	</head>
	<body>
	<!-- menu bar include -->
	<%@include file="/WEB-INF/view/menu.jspf" %>
	
		<div style="padding: 15px" class="profile">
			<form class="pure-form profile-form" method="post" action="/ticket/user/update/password">
				<fieldset>
					<legend>修改密碼</legend>
					<c:if test="${not empty error}">
						<p style="color:red">${error}</p>
					</c:if>
					<p>手機: ${ userCert.userPhonenumber } </p> 
					<p>原密碼: <input type="text" name="oldPassword" placeholder="請輸入原密碼" required /></p>
					<p>新密碼: <input type="text" name="newPassword" placeholder="請輸入新密碼" required /></p>
					<div class="buttons-container">
						<button type="reset" class="button-warning pure-button">重置</button>
						<button type="submit" class="button-success pure-button">修改</button>	
					</div>  
				</fieldset>
			</form>
		</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
	</body>
</html>