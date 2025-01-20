package ticket.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/captcha")
public class CaptchaServlet extends HttpServlet {
    
    // 設定圖形碼的寬度和高度
    private static final int WIDTH = 170;
    private static final int HEIGHT = 50;
    
    // 設定字母和數字的集合
    private static final String CHAR_SET = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz1234567890";
    
    // 設定圖片中幾個隨機的干擾線
    private static final int LINE_COUNT = 5;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	// 檢查是否進入此方法
        System.out.println("Generating CAPTCHA image...");
    	
    	response.setContentType("image/jpeg");
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        
        // 設定背景顏色
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
         
        // 設定圖形碼
        String captchaCode = generateCaptchaCode();
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.RED);
        g.drawString(captchaCode, 20, 40);

        // 設定干擾線
        Random rand = new Random();
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < LINE_COUNT; i++) {
            int x1 = rand.nextInt(WIDTH);
            int y1 = rand.nextInt(HEIGHT);
            int x2 = rand.nextInt(WIDTH);
            int y2 = rand.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }
        
        // 儲存到 session 中，以便驗證
        request.getSession().setAttribute("captcha", captchaCode);
        
        // 將圖片輸出到響應流
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    // 隨機生成圖形碼
    private String generateCaptchaCode() {
        Random rand = new Random();
        StringBuilder captchaCode = new StringBuilder(6);  // 6 位圖形碼
        for (int i = 0; i < 6; i++) {
            captchaCode.append(CHAR_SET.charAt(rand.nextInt(CHAR_SET.length())));
        }
        return captchaCode.toString();
    }
}