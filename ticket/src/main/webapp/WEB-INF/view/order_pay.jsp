<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->
<%@ taglib uri="jakarta.tags.fmt" prefix="f" %> <!-- 格式化庫 -->
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${ orderDto.eventName }</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
		
		<!-- 編輯時間格式 -->
		<script src="https://cdn.jsdelivr.net/npm/date-fns@4.1.0/cdn.min.js"></script>
		<style>
		</style>
	</head>
	<body>
		<!-- menu bar include -->
		<%@include file="/WEB-INF/view/menu.jspf" %>
		
		<div class="pure-form" style="padding: 15px;">
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
			<p>訂單成立: ${ orderDto.orderDate }<p />
			<p class="text-white bg-danger text-center">您的訂單已保留，請在 <span id="countdown"></span> 內完成填寫資料並確認訂單。若超過時限，系統將自動取消。</p>
		</div>
		<div class="pure-form" style="padding: 15px;">
			<h3>購票總覽</h3>
			<fieldset>
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
					<input type="hidden" name="seatIds" value="${ orderSeatsDto.seatId }">
					</c:forEach>
						<tr>
							<td colspan="2">票價合計: </td>
							<td><f:formatNumber value="${ orderDto.orderPrice }" type="currency" maxFractionDigits="0" /></td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="pure-form" style="padding: 15px;">
			<a href="/ticket/order/delete?orderId=${ orderDto.orderId }&eventId=${ orderDto.eventId }" onclick="return confirmDelete('${ orderDto.orderId }','${ orderDto.eventId }');" class="button-error pure-button">取消</a>
			<a href="/ticket/order/finish?orderId=${ orderDto.orderId }" class="button-secondary pure-button">付款</a>
		</div>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
	<script>
	// 从后端获取的时间字符串
	const orderDate = "${ orderDto.orderDate }"  // 示例数据（可以替换成从数据库获得的时间）
	// 使用 date-fns 解析日期字符串，并转换为 JavaScript Date 对象
	const endTime = dateFns.parse(orderDate, 'yyyy-MM-dd HH:mm:ss', new Date());
	//console.log(endTime);  // 打印解析后的日期
	let endtimems = endTime.getTime() + 600000;  // 获取毫秒数
	//console.log(endtimems);

	//倒數計時
	function calcTime(){
	  let now = new Date();
	  //console.log(now);
	  let nowms = now.getTime();
	  //console.log(nowms);
	  let offsetTime = (endtimems - nowms) /1000;
	  //console.log(offsetTime);
	  
	  if (offsetTime <= 0) {
	      clearInterval(timerId);  // 使用定时器 ID 清除定时器
	      alert('訂單已取消！');
	      window.location.replace("/ticket/order/delete?orderId=${ orderDto.orderId }&eventId=${ orderDto.eventId }");
	      return;
	  }
	  
	  let sec = Math.floor(offsetTime % 60);         //秒
	  let min = Math.floor(offsetTime /60 % 60);     //分
	  let hr  = Math.floor(offsetTime /60 /60 % 24); //時
	  let day = Math.floor(offsetTime /60 /60 / 24); //天

	  if (sec < 10) { sec = '0' + sec;}
	  if (min < 10) { min = '0' + min;}
	  if (hr  < 10) { hr  = '0' + hr ;}
	  if (day < 10) { day = '0' + day;}

	  let result =  min + ' : ' + sec ;

	  document.querySelector('#countdown').innerHTML = result;
	}

	 // 使用 setInterval，每秒钟调用 calcTime
	let timerId = setInterval(calcTime, 1000);

	// 確認刪除操作的函數
	function confirmDelete(orderId,eventId) {
		// 顯示確認對話框
		const confirmation = confirm("確定要取消訂單嗎？取消後將不會保留。");

		if (confirmation) {
			// 使用 AJAX 發送刪除請求
			var xhr = new XMLHttpRequest();
			xhr.open("GET", '/ticket/order/delete?orderId=' + orderId + '&eventId=' + eventId, true);
			
			// 當請求完成後的回調函數
			xhr.onload = function() {
				if (xhr.status === 200) {
					alert("訂單已取消");
					// 這裡會觸發跳轉到原來的鏈接
					window.location.href = '/ticket/event/view?eventId=' + eventId;
				} else {
					alert("取消訂單失敗！");
				}
			};
			xhr.send();  // 發送請求
		}

		// 如果用戶選擇「取消」，則返回 false，阻止鏈接的默認行為（避免跳轉）
		return false;
	}
	</script>
	</body>
</html>