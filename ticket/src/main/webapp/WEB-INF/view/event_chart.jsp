<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${ eventDto.eventName }</title>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
		<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
		
		<style>
			#eventChart {
				width: 1400px;
				max-width: 90%;
				height: auto;
				background-color: white;
			}
		</style>
	</head>
	<body>
	<!-- menu bar include -->
	<%@include file="/WEB-INF/view/menu.jspf" %>
		
	<div id="eventChart">
	    <h2>${ eventDto.eventName }</h2>
    	<canvas id="seatChart" width="400" height="200"></canvas>
	</div>
	
	<div class="pure-form" style="max-width: 90%;">
		<fieldset id="seatDetails">
        <legend>座位銷售詳情</legend>
        <table class="pure-table pure-table-bordered">
            <thead>
                <tr>
                    <th>座位類別</th>
                    <th>總座位數</th>
                    <th>已銷售座位數</th>
                    <th>剩餘座位數</th>
                </tr>
            </thead>
            <tbody id="seatDetailsBody">
                <!-- 這裡的內容會在 WebSocket 更新時動態插入 -->
                <c:forEach var="seatCategory" items="${seatCategoriesDto}">
                    <tr>
                        <td>${seatCategory.categoryName}</td>
                        <td>${seatCategory.numSeats}</td>
                        <td>${seatCategory.soldSeats}</td>
                        <td>${seatCategory.numSeats - seatCategory.soldSeats}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </fieldset>
	</div>
	
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
    <script>
 		// 創建 WebSocket 連接
    	var socket = new WebSocket("ws://localhost:8080/ticket/seatDataSocket");
    
        // 從 JSP 中取得資料並轉換成 JavaScript 可以使用的格式
        var categories = [];
        var numSeatsData = [];
        var soldSeatsData = [];
        var remainingSeatsData = []; // 用來存儲剩餘座位數的數據

        // 使用 JSTL 來遍歷 List<SeatCategoriesDto> 並提取資料
        <c:forEach var="seatCategory" items="${seatCategoriesDto}">
            categories.push("${seatCategory.categoryName}");
            numSeatsData.push(${seatCategory.numSeats});
            soldSeatsData.push(${seatCategory.soldSeats});
            
         	// 計算剩餘座位數 (總座位數 - 已銷售座位數)
            remainingSeatsData.push(${seatCategory.numSeats} - ${seatCategory.soldSeats});
        </c:forEach>

        // 使用 Chart.js 繪製柱狀圖
        var ctx = document.getElementById('seatChart').getContext('2d');
        var seatChart = new Chart(ctx, {
            type: 'bar',  // 設定圖表為柱狀圖
            data: {
                labels: categories,  // x 軸的標籤
                datasets: [{
                    label: '剩餘座位數',
                    data: remainingSeatsData,  // 剩餘座位數資料
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',  // 填充顏色
                    borderColor: 'rgba(54, 162, 235, 1)',  // 邊框顏色
                    borderWidth: 1
                }, {
                    label: '已銷售座位數',
                    data: soldSeatsData,  // 已銷售座位數資料
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',  // 填充顏色
                    borderColor: 'rgba(255, 99, 132, 1)',  // 邊框顏色
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true  // y 軸從 0 開始
                    }
                }
            }
        });
        
     	// 當 WebSocket 連線打開時執行
        socket.onopen = function() {
            console.log("WebSocket connection established.");
        };

        // 當 WebSocket 收到來自後端的訊息時更新圖表
        socket.onmessage = function(event) {
            var data = JSON.parse(event.data);
			
            //console.log(data);  // 檢查資料結構
            
            // 假設後端傳來的資料結構包含 categories、numSeats 和 soldSeats
            categories = data.categories;
            numSeatsData = data.numSeats;
            soldSeatsData = data.soldSeats;
			
         	// 計算剩餘座位數
            remainingSeatsData = numSeatsData.map(function(numSeats, index) {
                return numSeats - soldSeatsData[index];  // 計算每個類別的剩餘座位數
            });
            
            // 更新 Chart.js 圖表
            seatChart.data.labels = categories;
            seatChart.data.datasets[0].data = remainingSeatsData;  // 更新剩餘座位數
            seatChart.data.datasets[1].data = soldSeatsData;

            // 重新渲染圖表
            seatChart.update();
            
         	// 更新 fieldset 中的座位詳情表格
            var seatDetailsBody = document.getElementById('seatDetailsBody');
            seatDetailsBody.innerHTML = '';  // 清空表格內容
            
            // 重新填充表格
            for (var i = 0; i < categories.length; i++) {
                var row = document.createElement('tr');

                // 使用 textContent 來插入純文本
                var categoryCell = document.createElement('td');
                categoryCell.textContent = categories[i];
                row.appendChild(categoryCell);

                var numSeatsCell = document.createElement('td');
                numSeatsCell.textContent = numSeatsData[i];
                row.appendChild(numSeatsCell);

                var soldSeatsCell = document.createElement('td');
                soldSeatsCell.textContent = soldSeatsData[i];
                row.appendChild(soldSeatsCell);

                var remainingSeatsCell = document.createElement('td');
                remainingSeatsCell.textContent = remainingSeatsData[i];
                row.appendChild(remainingSeatsCell);

                seatDetailsBody.appendChild(row);
            }
        };

        // 當 WebSocket 連線關閉時執行
        socket.onclose = function() {
            console.log("WebSocket connection closed.");
        };

        // 處理 WebSocket 錯誤
        socket.onerror = function(error) {
            console.error("WebSocket error: " + error);
        };
    </script>
	</body>
</html>