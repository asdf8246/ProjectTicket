package com.example.demo.ticket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.ticket.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
		/* 多筆:查詢所有使用者
		@Query("select u from User u")
		List<User> findAllUsers();
		
		用findAll()
		*/
		
		/*// 根據 userId 查詢
	    Optional<User> findById(Integer userId);
	    
	    用findById()
	    */
	
		// 根據電話號碼查詢
		@Query("select u from User u where u.phonenumber = :phonenumber")
	    Optional<User> findByPhonenumber(String phonenumber);
		
	    
		// 根據 Email 查詢
		@Query("select u from User u where u.email = :email")
	    Optional<User> findByEmail(String email);
		
		/*// 新增
		Object addUser(User user);
		
		用save()
		*/
		
		// 修改姓名
		@Modifying
	    @Query("UPDATE User u SET u.username = :name WHERE u.userId = :userId")
	    void updateName(@Param("userId") Integer userId, @Param("name") String name);
		
		// 修改 email
		@Modifying
	    @Query("UPDATE User u SET u.email = :email WHERE u.userId = :userId")
	    void updateEmail(@Param("userId") Integer userId, @Param("email") String email);
		
		// 修改 role 狀態
		@Modifying
	    @Query("UPDATE User u SET u.role = :role WHERE u.userId = :userId")
	    void updateUserRole(@Param("userId") Integer userId, @Param("role") String role);
		
		//修改密碼
		@Modifying
	    @Query("UPDATE User u SET u.passwordHash = :passwordHash WHERE u.userId = :userId")
	    void updatePasswordHash(@Param("userId") Integer userId, @Param("passwordHash") String newPasswordHash);
		

		/*//刪除: 根據 userId 來刪除
		void deleteUser(Integer userId);
		
		用deleteById()
		*/
}
