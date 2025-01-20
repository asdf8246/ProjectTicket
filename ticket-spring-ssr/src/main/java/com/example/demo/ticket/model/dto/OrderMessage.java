package com.example.demo.ticket.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage implements Serializable {
	private String userId;               // 用戶ID
    private String eventId;               // 事件ID
    private String eventName;             // 事件名稱
    private String[] seatPrices;          // 座位價格
    private String[] numSeatss;           // 座位數量
    private String[] seatCategoryIds;     // 座位類別ID
}
