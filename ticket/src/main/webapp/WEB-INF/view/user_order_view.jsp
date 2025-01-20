<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->
<%@ taglib uri="jakarta.tags.fmt" prefix="f" %> <!-- 格式化庫 -->
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${ orderDto.eventName } 訂單</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
	</head>
	<body>
	<!-- menu bar include -->
	<%@include file="/WEB-INF/view/menu.jspf" %>
	
	<div class="pure-form">
		<h2>${ orderDto.eventName }</h2>
		<p>
			<a class="" data-bs-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
			顯示活動詳細資料
			</a>
		</p>
		<div class="collapse" id="collapseExample">
			<div class="">
			<p>活動日期: ${ eventDto.eventDate }</p>
			<p>活動地點: ${ eventDto.venue } / ${ eventDto.address }</p>
			</div>
		</div>
		<p>訂單成立: ${ orderDto.orderDate }</p>
	</div>
	<div class="pure-form">
		<fieldset>
			<legend>訂單內容</legend>
			<table class="pure-table pure-table-bordered">
				<thead>
					<tr>
						<th>票種</th><th>序號</th><th>票價</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="orderSeatsDto" items="${ orderSeatsDto }">
					<tr>
						<td>${ orderSeatsDto.categoryName }</td>
						<td>#${ orderSeatsDto.seatNumber }</td>
						<td><f:formatNumber value="${ orderSeatsDto.seatPrice }" type="currency" maxFractionDigits="0" /></td>
					</tr>
				</c:forEach>
					<tr>
						<td colspan="2">票價合計: </td>
						<td><f:formatNumber value="${ orderDto.orderPrice }" type="currency" maxFractionDigits="0" /></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
	</body>
</html>