package ticket.repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ticket.model.entity.User;



public class UserDaoImpl implements UserDao {

	@Override
	public List<User> findAllUsers() {
		List<User> users = new ArrayList<User>();
		String sql = "select user_id, username, phonenumber, password_hash, salt, email, role from users";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql)) {
				//逐筆尋訪
				while (rs.next()) {
					//建立 user 物件並將資料配置進去
					User user = new User();
					user.setId(rs.getInt("user_id"));
					user.setName(rs.getString("username"));
					user.setPhonenumber(rs.getString("phonenumber"));
					user.setPasswordHash(rs.getString("password_hash"));
					user.setSalt(rs.getString("salt"));
					user.setEmail(rs.getString("email"));
					user.setRole(rs.getString("role"));
					// 將 user 物件放到 users 集合中保存
					users.add(user);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users; // 回傳有 user 物件的集合
	}

	@Override
	public User getUser(String phonenumber) {
		String sql = "select user_id, username, phonenumber, password_hash, salt, email, role from users where phonenumber=?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, phonenumber);// 第一個 ? 放 phonenumber
				
				try(ResultSet rs = pstmt.executeQuery()){
					if (rs.next()) { // 若有得到一筆
						// 建立 user 物件並將資料配置進去
						User user = new User();
						user.setId(rs.getInt("user_id"));
						user.setName(rs.getString("username"));
						user.setPhonenumber(rs.getString("phonenumber"));
						user.setPasswordHash(rs.getString("password_hash"));
						user.setSalt(rs.getString("salt"));
						user.setEmail(rs.getString("email"));
						user.setRole(rs.getString("role"));
						return user; // 返回 user 物件
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public User getUserByEmail(String account) {
		String sql = "select user_id, username, phonenumber, password_hash, salt, email, role from users where email=?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, account);// 第一個 ? 放 phonenumber
				
				try(ResultSet rs = pstmt.executeQuery()){
					if (rs.next()) { // 若有得到一筆
						// 建立 user 物件並將資料配置進去
						User user = new User();
						user.setId(rs.getInt("user_id"));
						user.setName(rs.getString("username"));
						user.setPhonenumber(rs.getString("phonenumber"));
						user.setPasswordHash(rs.getString("password_hash"));
						user.setSalt(rs.getString("salt"));
						user.setEmail(rs.getString("email"));
						user.setRole(rs.getString("role"));
						return user; // 返回 user 物件
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public User getUser(Integer userId) {
		String sql = "select user_id, username, phonenumber, password_hash, salt, email, role from users where user_id=?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, userId);
				
				try(ResultSet rs = pstmt.executeQuery()){
					if (rs.next()) { // 若有得到一筆
						// 建立 user 物件並將資料配置進去
						User user = new User();
						user.setId(rs.getInt("user_id"));
						user.setName(rs.getString("username"));
						user.setPhonenumber(rs.getString("phonenumber"));
						user.setPasswordHash(rs.getString("password_hash"));
						user.setSalt(rs.getString("salt"));
						user.setEmail(rs.getString("email"));
						user.setRole(rs.getString("role"));
						return user; // 返回 user 物件
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object addUser(User user) {
		String sql = "insert into users(username, phonenumber, password_hash, salt, email, role) value(?, ?, ?, ?, ?, ?)";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, user.getName());
				pstmt.setString(2, user.getPhonenumber());
				pstmt.setString(3, user.getPasswordHash());
				pstmt.setString(4, user.getSalt());
				pstmt.setString(5, user.getEmail());
				pstmt.setString(6, user.getRole());
				
				int rowcount = pstmt.executeUpdate(); // 執行 update 若成功則回傳 1
				if (rowcount !=1) {
					throw new RuntimeException("新增失敗");
				}
				String message = "新增成功";
				return message;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "註冊失敗，手機或電子郵件重複!";
		}
	}

	

	@Override
	public void updateName(Integer Id, String name) {
		String sql = "update users set username = ? where user_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, name);
				pstmt.setInt(2, Id);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + Id + "name:" + name);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateEmail(Integer Id, String email) {
		String sql = "update users set email = ? where user_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, email);
				pstmt.setInt(2, Id);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + Id + "email:" + email);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateUserRole(Integer Id, String role) {
		String sql = "update users set role = ? where user_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, role);
				pstmt.setInt(2, Id);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + Id + "role:" + role);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteUser(Integer Id) {
		String sql = "delete from users where user_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				
				pstmt.setInt(1, Id);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("刪除失敗 Id:" + Id);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updatePasswordHash(Integer Id, String newPasswordHash) {
		String sql = "update users set password_hash=? where user_id=? ";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, newPasswordHash);
				pstmt.setInt(2, Id);
	
				pstmt.executeUpdate(); // 更新
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
