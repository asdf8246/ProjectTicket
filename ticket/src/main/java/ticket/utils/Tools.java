package ticket.utils;

import java.io.IOException;
import java.io.InputStream;

public class Tools {
	
	public String detectImageFormat(InputStream inputStream) throws IOException {
	    byte[] header = new byte[4];
	    
	    // 讀取圖片的前4個字節
	    inputStream.read(header);

	    // 判斷 JPEG 格式
	    if (header[0] == (byte) 0xFF && header[1] == (byte) 0xD8) {
	        return "image/jpeg";
	    }

	    // 判斷 PNG 格式
	    if (header[0] == (byte) 0x89 && header[1] == (byte) 0x50 &&
	        header[2] == (byte) 0x4E && header[3] == (byte) 0x47) {
	        return "image/png";
	    }

	    // 判斷 GIF 格式
	    if (header[0] == (byte) 0x47 && header[1] == (byte) 0x49 &&
	        header[2] == (byte) 0x46 && header[3] == (byte) 0x38) {
	        return "image/gif";
	    }
	    
	    /*
	    // 判斷 BMP 格式
	    if (header[0] == (byte) 0x42 && header[1] == (byte) 0x4D) {
	        return "image/bmp";
	    }

	    // 判斷 TIFF 格式
	    if (header[0] == (byte) 0x49 && header[1] == (byte) 0x49 && 
	        header[2] == (byte) 0x2A && header[3] == (byte) 0x00) {
	        return "image/tiff";
	    }

	    // 判斷 WebP 格式
	    if (header[0] == (byte) 0x52 && header[1] == (byte) 0x49 &&
	        header[2] == (byte) 0x46 && header[3] == (byte) 0x46) {
	        byte[] webpHeader = new byte[4];
	        inputStream.read(webpHeader);
	        // 檢查 WebP 的後續字節
	        if (webpHeader[0] == (byte) 0x57 && webpHeader[1] == (byte) 0x45 &&
	            webpHeader[2] == (byte) 0x42 && webpHeader[3] == (byte) 0x50) {
	            return "image/webp";
	        }
	    }

	    // 判斷 HEIF 格式
	    if (header[0] == (byte) 0x66 && header[1] == (byte) 0x74 &&
	        header[2] == (byte) 0x79 && header[3] == (byte) 0x70) {
	        byte[] heifHeader = new byte[4];
	        inputStream.read(heifHeader);
	        // 檢查 HEIF 的後續字節
	        if (heifHeader[0] == (byte) 0x68 && heifHeader[1] == (byte) 0x65 &&
	            heifHeader[2] == (byte) 0x69 && heifHeader[3] == (byte) 0x63) {
	            return "image/heif";
	        }
	    }

	    // 判斷 JPEG 2000 格式
	    if (header[0] == (byte) 0x00 && header[1] == (byte) 0x00 &&
	        header[2] == (byte) 0x00 && header[3] == (byte) 0x0C) {
	        byte[] jp2Header = new byte[4];
	        inputStream.read(jp2Header);
	        // 檢查 JPEG 2000 的後續字節
	        if (jp2Header[0] == (byte) 0x6A && jp2Header[1] == (byte) 0x50 &&
	            jp2Header[2] == (byte) 0x20 && jp2Header[3] == (byte) 0x20) {
	            return "image/jp2";
	        }
	    }
		*/
		
	    // 如果無法識別，返回二進制流
	    return "application/octet-stream";
	}
}
