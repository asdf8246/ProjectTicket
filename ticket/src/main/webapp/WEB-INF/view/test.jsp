<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:forEach var="seats" items="${ seats }">
		${ seats.seatId }<p />
		${ seats.eventId }<p />
		${ seats.seatCategoryId }<p />
		${ seats.seatNumber }<p />
		${ seats.categoryName }<p />
	</c:forEach>
</body>
</html>