package ticket.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ticket.model.dto.UserCert;
import ticket.service.UserService;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet{
	private UserService userService = new UserService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 取得 session
		HttpSession session = req.getSession();
		Integer login = 0;
		String userName = null;
		String userRole = null;
		// 判斷是否登入
		if (session.getAttribute("userCert")!=null) {
			login = 1;
			UserCert userCert = (UserCert)session.getAttribute("userCert");
			userName = userService.getUser(userCert.getUserId()).getUsername();
			userRole = userCert.getRole();
			req.setAttribute("userName", userName);
			req.setAttribute("userRole", userRole);
		}
		req.setAttribute("login", login);		
		
		req.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(req, resp);
		return;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String phonenumber = req.getParameter("phonenumber");
		
		String userRole = "ROLE_USER";
		Object message = userService.appendUser(username, phonenumber, password, email, userRole);

		req.setAttribute("message", message);
		req.getRequestDispatcher("/WEB-INF/view/result.jsp").forward(req, resp);
	}

}
