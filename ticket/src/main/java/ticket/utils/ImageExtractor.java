package ticket.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.OutputStream;

import ticket.model.dto.EventDto;
import ticket.model.entity.Events;
import ticket.repository.BaseDao;

public class ImageExtractor extends BaseDao {
	public static void main(String[] args) {

        int eventId = 5; // 假設你要提取 eventId = 1 的圖片
            String sql = "SELECT event_image FROM events WHERE event_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, eventId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // 提取 BLOB 數據
                        Blob imageBlob = rs.getBlob("event_image");
                        if (imageBlob != null) {
                            // 轉換成 InputStream
                            try (InputStream inputStream = imageBlob.getBinaryStream()) {
                            	
                            	/*Events events = new Events();
                            	events.setEventImage(inputStream);
                            	EventDto eventDto = new EventDto();
                            	eventDto.setEventImage(events.getEventImage());
                            	InputStream iStream = eventDto.getEventImage();*/
                            	
                                // 保存圖片數據到本地文件
                                File outputFile = new File("output_image.jpg"); // 替換為合適的文件路徑
                                try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                                    byte[] buffer = new byte[1024];
                                    int bytesRead;
                                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, bytesRead);
                                    }
                                }
                                System.out.println("圖片已保存為 " + outputFile.getAbsolutePath());
                            }
                        } else {
                            System.out.println("找不到圖片數據");
                        }
                    } else {
                        System.out.println("未找到符合的事件");
                    }
                }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
	}
}
