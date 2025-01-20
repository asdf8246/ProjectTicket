package ticket.model.dto;

import lombok.Data;

@Data
public class UserDto {
	private Integer userId; // 使用者ID
	private String username; // 使用者名稱
	private String userPhonenumber; //手機
	private String userEmail; // 電子郵件
	private String userRole; // 角色權限
}
