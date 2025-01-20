package com.example.demo.ticket.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private Integer userId; // 使用者ID
	private String username; // 使用者名稱
	private String phonenumber; //手機
	private String email; // 電子郵件
	private String role; // 角色權限
	private String password;
	
	public UserDto(String username, String phonenumber, String password, String email, String role) {
		this.username = username;
		this.phonenumber = phonenumber;
		this.password = password;
		this.email = email;
		this.role = role;
	}
}
