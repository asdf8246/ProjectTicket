package ticket.service;

import java.util.ArrayList;
import java.util.List;

import ticket.exception.PasswordInvalidException;
import ticket.exception.UserNotFoundException;
import ticket.model.dto.UserDto;
import ticket.model.entity.User;
import ticket.repository.UserDao;
import ticket.repository.UserDaoImpl;
import ticket.utils.Hash;

//UserService 是給 UserServlet(Controller) 使用
public class UserService {
	private UserDao userDao = new UserDaoImpl();

	// 所有使用者
	public List<UserDto> findAll() {
		List<UserDto> userDtos = new ArrayList<>();
		// 將 User 轉 UserDto
		// 向 userDao 索取 List<User> 集合
		List<User> users = userDao.findAllUsers();
		for (User user : users) {
			// 一個一個將 User 轉成 UserDto 並放在 userDtos 集合中
			UserDto userDto = new UserDto();
			userDto.setUserId(user.getId());
			userDto.setUsername(user.getName());
			userDto.setUserPhonenumber(user.getPhonenumber());
			userDto.setUserEmail(user.getEmail());
			userDto.setUserRole(user.getRole());

			userDtos.add(userDto);
		}
		return userDtos;
	}

	// 新增使用者
	public Object appendUser(String username, String phonenumber, String password, String email, String role) {
		String salt = Hash.getSalt(); // 得到隨機鹽
		String passwordHash = Hash.getHash(password, salt); // 得到 hash
		// 根據上列參數封裝到 User 物件中
		User user = new User();
		user.setName(username);
		user.setPhonenumber(phonenumber);
		user.setPasswordHash(passwordHash);
		user.setSalt(salt);
		user.setEmail(email);
		user.setRole(role);
		// 存入(新增使用者): 調用 userDao.addUser(user)
		Object message = userDao.addUser(user);
		return message;
	}

	// 刪除使用者
	public void deleteUser(String userId) {
		userDao.deleteUser(Integer.parseInt(userId));
	}

	// 取得指定使用者
	public UserDto getUser(String phonenumber) {
		User user = userDao.getUser(phonenumber);
		if (user == null) {
			return null;
		}
		// 一個一個將 User 轉成 UserDto 並放在 userDto 集合中
		UserDto userDto = new UserDto();
		userDto.setUserId(user.getId());
		userDto.setUsername(user.getName());
		userDto.setUserPhonenumber(user.getPhonenumber());
		userDto.setUserEmail(user.getEmail());
		userDto.setUserRole(user.getRole());
		return userDto;
	}

	// 取得指定使用者 by userId
	public UserDto getUser(Integer userId) {
		User user = userDao.getUser(userId);
		if (user == null) {
			return null;
		}
		// 一個一個將 User 轉成 UserDto 並放在 userDto 集合中
		UserDto userDto = new UserDto();
		userDto.setUserId(user.getId());
		userDto.setUsername(user.getName());
		userDto.setUserPhonenumber(user.getPhonenumber());
		userDto.setUserEmail(user.getEmail());
		userDto.setUserRole(user.getRole());
		return userDto;
	}

	// 修改使用者
	public void updateUser(String userId, String username, String phonenumber, String email, String role) {
		if (!username.isEmpty()) {
			userDao.updateName(Integer.parseInt(userId), username);
		}
		if (!email.isEmpty()) {
			userDao.updateEmail(Integer.parseInt(userId), email);
		}
		if (!role.isEmpty()) {
			userDao.updateUserRole(Integer.parseInt(userId), role);
		}
	}

	// 變更密碼
	public void updatePassword(Integer userId, String phonenumber, String oldPassword, String newPassword)
			throws UserNotFoundException, PasswordInvalidException {
		User user = userDao.getUser(phonenumber);
		if (user == null) {
			throw new UserNotFoundException();
		}

		// 比對修改之前的 password 是否正確
		String oldPasswordHash = Hash.getHash(oldPassword, user.getSalt());
		if (!oldPasswordHash.equals(user.getPasswordHash())) {
			throw new PasswordInvalidException();
		}

		// 產生新密碼的 Hash
		String newPasswordHash = Hash.getHash(newPassword, user.getSalt());
		// 密碼 Hash 修改
		userDao.updatePasswordHash(userId, newPasswordHash);
	}
}
