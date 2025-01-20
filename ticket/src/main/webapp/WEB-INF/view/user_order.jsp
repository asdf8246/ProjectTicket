<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->
<%@ taglib uri="jakarta.tags.fmt" prefix="f" %> <!-- 格式化庫 -->
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>歷史訂單</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
		
		<!-- 編輯時間格式 -->
		<script src="https://cdn.jsdelivr.net/npm/date-fns@4.1.0/cdn.min.js"></script>
	</head>
	<body>
	<!-- menu bar include -->
	<%@include file="/WEB-INF/view/menu.jspf" %>
	
		<div class="pure-form" style="padding: 15px;">
			<fieldset>
				<legend>歷史訂單</legend>
				<table class="pure-table pure-table-bordered">
					<thead>
						<tr>
							<th>活動名稱</th><th>票券總價</th><th>建立日期</th><th>訂單狀態</th><th></th>
						</tr>
					</thead>
					<tbody id="orderTableBody">
					<c:forEach var="orderDtos" items="${ orderDtos }">
						<tr>
							<td>${ orderDtos.eventName }</td>
							<td><f:formatNumber value="${ orderDtos.orderPrice }" type="currency" maxFractionDigits="0" /></td>
							<td>${ orderDtos.orderDate }</td>
							<td>${ orderDtos.orderStatus }</td>
							<td>
								<c:if test="${ orderDtos.orderStatus == 'pending' }">
									<a href="/ticket/order/pay?orderId=${ orderDtos.orderId }" class="button-secondary pure-button">前往付費</a><br>
								</c:if>
								<c:if test="${ orderDtos.orderStatus != 'pending' }">
									<a href="/ticket/user/order/view?orderId=${ orderDtos.orderId }" class="button-secondary pure-button">查看訂單</a><br>
								</c:if>
									<a href="/ticket/event/view?eventId=${ orderDtos.eventId }" class="button-success pure-button">活動頁面</a><br>
								<!-- 根據 orderStatus 判斷是否顯示刪除訂單按鈕 -->
                        		<c:if test="${ orderDtos.orderStatus == 'paid' }">
									<a href="#" id="cancelOrder" onclick="return confirmCancel('${ orderDtos.orderId }', '${ orderDtos.orderDate }');" class="button-error pure-button">前往退票</a>
								</c:if>
								<c:if test="${ orderDtos.orderStatus == 'pending' }">
									<a href="/ticket/order/delete?orderId=${ orderDtos.orderId }" onclick="return confirmDelete('${ orderDtos.orderId }');" class="button-error pure-button">取消訂單</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</fieldset>
		</div>
		
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
	<script src="/ticket/js/user.js"></script>
	</body>
</html>