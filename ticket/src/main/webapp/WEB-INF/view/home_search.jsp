<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <!-- 核心庫 -->
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>搜尋</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css">
		<link rel="stylesheet" href="/ticket/css/buttons.css">
		<link rel="stylesheet" href="/ticket/css/layout.css">
		
		<!-- 編輯時間格式 -->
		<script src="https://cdn.jsdelivr.net/npm/date-fns@4.1.0/cdn.min.js"></script>
	</head>
	<body>
    <section id="sec1">
        <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
            <div class="container-xl">
                <a class="navbar-brand" href="/ticket/home">Ticket</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
                    aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent" style="justify-content: space-between">
                    <div>
                        <ul class="navbar-nav me-auto mb-2 mb-md-0">

                            <li class="nav-item">
                                <a class="nav-link active" aria-current="page" href="#sec1">TOP</a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link text-white" href="/ticket/user/view">會員資訊</a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link text-white" href="/ticket/user/order">歷史訂單</a>
                            </li>

							<c:if test="${ userRole == 'ROLE_ADMIN' }">
								<li class="nav-item dropdown">
									<a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdown" role="button"
										data-bs-toggle="dropdown" aria-expanded="false">
										管理專區
									</a>
									<ul class="dropdown-menu" aria-labelledby="navbarDropdown">
										<li><a class="dropdown-item" href="/ticket/user">會員管理</a></li>
										<li><a class="dropdown-item" href="/ticket/event">活動管理</a></li>
										<li><a class="dropdown-item" href="/ticket/event/add">新增活動</a></li>
									</ul>
								</li>
							</c:if>

                        </ul>
                    </div>

                    <div>
                        <form id="searchForm" class="d-flex" method="post" action="/ticket/home/search">
                            <input id="searchInput" class="form-control me-2" type="search" name="search" placeholder="搜尋活動" aria-label="Search">
                            <button class="btn btn-success" style="display: inline; white-space: nowrap;" type="submit">搜尋</button>
                        </form>
                    </div>

                    <div>
                        <ul class="navbar-nav me-auto mb-2 mb-md-0">
                            
                            <c:if test="${ login == 0 }">
	                            <li class="nav-item">
	                                <a class="nav-link text-white" href="/ticket/login">登入</a>
	                            </li>
							</c:if>
							
							<c:if test="${ login == 1 }">
							
								<li class="nav-item">
	                                <a class="nav-link text-white" href="#">${ userName }</a>
	                            </li>
							
	                            <li class="nav-item">
	                                <a class="nav-link text-white" href="/ticket/logout">登出</a>
	                            </li>
							</c:if>
							
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
    </section>
    
    <header style="width: 100%; background-color: #000;">
	    <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
		  <div class="carousel-indicators">
		    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
		    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
		    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
		  </div>
		  <div class="carousel-inner">
	        <div class="carousel-item active" data-bs-interval="6000">
	            <a href="<c:url value='/event/view?eventId=${mainEventId}' />">
	                <img src="<c:url value='/image?id=${mainEventId}&timestamp=${System.currentTimeMillis()}' />" class="main-image" alt="Event Image" />
	            </a>
	        </div>
	
			<c:forEach var="eventId" items="${randomEventIds}">
			    <div class="carousel-item" data-bs-interval="6000">
			    	<a href="<c:url value='/event/view?eventId=${eventId}' />">
			        	<img src="<c:url value='/image?id=${eventId}&timestamp=${System.currentTimeMillis()}' />" class="main-image" alt="Event Image" />
			    	</a>
			    </div>
			</c:forEach>
		  </div>
		  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
		    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
		    <span class="visually-hidden">Previous</span>
		  </button>
		  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
		    <span class="carousel-control-next-icon" aria-hidden="true"></span>
		    <span class="visually-hidden">Next</span>
		  </button>
		</div>
    </header>
	
	<div class="row">
		<c:forEach var="searchEventDtos" items="${searchEventDtos}">
            <div class="col-lg-3 p-3 card">
                <a href="<c:url value='/event/view?eventId=${searchEventDtos.eventId}' />" class="text-decoration-none">
                    <div class="border rounded h-100">
                        <img src="<c:url value='/image?id=${searchEventDtos.eventId}&timestamp=${System.currentTimeMillis()}' />" class="home-image" alt="Event Image" />
                        <div class="card-body">
                            <h5 class="card-title fw-bold link-dark">${searchEventDtos.eventName}</h5>
                            <p class="card-text"><small class="text-muted"><span id="countdown-${searchEventDtos.eventId}"></span></small></p>
                        </div>
                    </div>
                </a>
            </div>
            <!-- 為每個活動生成倒數計時腳本 -->
		    <script>
		        // 從後端獲取的時間字符串，將每個活動的 sellDate 傳遞給 JavaScript
		        const sellDate_${searchEventDtos.eventId} = "${searchEventDtos.sellDate}";  // 使用每個活動的 sellDate，並給變數加上事件ID後綴
		
		        // 使用 date-fns 解析日期字符串，並轉換為 JavaScript Date 對象
		        const endTime_${searchEventDtos.eventId} = dateFns.parse(sellDate_${searchEventDtos.eventId}, 'yyyy-MM-dd HH:mm:ss', new Date());
		
		        // 取得毫秒數
		        let endtimems_${searchEventDtos.eventId} = endTime_${searchEventDtos.eventId}.getTime();  

		        // 倒數計時函數
		        function calcTime_${searchEventDtos.eventId}(eventId) {
		            let now = new Date();
		            let nowms = now.getTime();
		            let offsetTime = (endtimems_${searchEventDtos.eventId} - nowms) / 1000;
					
					if ('${searchEventDtos.eventStatus}' == '已結束'){
						let timeup = '已過活動時間!';
		                document.querySelector(`#countdown-${searchEventDtos.eventId}`).innerHTML = timeup;
		                clearInterval(timerId_${searchEventDtos.eventId}); // 停止倒數計時
		                return;
					}

		            if (offsetTime <= 0) {
		                let timeup = '票券已開賣!';
		                document.querySelector(`#countdown-${searchEventDtos.eventId}`).innerHTML = timeup;
		                clearInterval(timerId_${searchEventDtos.eventId}); // 停止倒數計時
		                return;
		            }
		
		            let sec = Math.floor(offsetTime % 60);         //秒
		            let min = Math.floor(offsetTime / 60 % 60);    //分
		            let hr = Math.floor(offsetTime / 60 / 60 % 24); //時
		            let day = Math.floor(offsetTime / 60 / 60 / 24); //天
		
		            let result = null;
		            if (day>0) {
		            	result = '距離開賣剩餘 ' + day + ' 天';
					} else if (hr >0) {
						result = '距離開賣剩餘 ' + hr + ' 時';
					} else {
						result = '距離開賣剩餘 ' + min + ' 分';
					}
		            
		            
		            document.querySelector(`#countdown-${searchEventDtos.eventId}`).innerHTML = result;
		        }
		        
		        let timerId_${searchEventDtos.eventId};
		        
		     	// 立即顯示倒數時間
		        calcTime_${searchEventDtos.eventId}(${searchEventDtos.eventId});
		        
		        // 為每個活動設置定時器，使用活動的 ID
		        timerId_${searchEventDtos.eventId} = setInterval(function() {
		            calcTime_${searchEventDtos.eventId}(${searchEventDtos.eventId});
		        }, 60000);
		    </script>
		</c:forEach>
	</div>
	
	
    <!-- 至JSDeliver網站搜Bootstrap5取得 -->	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
	<script src="/ticket/js/home.js"></script>
	</body>
</html>