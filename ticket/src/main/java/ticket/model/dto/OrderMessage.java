package ticket.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderMessage implements Serializable {
	private String userId;               // 用戶ID
    private String eventId;               // 事件ID
    private String eventName;             // 事件名稱
    private String[] seatPrices;          // 座位價格
    private String[] numSeatss;           // 座位數量
    private String[] seatCategoryIds;     // 座位類別ID
    
    
    
    // 构造函数
    public OrderMessage(String userId, String eventId, String eventName, 
                        String[] seatPrices, String[] numSeatss, String[] seatCategoryIds) {
        this.userId = userId;
        this.eventId = eventId;
        this.eventName = eventName;
        this.seatPrices = seatPrices;
        this.numSeatss = numSeatss;
        this.seatCategoryIds = seatCategoryIds;
    }
}
