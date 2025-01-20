<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>新增活動</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
		
		<!-- 引入 flatpickr 样式 -->
   	 	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    	<!-- 引入 flatpickr 脚本 -->
    	<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    	
    	<!-- 台灣縣市二聯式選單 -->
    	<script src="https://cdn.jsdelivr.net/npm/tw-city-selector@2.1.1/dist/tw-city-selector.min.js"></script>
    	
	</head>
	<body>
		<!-- menu bar include -->
		<%@include file="/WEB-INF/view/menu.jspf" %>
		
		<div style="padding: 15px">
			<form id="eventForm" class="pure-form" method="post" enctype="multipart/form-data" action="/ticket/event/add">
				<h2>新增活動</h2>
				<div>
					<div>	
					<label class="input-group-text" for="file" style="width: 90px; margin-left: 50px;">
						選擇圖片<input type="file" name="file" id="file" multiple class="file d-none form-control"
               			accept=".jpg, .jpeg, .png, .gif" onchange="chkfile(this)">
					</label>
					</div>
					<div id="img_errmsg" class="text-danger text-center tw-bold"></div>
            		<div id="img_area" class="text-center"></div>
				</div>
				<div>
					<fieldset>
						<p class="text-start" style="margin-left: 73px;">活動名稱:<input type="text" name="eventName" placeholder="請輸入活動名稱" style="width: 325px" required /></p>
						<p class="text-start" style="margin-left: 73px;"><label for="datetime">售票日期: </label><input type="text" class="datetime" id="sellDate" name="sellDate" style="width: 325px" required /></p>
						<p class="text-start" style="margin-left: 73px;"><label for="datetime">活動日期: </label><input type="text" class="datetime" id="eventDate" name="eventDate" style="width: 325px" required /></p> 
						<p class="text-start" style="margin-left: 73px;">活動地點:<input type="text" name="venue" placeholder="請輸入建物名稱" style="width: 325px" required /></p>
						<div role="tw-city-selector" style="display: flex;flex-direction: row;align-items: stretch;width: 300px;margin-left: 73px;">活動地址:</div>
						<p class="text-start" style="margin-left: 73px;"><input type="text" name="address" style="margin-left: 68px;width: 325px" placeholder="請輸入詳細地址" required></p>
						<p class="text-start" style="margin-left: 73px;">活動簡介:<textarea rows="10" cols="36" name="description"></textarea></p>
						<input type="hidden" name="eventId" value="${ eventDto.eventId }">
					</fieldset>
				</div>
				<h2>座位設定</h2>
				<button onclick="addSeat()" class="button-secondary pure-button">新增欄位</button><p />
				<div>
					<fieldset>
						<table id="seat-table" class="pure-table pure-table-bordered">
    						<thead>
        						<tr>
						           <th>座位等級</th>
						           <th>座位價額</th>
						           <th>座位數量</th>
						           <th>刪除欄位</th>
        						</tr>
   							 </thead>
    						 <tbody>
       							 <tr>
						            <td><input type="text" name="categoryName" style="width: 170px" placeholder="請輸入座位分級/區" required></td>
						            <td><input type="number" name="seatPrice" style="width: 100px" min="0" required></td>
						            <td><input type="number" name="numSeats" style="width: 100px" min="1" required></td>
						            <td></td>
        						</tr>
    						</tbody>
						</table>
					</fieldset>
				</div>
				<button type="reset" class="button-warning pure-button">重置</button>
				<button type="submit" class="button-success pure-button">新增活動</button>
			</form>
		</div>
		
		
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
		<script src="/ticket/js/event.js"></script>
		<script>
   			function addSeat() {
   			    // 取得表格中的 <tbody> 參照
   			    const tableBody = document.querySelector('#seat-table tbody');
   			    
   			    // 複製表格中的最後一行
   			    const lastRow = tableBody.rows[tableBody.rows.length - 1];
   			    const newRow = lastRow.cloneNode(true);  // 複製最後一行

   			    // 清空新複製行中的輸入框內容（這步可選）
   			    const inputs = newRow.querySelectorAll('input');
   			    inputs.forEach(input => input.value = '');
   			    
   				// 清空新複製行中的第4個 <td> 內容（完全清除所有內部元素）
   			    const thirdTd = newRow.cells[3]; // 取得第4個 <td>（即第3個索引）
   			    thirdTd.innerHTML = '';  // 清空第4個 <td> 內的所有內容（包括 <input> 和 <button>）

   			    // 創建刪除按鈕並加到新的行
   			    const deleteButton = document.createElement('button');
   			    deleteButton.textContent = '刪除';
   			    deleteButton.classList.add('delete-btn');
   			    deleteButton.classList.add('button-error', 'pure-button');
   			    deleteButton.onclick = function () {
   			        if (confirm('確定刪除此組座位設定？')) {
   			            tableBody.removeChild(newRow);  // 刪除新增的行
   			        }
   			    };

   			    // 把刪除按鈕加到新行中的最後一個 <td>
   			    newRow.cells[3].appendChild(deleteButton);

   			    // 將新複製的行添加到表格中
   			    tableBody.appendChild(newRow);
   			}
   			 
	   		document.getElementById("eventForm").onsubmit = function() {
	   		    var fileInput = document.getElementById("file");
	   		    if (fileInput.files.length === 0) {
	   		        alert("請選擇一個檔案！");
	   		        return false;  // 阻止表單提交
	   		    }
	   		};
		</script>
	</body>
</html>