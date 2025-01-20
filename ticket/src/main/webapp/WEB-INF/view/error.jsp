<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <title>錯誤訊息</title>
	    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
	    <style>
	        body {
	            font-family: Arial, sans-serif;
	            text-align: center;
	            margin-top: 13%;
	        }
	        .message {
	            font-size: 24px;
	            color: red;
	        }
	    </style>
	    <script>
	        // 設定 3 秒後跳轉至首頁
	        setTimeout(function() {
	            window.location.href = '/ticket/home';
	        }, 3000); // 3000 毫秒 = 3 秒
	    </script>
	</head>
	<body>
		<div class="message" style="padding: 15px">
			<h1>
				錯誤訊息: <%=request.getAttribute("message") %>
			</h1>
        	<h2>3 秒後將自動跳轉至首頁...</h2>
			<p><a href="/ticket/home">若無正常跳轉，請點擊此行文字。</a></p>
		</div>
	</body>
</html>