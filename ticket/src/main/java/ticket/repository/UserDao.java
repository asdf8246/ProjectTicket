package ticket.repository;

import java.util.List;

import ticket.model.entity.User;





// User (DAO: Data Access Object)
public interface UserDao {
	// 多筆:查詢所有使用者
	List<User> findAllUsers();
	
	// 單筆:根據 phonenumber 查詢該筆使用者
	User getUser(String phonenumber);
	
	User getUser(Integer userId);
	
	User getUserByEmail(String account);
	
	// 新增
	Object addUser(User user);
	
	// 修改姓名
	void updateName(Integer Id,String name);
	
	// 修改 email
	void updateEmail(Integer Id, String email);
	
	// 修改 role 狀態
	void updateUserRole(Integer Id, String role);
	
	//刪除: 根據 userId 來刪除
	void deleteUser(Integer Id);
	
	//修改密碼
	void updatePasswordHash(Integer Id, String newPasswordHash);
}
