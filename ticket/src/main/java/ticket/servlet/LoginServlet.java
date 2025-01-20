package ticket.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ticket.exception.CertException;
import ticket.model.dto.UserCert;
import ticket.service.CertService;


@WebServlet("/login")
public class LoginServlet extends HttpServlet{
	private CertService certService = new CertService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 取得 session
		HttpSession session = req.getSession();
				
		// 判斷是否登入
		if (session.getAttribute("userCert")!=null) {
			resp.sendRedirect("/ticket/home"); // 重導回首頁
			return;
		}
		
		Integer login = 0;
		req.setAttribute("login", login);
		// 重導到 login.jsp
		req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String account = req.getParameter("account");
		String password = req.getParameter("password");
		String captcha = req.getParameter("captcha");
		//驗證帳密取得憑證
		UserCert userCert = null;
		
		Integer login = 0;
		req.setAttribute("login", login);
		
        // 從 session 中獲取生成的 CAPTCHA
        String captchaInSession = (String)req.getSession().getAttribute("captcha");
		
        if (captcha == null || !captcha.equalsIgnoreCase(captchaInSession)) {
        	// CAPTCHA 驗證失敗，提示錯誤
            req.setAttribute("error", "驗證碼錯誤!");
            req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
            return;
		}
        
		
		try {
			userCert = certService.gerCert(account, password);
		} catch (CertException e) {
			req.setAttribute("error", e.getMessage());
	        req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
			return;
		}
		
		//將憑證放入 session 變數中以利其他城市進行取用與驗證
		HttpSession session = req.getSession();
		session.setAttribute("userCert", userCert);// 放憑證
		req.setAttribute("locale", req.getLocale());// 取得客戶端所在地 例如: zh_TW
		
		req.setAttribute("message", "登入成功");
		// 檢查 session 中的 redirectURL 是否有資料
		if (session.getAttribute("redirectURL")==null) {
			resp.sendRedirect("/ticket/home");
		} else {
			String redirectURL = session.getAttribute("redirectURL").toString();
			resp.sendRedirect(redirectURL);
			session.setAttribute("redirectURL", null); // 清空 "redirectURL"
		}
		
	}
	
}
