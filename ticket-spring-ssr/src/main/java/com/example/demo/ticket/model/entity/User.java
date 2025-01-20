package com.example.demo.ticket.model.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")  // 對應資料表名稱
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 對應自動增量的 user_id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "phonenumber", nullable = false, length = 255, unique = true)
    private String phonenumber;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "salt", nullable = false, length = 255)
    private String salt;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "role", nullable = false, length = 255)
    private String role = "ROLE_USER";  // 預設值
    
}