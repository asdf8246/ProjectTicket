package ticket.model.entity;

import lombok.Data;

//代表 users 資料表的紀錄
/**
* 建立 users 資料表
* 	user_id int primary key auto_increment comment '使用者ID',
	username varchar(255) not null comment '使用者姓名',
	phonenumber varchar(255) not null unique comment '手機',
 	password_hash varchar(255) not null comment '使用者Hash密碼',
 	salt varchar(255) not null comment '隨機鹽',
 	email varchar(255) not null unique comment '電子郵件',
 	role varchar(255) not null default 'ROLE_USER' comment '角色權限'
*/
@Data
public class User {
	private Integer Id; // 使用者ID
	private String name; // 使用者名稱
	private String phonenumber; //手機
	private String passwordHash; // 使用者Hash密碼
	private String salt; // 隨機鹽
	private String email; // 電子郵件
	private String role; // 角色權限
}