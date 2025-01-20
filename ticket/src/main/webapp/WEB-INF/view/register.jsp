<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>註冊</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
		
	</head>
	<body style="padding: 15px">
	<!-- menu bar include -->
	<%@include file="/WEB-INF/view/menu.jspf" %>

	<div class="profile">
		<form class="pure-form profile-form" method="post" action="/ticket/register">
			<fieldset>
				<legend>註冊</legend>
				<p>姓名: <input type="text" name="username" placeholder="請輸入姓名" required /></p>
				<p>手機: <input type="tel" name="phonenumber" pattern="09\d{8}" placeholder="例如：0912345678" required></p>
				<p>密碼: <input type="password" name="password" placeholder="請輸入 password" required /></p>
				<p>電郵: <input type="email" name="email" placeholder="請輸入 email" required /></p>
				<div class="buttons-container">
					<button type="reset" class="pure-button">重置</button>
					<button type="submit" class="pure-button pure-button-primary">註冊</button>
				</div>
			</fieldset>
		</form>
	</div>

	</body>
</html>
