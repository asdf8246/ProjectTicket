<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>登入</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">

		<style>
		</style>
	</head>
	<body style="padding: 15px">
	<!-- menu bar include -->
	<%@include file="/WEB-INF/view/menu.jspf" %>

		<div class="profile">
			<form id="login-form" class="pure-form profile-form" method="post" action="/ticket/login">
				<fieldset>
					<legend>登入</legend>
					<c:if test="${not empty error}">
						<p style="color:red">${error}</p>
					</c:if>
					<p><label for="account"></label>
					<input type="text" id="account" name="account" placeholder="請輸入手機或電子郵件" required></p>

					<p><label for="password"></label>
					<input type="password" id="password" name="password" placeholder="請輸入密碼" required></p>

					<p>驗證碼: <img src="<c:url value='/captcha?timestamp=${System.currentTimeMillis()}' />" alt="Captcha Image"></p>
					<p><input type="text" id="captcha" name="captcha" placeholder="請輸入驗證碼" required></p>
					<div class="buttons-container">
						<a href="/ticket/register" class="pure-button">註冊</a>
						<button type="submit" class="pure-button pure-button-primary">登入</button>
					</div>
				</fieldset>
			</form>
		</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
	<script>
		document.getElementById('login-form').addEventListener('submit', function (event) {
		event.preventDefault(); // 防止表單自動提交

		const account = document.getElementById('account').value;
		
		// 定義手機號碼和電子郵件的正則表達式
		const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;  // 電子郵件正則
		const phonePattern = /^(\+?\d{1,4}[\s-]?)?(\(?\d{2,4}\)?[\s-]?)?\d{3,4}[\s-]?\d{3,4}$/;  // 手機號碼正則

		// 檢查輸入是手機號碼還是電子郵件
		if (!emailPattern.test(account) && !phonePattern.test(account)) {
			alert('請輸入有效的手機或電子郵件');
			return;  // 中止提交
		} 

		this.submit(); // 可以解除註解提交表單
		});
	</script>
	</body>
</html>





























