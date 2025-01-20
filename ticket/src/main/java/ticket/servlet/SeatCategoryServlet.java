package ticket.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ticket.service.SeatCategoriesService;

@WebServlet(urlPatterns = {"/SeatCategory/*"})
public class SeatCategoryServlet extends HttpServlet {
	
	private SeatCategoriesService seatCategoriesService = new SeatCategoriesService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo.equals("/delete")) {
			String eventId = req.getParameter("eventId");
			String seatCategoryId = req.getParameter("seatCategoryId");
			seatCategoriesService.deleteSeatCategorie(seatCategoryId);
			resp.sendRedirect("/ticket/event/get?eventId=" + eventId);
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
}
