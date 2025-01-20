package ticket.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ticket.model.dto.EventDto;
import ticket.model.dto.OrderDto;
import ticket.model.dto.UserCert;
import ticket.model.dto.UserDto;
import ticket.service.EventService;
import ticket.service.OrderService;
import ticket.service.UserService;
import ticket.utils.CheckUser;


/**
 request  +-------------+         +-------------+        +---------+
--------> | UserServlet | ------> | UserServise | -----> | UserDao | -----> MySQL(web.users)
          | (Controller)| <-----  |             | <----- |         | <-----
          +-------------+ UserDto +-------------+  User  +---------+
          		 |   	   (Dto)				 (Entity)
          		 |
          		 v
          +-------------+
<-------- |   user.jsp  |
 response |    (View)   |
          +-------------+
          
 查詢所有: GET /user
 查詢單筆: GET /user/get?username=admin
 新增單筆: POST /user/add
 修改單筆: POST /user/update?userId=1
 刪除單筆: GET /user/delete?userId=1 
 修改密碼: GET /user/update/password (得到修改密碼頁面)
 修改密碼: POST /user/update/password (修改密碼處理程序)
          
 */

@WebServlet(urlPatterns = {"/user/*"})
public class UserServlet extends HttpServlet {

	private UserService userService = new UserService();
	private OrderService orderService = new OrderService();
	private CheckUser checkUser = new CheckUser();
	private EventService eventService = new EventService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		//resp.getWriter().print(pathInfo);
		
		HttpSession session = req.getSession();
		UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
		Integer certUserId = userCert.getUserId();
		String certUserRole = userCert.getRole();

		Integer login = 0;
		String userName = null;
		// 判斷是否登入
		if (session.getAttribute("userCert")!=null) {
			login = 1;
			userName = userService.getUser(userCert.getUserId()).getUsername();
			req.setAttribute("userName", userName);
			req.setAttribute("userRole", certUserRole);
		}
		req.setAttribute("login", login);
		
		if (pathInfo == null || pathInfo.equals("/*")) {
			
			if (!checkUser.checkUserRole(certUserId, certUserRole)) {
				resp.sendRedirect("/ticket/home");
				return;
			}
			
			//查詢全部
			List<UserDto> userDtos = userService.findAll();
			//resp.getWriter().print(userDtos);
			// 將必要資料加入到 requset 屬性中以便交由 jsp 進行分析與呈現
			req.setAttribute("userDtos", userDtos);
			// 重導到 user.jsp
			req.getRequestDispatcher("/WEB-INF/view/user.jsp").forward(req, resp);
			return;
		} else if (pathInfo.equals("/delete")) {
			String userId = req.getParameter("userId");
			
			if (!checkUser.checkUserId(userId, certUserId) && !checkUser.checkUserRole(certUserId,certUserRole)) {
				req.setAttribute("message", "執行錯誤操作!!!");
				req.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(req, resp);
				return;
			}
			
			userService.deleteUser(userId);
			
			if (checkUser.checkUserRole(certUserId, certUserRole)) {
				resp.sendRedirect("/ticket/user");
				return;
			}
			
			// 刪除完畢後，重新執行首頁
			// 將 session 失效
			session.invalidate();
			resp.sendRedirect("/ticket/home");
			return;
		} else if (pathInfo.equals("/get")) { //  取得 user 資料並導入到修改頁面
			String userPhonenumber =req.getParameter("userPhonenumber");
			UserDto userDto = userService.getUser(userPhonenumber);
			// 將必要資料加入到 requset 屬性中以便交由 jsp 進行分析與呈現
			req.setAttribute("userDto", userDto);
			// 重導到 user_update.jsp
			req.getRequestDispatcher("/WEB-INF/view/user_update.jsp").forward(req, resp);
			return;
		} else if (pathInfo.equals("/update/password")) { //修改密碼頁面
			req.getRequestDispatcher("/WEB-INF/view/update_password.jsp").forward(req, resp);
			return;
		} else if (pathInfo.equals("/order")) {
			List<OrderDto> orderDtos = orderService.getUserOrders(certUserId);
			
			req.setAttribute("orderDtos", orderDtos);
			req.getRequestDispatcher("/WEB-INF/view/user_order.jsp").forward(req, resp);
			return;
		} else if (pathInfo.equals("/order/view")) {
			String orderId = req.getParameter("orderId");
			List<OrderDto> orderSeatsDto = orderService.getOrderSeats(orderId);
			OrderDto orderDto = orderService.getOrder(orderId);
			
			Integer eventId = orderDto.getEventId();
			EventDto eventDto = eventService.getEvent(eventId.toString());
			
			req.setAttribute("orderSeatsDto", orderSeatsDto);
			req.setAttribute("orderDto", orderDto);
			req.setAttribute("eventDto", eventDto);
			req.getRequestDispatcher("/WEB-INF/view/user_order_view.jsp").forward(req, resp);
			return;
		} else if (pathInfo.equals("/view")) {
			UserDto userDto = userService.getUser(certUserId);
			req.setAttribute("userDto", userDto);
			req.getRequestDispatcher("/WEB-INF/view/user_view.jsp").forward(req, resp);
			return;
		} else if (pathInfo.equals("/update")) { //  取得 user 資料並導入到修改頁面
			UserDto userDto = userService.getUser(certUserId);
			// 將必要資料加入到 requset 屬性中以便交由 jsp 進行分析與呈現
			req.setAttribute("userDto", userDto);
			// 重導到 user_update.jsp
			req.getRequestDispatcher("/WEB-INF/view/user_update.jsp").forward(req, resp);
			return;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String role = req.getParameter("role");
		String phonenumber = req.getParameter("phonenumber");
		String userId = req.getParameter("userId");
		String oldPassword = req.getParameter("oldPassword");
		String newPassword = req.getParameter("newPassword");
		
		switch (pathInfo) {
		case "/add": 
			userService.appendUser(username, phonenumber, password, email, role);
			break;
		case "/update":
			userService.updateUser(userId, username, phonenumber, email, role);
			break;
		case "/update/password":
			// 修改密碼要在已登入的環境下
			HttpSession session = req.getSession();
			UserCert userCert = (UserCert)session.getAttribute("userCert"); // 取得 session 登入憑證
			Integer login = 0;
			String userName = null;
			String userRole = null;
			// 判斷是否登入
			if (session.getAttribute("userCert")!=null) {
				login = 1;
				userName = userService.getUser(userCert.getUserId()).getUsername();
				userRole = userCert.getRole();
				req.setAttribute("userName", userName);
				req.setAttribute("userRole", userRole);
			}
			req.setAttribute("login", login);
			
			try {
				userService.updatePassword(userCert.getUserId(), userCert.getUserPhonenumber(), oldPassword, newPassword);
				req.setAttribute("message", "密碼更新成功");
				req.getRequestDispatcher("/WEB-INF/view/result.jsp").forward(req, resp);
			} catch (Exception e) {
				 req.setAttribute("error", "密碼錯誤!");
		         req.getRequestDispatcher("/WEB-INF/view/update_password.jsp").forward(req, resp);
			}
			return;
		}
		
		// 外重導到指定 URL 網頁
		resp.sendRedirect("/ticket/user");
	}
	
	
}
