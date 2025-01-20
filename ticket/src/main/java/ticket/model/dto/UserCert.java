package ticket.model.dto;

//使用者憑證
//登入成功後會得到憑證資料( 只有 Getter )
public class UserCert {
	private Integer userId; // 使用者ID
	private String userPhonenumber; // 使用者名稱
	private String role; // 角色權限
	
	public UserCert(Integer userId, String userPhonenumber, String role) {
		this.userId = userId;
		this.userPhonenumber = userPhonenumber;
		this.role = role;
	}
	
	
	
	public Integer getUserId() {
		return userId;
	}
	public String getUserPhonenumber() {
		return userPhonenumber;
	}
	public String getRole() {
		return role;
	}



	@Override
	public String toString() {
		return "UserCert [userId=" + userId + ", userPhonenumber=" + userPhonenumber + ", role=" + role + "]";
	}
	
	
	
	
}
