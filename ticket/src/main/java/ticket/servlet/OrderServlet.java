package ticket.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import ticket.model.dto.EventDto;
import ticket.model.dto.OrderDto;
import ticket.model.dto.OrderMessage;
import ticket.model.dto.SeatCategoriesDto;
import ticket.model.dto.UserCert;
import ticket.model.entity.Seats;
import ticket.service.EventService;
import ticket.service.OrderService;
import ticket.service.SeatCategoriesService;
import ticket.service.SeatsService;
import ticket.service.UserService;
import ticket.utils.CheckUser;


@MultipartConfig
@WebServlet(urlPatterns = {"/order/*"})
public class OrderServlet extends HttpServlet{
	private OrderService orderService = new OrderService();
	private EventService eventService = new EventService();
	private SeatCategoriesService seatCategoriesService = new SeatCategoriesService();
	private SeatsService seatsService = new SeatsService();
	private CheckUser checkUser = new CheckUser();
	private UserService userService = new UserService();
	
	private static final String QUEUE_NAME = "order_queue"; // RabbitMQ 隊列名稱
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		HttpSession session = req.getSession();
		UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
		Integer userId = userCert.getUserId();
		String userRole = userCert.getRole();
		Integer login = 0;
		String userName = null;
		// 判斷是否登入
		if (session.getAttribute("userCert")!=null) {
			login = 1;
			userName = userService.getUser(userCert.getUserId()).getUsername();
			userRole = userCert.getRole();
			req.setAttribute("userName", userName);
			req.setAttribute("userRole", userRole);
		}
		req.setAttribute("login", login);
		
		if (pathInfo.equals("/buy")) {
			String eventId = req.getParameter("eventId");
			OrderDto orderDto = orderService.checkUserOrderStatus(userId, eventId);
			
			if (orderDto != null) {
				if (orderDto.getOrderStatus().equals("pending")) {
					resp.setContentType("text/html;charset=UTF-8");
		            resp.getWriter().write("<script type='text/javascript'>");
		            resp.getWriter().write("alert('尚有未付款訂單!');");
		            resp.getWriter().write("window.location.href = '/ticket/user/order';"); // 重新導向回表單頁面
		            resp.getWriter().write("</script>");
			        return;	
				}
				
				if (orderDto.getOrderStatus().equals("paid")) {
					resp.setContentType("text/html;charset=UTF-8");
		            resp.getWriter().write("<script type='text/javascript'>");
		            resp.getWriter().write("alert('已完成票券購買!');");
		            resp.getWriter().write("window.location.href = '/ticket/user/order';"); // 重新導向回表單頁面
		            resp.getWriter().write("</script>");
			        return;	
				}
			}
			
			String sellDate = eventService.getEvent(eventId).getSellDate();
			// 定義時間格式
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        // 將資料庫時間字符串轉換為 LocalDateTime
	        LocalDateTime sellDateTime = LocalDateTime.parse(sellDate, formatter);
	        // 獲取當前時間
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        if (sellDateTime.isAfter(currentDateTime)) {
	        	req.setAttribute("message", "執行錯誤操作!!!");
				req.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(req, resp);
				return;
			}
	        
			List<SeatCategoriesDto> seatCategoriesDto = seatCategoriesService.getSeatCategories(eventId);
			EventDto eventDto = eventService.getEvent(eventId);
			
			
			req.setAttribute("userId", userId.toString());
			req.setAttribute("eventDto", eventDto);
			req.setAttribute("seatCategoriesDto", seatCategoriesDto);
			req.getRequestDispatcher("/WEB-INF/view/order_buy.jsp").forward(req, resp);
			return;
		}
		if (pathInfo.equals("/pay")) {
			String orderId = req.getParameter("orderId");
			OrderDto orderDto = orderService.getOrder(orderId);
			List<OrderDto> orderSeatsDto = orderService.getOrderSeats(orderId);
			
			if (orderDto == null) {
				resp.setContentType("text/html;charset=UTF-8");
	            resp.getWriter().write("<script type='text/javascript'>");
	            resp.getWriter().write("alert('訂單已取消!');");
	            resp.getWriter().write("window.location.href = '/ticket/user/order';"); // 重新導向回表單頁面
	            resp.getWriter().write("</script>");
		        return;
			}
			
			if (!checkUser.checkOrderUser(orderId, userId)) {
				req.setAttribute("message", "執行錯誤操作!!!");
				req.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(req, resp);
				return;
			}
			String eventId = orderDto.getEventId().toString();
			EventDto eventDto = eventService.getEvent(eventId);
			req.setAttribute("eventDto", eventDto);
			req.setAttribute("orderDto", orderDto);
			req.setAttribute("orderSeatsDto", orderSeatsDto);
			req.getRequestDispatcher("/WEB-INF/view/order_pay.jsp").forward(req, resp);
			return;
		}
		if (pathInfo.equals("/delete")) {
			String orderId = req.getParameter("orderId");
			
			if (!checkUser.checkOrderUser(orderId, userId) && !checkUser.checkUserRole(userId, userRole)) {
				req.setAttribute("message", "執行錯誤操作!!!");
				req.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(req, resp);
				return;
			}
			String eventId = req.getParameter("eventId");
			List<OrderDto> orderSeatsDto = orderService.getOrderSeats(orderId);
			String seatStatus = "available";
			seatsService.updateSeatsStatus(orderSeatsDto, seatStatus, eventId);
			orderService.deleteOrder(orderId);
			
			if (eventId == null) {
				resp.sendRedirect("/ticket/user/order");
				return;
			}
			
			resp.sendRedirect("/ticket/event/view?eventId=" + eventId);
			return;
		}
		if (pathInfo.equals("/finish")) {
			String orderId = req.getParameter("orderId");
			
			if (!checkUser.checkOrderUser(orderId, userId)) {
				req.setAttribute("message", "執行錯誤操作!!!");
				req.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(req, resp);
				return;
			}
			
			OrderDto orderDto = orderService.getOrder(orderId);
			String eventId = orderDto.getEventId().toString();
			
			List<OrderDto> orderSeatsDto = orderService.getOrderSeats(orderId);
			String seatStatus = "sold";
			seatsService.updateSeatsStatus(orderSeatsDto, seatStatus, eventId);
			String orderStatus = "paid";
			orderService.updateOrderStatus(orderId, orderStatus);
			resp.sendRedirect("/ticket/user/order");
			return;
		}
		if (pathInfo.equals("/cancel")) {
			String orderId = req.getParameter("orderId");
			
			if (!checkUser.checkOrderUser(orderId, userId) && !checkUser.checkUserRole(userId, userRole)) {
				req.setAttribute("message", "執行錯誤操作!!!");
				req.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(req, resp);
				return;
			}
			
			OrderDto orderDto = orderService.getOrder(orderId);
			String eventId = orderDto.getEventId().toString();
			
			List<OrderDto> orderSeatsDto = orderService.getOrderSeats(orderId);
			String seatStatus = "available";
			seatsService.updateSeatsStatus(orderSeatsDto, seatStatus, eventId);
			String orderStatus = "canceled";
			orderService.updateOrderStatus(orderId, orderStatus);
			resp.sendRedirect("/ticket/user/order");
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String pathInfo = req.getPathInfo();
        	
		HttpSession session = req.getSession();
		UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
		Integer userId = userCert.getUserId();
		
		if (pathInfo.equals("/buy")) {
			       
			 // 處理表單中的所有部分
	        Collection<Part> parts = req.getParts();
	        
	        // 遍歷所有的表單部分
	        for (Part part : parts) {
	            String fieldName = part.getName();
	            String submittedFileName = part.getSubmittedFileName();  // 獲取文件名

	            // 確保是表單中的文件欄位
	            if (submittedFileName != null && !submittedFileName.isEmpty()) {
	                // 處理文件上傳的部分
	                System.out.println("處理文件: " + submittedFileName);
	                // 這裡可以使用 part.getInputStream() 來讀取文件內容
	            } else {
	                // 處理普通的表單字段
	                String fieldValue = req.getParameter(fieldName);
	                System.out.println("Field: " + fieldName + " = " + fieldValue);
	            }
	        }

	        // 例如，您可以從表單中獲取 seatCategoryIds 和 seatPrices
	        String[] seatCategoryIds = req.getParameterValues("seatCategoryIds");
	        String[] seatPrices = req.getParameterValues("seatPrices");
	        String[] numSeatss = req.getParameterValues("numSeatss");
	        String eventId = req.getParameter("eventId");
	        String eventName = req.getParameter("eventName");
			
			
			OrderMessage orderMessage = new OrderMessage(userId.toString(), eventId, eventName, seatPrices, numSeatss, seatCategoryIds);
			
			// 發送消息到 RabbitMQ
	        sendOrderToQueue(orderMessage);
			
	        // 设置响应内容类型为 JSON
	        resp.setContentType("application/json");
	        resp.setCharacterEncoding("UTF-8");

	        // 创建 PrintWriter 对象输出 JSON 数据
	        PrintWriter out = resp.getWriter();
	        out.println("{\"status\": \"success\", \"message\": \"訂單已成功提交，處理中...\"}");
	        out.flush();
	        
			return;
		}
	}
	
	private void sendOrderToQueue(OrderMessage orderMessage) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // 設置 RabbitMQ 服務器的地址
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            // 宣告隊列
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // 序列化訂單消息為 JSON 格式
            String orderMessageJson = new Gson().toJson(orderMessage);

            // 發送消息到隊列
            channel.basicPublish("", QUEUE_NAME, 
                                 new AMQP.BasicProperties.Builder().deliveryMode(2).build(), 
                                 orderMessageJson.getBytes());

            System.out.println("訂單消息已發送到 RabbitMQ: " + orderMessageJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
