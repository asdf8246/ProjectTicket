<%@ page import="ticket.model.dto.EventDto"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>活動</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
	</head>
	<body>
		<!-- menu bar include -->
		<%@include file="/WEB-INF/view/menu.jspf" %>
		
		<div class="pure-form" style="padding: 15px;max-width: 2000px;">
			<fieldset>
				<legend>活動列表</legend>
				<table class="pure-table pure-table-bordered">
					<thead>
						<tr>
							<th></th><th>活動名稱</th><th>開賣日期</th><th>活動日期</th><th>活動地點</th><th>活動地址</th><th>活動簡介</th><th>活動狀態</th>
							<th></th>
						</tr>
					</thead>
					<c:forEach var="eventDto" items="${ eventDtos }">
						<tr>
							<td class="columnImage"><img src="<c:url value='/image?id=${eventDto.eventId}&timestamp=${System.currentTimeMillis()}' />" alt="Event Image" class="column-image" /></td>
							<td>${ eventDto.eventName }</td>
							<td>${ eventDto.sellDate }</td>
							<td>${ eventDto.eventDate }</td>
							<td>${ eventDto.venue }</td>
							<td>${ eventDto.address }</td>
							<td>${ eventDto.description }</td>
							<td>${ eventDto.eventStatus }</td>
							<td><a href="/ticket/event/get?eventId=${ eventDto.eventId }" class="button-secondary pure-button">修改</a><br>
							<a href="/ticket/event/delete?eventId=${ eventDto.eventId }" class="button-error pure-button">下架</a><br>
							<a href="/ticket/event/view?eventId=${ eventDto.eventId }" class="button-success pure-button">檢視</a></td>
						</tr>
					</c:forEach>
				</table>
			</fieldset>
		</div>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
	</body>
</html>