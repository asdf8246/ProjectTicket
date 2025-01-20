package ticket.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.catalina.connector.ClientAbortException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ticket.model.dto.EventDto;
import ticket.service.EventService;
import ticket.utils.Tools;


@WebServlet("/image")
public class ImageServlet extends HttpServlet {
	EventService eventService = new EventService();
	Tools tools = new Tools();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String eventId = req.getParameter("id");
		
		if (eventId != null) {
			EventDto eventDto = eventService.getEvent(eventId);
			InputStream eventImage = eventDto.getEventImage();
			
			// 確保圖片流存在
	        if (eventImage == null) {
	            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "圖片未找到");
	            return;
	        }
			
			// 設定響應的內容類型
			/*String contentType = tools.detectImageFormat(eventImage);
			System.out.println(contentType);
			resp.setContentType(contentType);
			resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			resp.setHeader("Pragma", "no-cache");
			resp.setDateHeader("Expires", 0);*/
	        resp.setContentType("application/octet-stream"); //搞了上面一堆都沒用，只寫這樣就成功了
			
			// 确保在响应未提交的情况下写入图片
			if (!resp.isCommitted()) {
			    try (OutputStream out = resp.getOutputStream()) {
			        byte[] buffer = new byte[1024];
			        int bytesRead;
			        
			        // 确保eventImage文件流正确读取
			        while ((bytesRead = eventImage.read(buffer)) != -1) {
			            try {
			                // 写数据到输出流
			                out.write(buffer, 0, bytesRead);
			            } catch (ClientAbortException e) {
			                // 客户端中止连接时，输出日志
			                System.out.println("客户端中止连接，停止写入数据");
			                return; // 客户端已中止连接，退出处理
			            } catch (IOException e) {
			                // 捕获其它IO异常
			                System.out.println("写入数据时发生 I/O 错误: " + e.getMessage());
			                return;
			            }
			        }
			        out.flush(); // 确保所有数据已刷新到客户端
			        //System.out.println("图片数据写入成功");
			        eventImage.close();

			    } catch (IOException e) {
			        // 捕获获取输出流时的IO异常
			        System.out.println("获取输出流时发生错误: " + e.getMessage());
			    }
			} else {
			    System.out.println("响应已经提交，无法继续写入图片数据");
			}
		}else {
	        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少 eventId 參數");
	    }
	}
}
