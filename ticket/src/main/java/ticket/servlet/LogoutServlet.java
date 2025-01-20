package ticket.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		// 取得 session
		HttpSession session = req.getSession();
		
		// 判斷是否登入
		if (session.getAttribute("userCert")==null) {
			resp.sendRedirect("/ticket/home"); // 重導回首頁
			return;
		}
		
		// 將 session 失效
		session.invalidate(); //所有 session 失效，sessionId 會重發
		//session.setAttribute("userCert", null); // 只讓憑證的 session變數 失效，但 sessionId 不會重發
		
		String message = "登出成功";
		req.setAttribute("message", message);
		req.getRequestDispatcher("/WEB-INF/view/result.jsp").forward(req, resp);
	}
	
	
}
